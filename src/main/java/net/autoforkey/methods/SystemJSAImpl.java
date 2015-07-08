package net.autoforkey.methods;

import net.autoforkey.Main;
import net.autoforkey.ScriptRunnable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
        Main.executors.execute(new ScriptRunnable(scriptName, func));
    }

    @Override
    public void run(Predicate<Object> func) {
        Main.executors.execute(new ScriptRunnable(func));
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
    private class FrameForAlert extends JFrame {
        public void start(String text, String button, int time, int x, int y) throws Exception {
            FrameForAlert frame = new FrameForAlert();
            frame.showDialog(text, button, time, x, y);
        }

        private class ConnectDialog extends JDialog {
            private JLabel label;
            public ConnectDialog(String text, String button, int time, int x, int y) {
                super((Frame)null, "", true);

                    if ("".equals(button)){
                        button = "OK";
                    }

                    JButton b = new JButton(button);
                    getContentPane().add(b, BorderLayout.SOUTH);

                    ConnectDialog.this.addKeyListener(new KeyListener() {
                        @Override
                        public void keyTyped(KeyEvent e) {

                        }

                        @Override
                        public void keyPressed(KeyEvent e) {
                            System.out.println(e.getKeyLocation());
                        }

                        @Override
                        public void keyReleased(KeyEvent e) {

                        }
                    });
                    b.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            new Hider(ConnectDialog.this, 0).start();

                        }
                    });


                label = new JLabel(text);
                getContentPane().add(label,BorderLayout.CENTER);

                if (time != 0){
                    new Hider(ConnectDialog.this, time).start();
                }


                pack();
                setLocation(x, y);
            }
        }
        //
        public void showDialog(String text, String button, int time, int x, int y) throws HeadlessException {
            JDialog dlg = new ConnectDialog(text, button, time, x, y);
            dlg.setVisible(true);
        }
        private class Hider extends Thread {
            private JDialog dialogToClose;
            private int time;

            public Hider(JDialog dialogToClose, int time) {
                this.dialogToClose = dialogToClose;
                this.time = time;
            }

            public void run() {
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                dialogToClose.setVisible(false);
            }
        }
    }
    @Override
    public void alert(String text, String button, int time, int x, int y) {


        FrameForAlert f = new FrameForAlert();

        try {
            f.start(text, button, time, x, y);
        } catch (Exception e) {}
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
