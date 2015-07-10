package net.autoforkey.methods;

import net.autoforkey.Main;
import net.autoforkey.ScriptRunnable;
import net.autoforkey.form.App;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Predicate;
/**
 * System Function for afk
 * Created by andrei on 05.07.15.
 */
public class SystemJSAImpl implements SystemJSA {

    protected static Robot robot = initRobot();

    private static Robot initRobot(){
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e) {}
        return robot;
    }
    @Override
    public void print(String msg) {
        System.out.print(msg);
    }

    @Override
    public void println() {
        System.out.println();
    }

    @Override
    public void println(String msg) {
        System.out.println(msg);
    }

    @Override
    public void printError(String msg) {
        System.err.print(msg);
    }

    @Override
    public void printlnError(String msg) {
        System.err.println(msg);
    }

    @Override
    public void sleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) { }
    }

    @Override
    public long time() {
        return System.currentTimeMillis();
    }

    @Override
    public void run(String scriptName, Predicate<Object> func) {
        //Main.executors.execute(new ScriptRunnable(scriptName, func));
        Main.runScript(new ScriptRunnable(scriptName, func));
    }

    @Override
    public void run(Predicate<Object> func) {
        //Main.executors.execute(new ScriptRunnable(func));
        Main.runScript(new ScriptRunnable(func));
    }

    @Override
    public Process exec(String command) {

        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec(command);
        } catch (IOException e) {}
        return proc;
    }

    @Override
    public Color getPixelColor(int x, int y) {
        return robot.getPixelColor(x, y);
    }

    @Override
    public String getOs() {
        return System.getProperty("os.name");
    }

    @Override
    public String sysInfo(String info) {
        return System.getProperty(info);
    }

    /**
     * Класс который выводится при вызове alert()
     */

    @Override
    public void alert(String text, int time) {
        (new App(text, time)).information();

        /*
        FrameForAlert f = new FrameForAlert();

        try {
            f.start(text, button, time, x, y);
        } catch (Exception e) {}
        */
    }

    @Override
    public Dimension getScreenSize() {
        return  Toolkit.getDefaultToolkit ().getScreenSize ();
    }

    @Override
    public void sendSignal(String script, String msg) {
        java.util.List<String> list = ScriptRunnable.signals.get(script);

        if (list == null){
            list = new ArrayList<>();
            list.add(msg);
            ScriptRunnable.signals.put(script, list);
        }
    }

    @Override
    public java.util.List getSignal(String script) {
        java.util.List<String> list = ScriptRunnable.signals.remove(script);

        if (list == null){
            list = new ArrayList<>();
        }
        return list;
    }
}
