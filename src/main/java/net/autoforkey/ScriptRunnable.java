package net.autoforkey;

import net.autoforkey.scripts.SystemScript;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;

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
    private final List<String> includeOnceList = new ArrayList<>();
    Context context = Context.newBuilder("js")
            .allowHostClassLookup(s -> true)
            .allowHostAccess(HostAccess.ALL)
            .build();
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
            eval(SystemScript.SCRIPT, SystemScript.SCRIPT_NAME);

            eval(fileName);
        } else {
            //Запускаем новый поток в скрипте
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

    private void eval(String file, String fileName) {
        eval(new ByteArrayInputStream(file.getBytes()), fileName);
    }

    private void eval(InputStream file, String fileName){
        try {
            String jsCode = getCodeFromStream(file);
            jsCode = runMacros(jsCode);

            context.eval("js", jsCode);

        } catch (IOException e) {
            System.err.println("Error reading file  " + fileName + ".\n" + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error in file  " + fileName + ".\n" + e.getMessage());
        }
    }

    /**
     * Подставляются удобные макросы
     * @param code - code of script
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

            /*
            endIndex2 = jsCode.indexOf(";", hashCodeIndex);

            if ((endIndex2 != -1) && (endIndex2 < endIndex)){
                endIndex = endIndex2;
            }
            */
            //System.out.printf("hashCodeIndex = " + hashCodeIndex + " end = " + endIndex);

            //заменяем макрос на рабочий код
            jsCode.replace(hashCodeIndex, endIndex, macro(jsCode.substring(hashCodeIndex, endIndex)));
        }

        /*
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
        */


        //В начале мы добавляем к программе лишний Enter, по этому его нужно убрать
        return jsCode.toString().replaceFirst("\n\n", "\n");
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
        String firstStr = "var scriptName=\"" + scriptName +"\",fileName=\"" + fileName + "\";\n";

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
