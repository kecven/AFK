package net.autoforkey;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by andrei on 05.07.15.
 */
public class ScriptRunnable implements Runnable {

    public static HashMap<String, List<String>> signals = new HashMap<>();

//    private static int countScripts = 0;

    private String fileName;
    private String scriptName;  //Название скрипта
    //private SystemJSA sys = new SystemJSAImpl();
    private List<String> includeOnceList = new ArrayList<>();

    private ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
    private ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("nashorn");

    /**
     * Функция для запуска в отдельном потоке
     */
    Predicate<Object> func = null;


    /**
     * Конструктор
     * @param file файл для запуска
     */
    public ScriptRunnable(String file){
        this.scriptName = file;
        this.fileName = file;
    }
    /**
     * Конструктор
     * @param scriptName название потока
     * @param file файл для запуска
     */
    public ScriptRunnable(String scriptName, String file){
        this.scriptName = scriptName;
        this.fileName = file;
    }

    /**
     * Конструктор
     * @param scriptName название потока
     * @param func функция для запуска
     */
    public ScriptRunnable(String scriptName, Predicate<Object> func){
        this.scriptName = scriptName;
        this.func = func;
    }

    /**
     * Конструктор
     * @param func функция для запуска
     */
    public ScriptRunnable(Predicate<Object> func){
        this.func = func;
    }

    @Override
    public void run() {
        if (scriptName != null) {
            Thread.currentThread().setName(scriptName);
        }
        //Если запускаем новый скрипт
        if (func == null) {
            InputStream systemJSA = ScriptRunnable.class.getResourceAsStream("methods/system.afk");
            eval(systemJSA, "methods/system.afk");

            eval(fileName);
        } else {
            //Запускаем новый поток в скрипте

        //    try {
        //        System.out.println("Script name = " + scriptName);
        //        scriptEngine.eval("var scriptName=\"" + scriptName +"\",fileName=\"" + fileName + "\";");
        //    } catch (ScriptException e) {}

            func.test(scriptName);
        }

    }

    private void eval(String fileName){
        if (includeOnceList.contains(fileName)){
            System.err.println("File " + fileName + " already include.");
            return;
        } else {
            includeOnceList.add(fileName);
        }


        try {
            eval(new FileInputStream(fileName), fileName);
        } catch (FileNotFoundException e) {
            System.err.println("File not found. " + fileName);
        }
    }

    private void eval(InputStream file, String fileName){
        try {
            String jsCode = getCodeFromStream(file);
            jsCode = runMacros(jsCode);
            scriptEngine.eval(jsCode);
        } catch (IOException e) {
            System.err.println("Error reading file  " + fileName + ".\n" + e.getMessage());
        } catch (ScriptException e) {
            System.err.println("Runtime error in file " + fileName + ".\n" + e.getMessage());
        }
    }

    /**
     * Подставляются удобные макросы
     * @param code
     * @return
     */
    private String runMacros(String code){

        StringBuilder jsCode = new StringBuilder(code);

        int hashCodeIndex = 0;
        int endIndex;
        int endIndex2;
/*
        //<КОСТЫЛЬ>
        //поиск макроса идёт по следующему выражению "\n#", если на первой строке идёт макрос, он ломанётся
        endIndex = jsCode.indexOf("\n");
        hashCodeIndex = code.indexOf(";#") + 1;
        if ((hashCodeIndex != 0) && hashCodeIndex < endIndex){
            jsCode = jsCode.replace(hashCodeIndex, endIndex, macro(jsCode.substring(hashCodeIndex, endIndex)));
        }
        //</КОСТЫЛЬ>

        hashCodeIndex = 0;
*/
        while ( -1 != (hashCodeIndex = jsCode.indexOf("\n#", hashCodeIndex))) {
            //нужно инкриментировать hashCodeIndex, что бы был endIndex != hashCodeIndex и вообще оно всё не будет работать
            //ищем конец строки макроса
            endIndex = jsCode.indexOf("\n", ++hashCodeIndex);
            endIndex2 = jsCode.indexOf(";", hashCodeIndex);

            if ((endIndex2 != -1) && (endIndex2 < endIndex)){
                endIndex = endIndex2;
            }

            //System.out.printf("hashCodeIndex = " + hashCodeIndex + " end = " + endIndex);

            //заменяем макрос на рабочий код
            jsCode.replace(hashCodeIndex, endIndex, macro(jsCode.substring(hashCodeIndex, endIndex)));
        }

        hashCodeIndex = 0;
        while ( -1 != (hashCodeIndex = jsCode.indexOf(";#", hashCodeIndex))) {
            //нужно инкриментировать hashCodeIndex, что бы был endIndex != hashCodeIndex и вообще оно всё не будет работать
            //ищем конец строки макроса
            endIndex = jsCode.indexOf("\n", ++hashCodeIndex);
            endIndex2 = jsCode.indexOf(";", hashCodeIndex);

            if ((endIndex2 != -1) && (endIndex2 < endIndex)){
                endIndex = endIndex2;
            }
            //System.out.printf("hashCodeIndex = " + hashCodeIndex + " end = " + endIndex);

            //заменяем макрос на рабочий код
            jsCode.replace(hashCodeIndex, endIndex, macro(jsCode.substring(hashCodeIndex, endIndex)));
        }


        return jsCode.toString();
    }

    /**
     * метод получает строку с макросом и должен её приобразовать в рабочий код
     * @param code
     * @return рабочий код
     */
    private String macro(String code){
        String result = "";

        if (code.charAt(code.length() - 1) == ';'){
            code = code.substring(0, (code.length() - 1));
        }

        if (code.indexOf("#include ") == 0){    //подключение библиотеки

            String includeFileName = code.substring(9).trim();
/*
            if (includeOnceList.contains(includeFileName)){
                return "";
            }
*/
            //Меняем имя исполняемого файла, что бы ошибки выдавало в правильном файле
            String last_fileName = this.fileName;
            this.fileName = includeFileName;

            eval(includeFileName);
            //Возвращаем имя файла
            this.fileName = last_fileName;

        } else if (code.indexOf("#require ") == 0){    //подключение библиотеки

            String includeFileName = code.substring(9).trim();

            try {
                String jsCode = getCodeFromStream(new FileInputStream(includeFileName));
                result = runMacros(jsCode);
            } catch (FileNotFoundException e) {
                System.err.println("File not found. " + includeFileName);
            } catch (IOException e) {
                System.err.println("Error reading file  " + includeFileName + ".\n" + e.getMessage());
            }


        } else if (code.indexOf("# ") == 0) {
            String[] kv = code.substring(2).split("->");

            if (kv.length != 2){
                System.err.println("Macros is bad");
                return result;
            }

            String key = kv[0].trim();
            String cmd = kv[1];
            result = "key.setHotKey('" + key + "', function(){" + cmd + "})";
        }


        return result;
    }

    /**
     * Чтение файла. Получить текст файла
     * @param file
     * @return
     * @throws IOException
     */
    private String getCodeFromStream(InputStream file) throws IOException {
        String firstStr = "var scriptName=\"" + scriptName +"\",fileName=\"" + fileName + "\";";

        StringBuilder javaScriptCode = new StringBuilder(firstStr);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(file, "UTF8"))) {
            String str;
            while ((str = in.readLine()) != null) {
                str = str.trim();
                javaScriptCode.append(str).append("\n");
            }
        }


        return javaScriptCode.toString();
    }
}
