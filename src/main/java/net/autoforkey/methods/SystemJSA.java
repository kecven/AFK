package net.autoforkey.methods;

import java.awt.*;
import java.util.function.Predicate;

/**
 * Created by andrei on 05.07.15.
 */
public interface SystemJSA {

    /**
     * print text to console
     * @param msg
     */
    public void print(String msg);

    /**
     * println text to console
     */
    public void println();

    /**
     * println text to console
     * @param msg
     */
    public void println(String msg);

    /**
     * print text to console
     * @param msg
     */
    public void printError(String msg);

    /**
     * println text to console
     * @param msg
     */
    public void printlnError(String msg);

    /**
     * sleep
     * @param time
     */
    public void sleep(long time);

    /**
     * получить текущее количество милисикунд
     */
    public long time();

    /**
     * Запуск функции в отдельнос потоке
     */
    public void run(String scriptName, Predicate<Object> func);

    /**
     * Запуск функции в отдельнос потоке
     */
    public void run(Predicate<Object> func);

    /**
     * Выполнение команды
     */
    public Process exec(String command);

    /**
     * получить цвет пикселя
     */
    public Color getPixelColor(int x, int y);

    /**
     * Тип системы(Linux / Windows)..
     */
    public String getOs();

    /**
     * Информация о системе
     */
    public String sysInfo(String info);

    /**
     * Вывести информационное сообщение
     * @param text
     * @param button
     * @param time
     * @param x
     * @param y
     */
    public void alert(String text, String button, int time, int x, int y);

    /**
     * Получить разрешение экрана
     * @return
     */
    public Dimension getScreenSize();

    /**
     * Послать сообщение другому скрипту
     * @return
     */
    public void sendSignal(String script, String msg);

    /**
     * Получить список всех сообщений для данного скрипта
     * @return
     */
    public java.util.List getSignal(String script);


}
