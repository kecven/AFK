package net.autoforkey.methods;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.MediaKey;
import com.tulskiy.keymaster.common.Provider;
import net.autoforkey.Main;
import net.autoforkey.ScriptRunnable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.function.Predicate;

/**
 * Created by andrei on 05.07.15.
 */
public class KeyJSAImpl extends SystemJSAImpl implements KeyJSA, ClipboardOwner {

    //private Robot robot = SystemJSAImpl.robot;
    private Provider provider = Provider.getCurrentProvider(true);
    private static final List<Integer> MODIFIERS = Arrays.asList(KeyEvent.VK_ALT, KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT, KeyEvent.VK_META);
    private Map<String, Integer> keys = staticKeys;
    private static Map<String, Integer> staticKeys = initKeys();

    private static Map initKeys(){
        Map<String, Integer> keys;
        Class c = java.awt.event.KeyEvent.class;
        Field[] fields = c.getFields();

        keys = new java.util.concurrent.ConcurrentHashMap<>(256);

        for (int i = 0; i < fields.length; i++){
            if ((fields[i].getName().indexOf("VK_")) == 0 && (fields[i].getType().getTypeName().equals("int"))) {
                String key = fields[i].getName().substring(3);
                if ("ALT".equals(key) || "CTRL".equals(key) || "SHIFT".equals(key)){
                    key = key.toLowerCase();
                }

                Field field = null;
                try {
                    field = KeyEvent.class.getDeclaredField(fields[i].getName());
                } catch (NoSuchFieldException e) {}

                Integer value = null;
                try {
                    value = (Integer) field.get(fields[i]);
                } catch (IllegalAccessException e) {}

                keys.put(key, value);
            }
        }

        //keys.forEach( (s, i) ->{System.out.println(s); });


        return keys;
    }

    @Override
    public void press(int key) {
        down(key);
        up(key);
    }

    @Override
    public void press(String key) {
        String[] keys = key.split(" ");

        for (int i = 0; i < keys.length; i++){
            down(getKey(keys[i]));
        }
        for (int i = keys.length - 1; i >= 0; i--) {
            up(getKey(keys[i]));
        }
    }

    @Override
    public void down(int key) {
        robot.keyPress(key);
    }

    @Override
    public void up(int key) {
        robot.keyRelease(key);
    }

        StringSelection stringSelection;
        @Override
        public void lostOwnership(Clipboard clipboard, Transferable contents) {}

        public void setClipboard(String data){
            stringSelection = new StringSelection(data);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, this);
        }

        public String getClipboard() {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            try {
                return (String) clipboard.getData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException | IOException e) { }
            return null;
        }

    public void setHotKey(String hotKey, Predicate<Object> func){
        final HotKeyListener listener = new HotKeyListener() {
            public void onHotKey(final HotKey key) {
                Main.executors.execute(new ScriptRunnable(hotKey, func));
            }
        };

        if (hotKey != null && hotKey.length() > 0) {
            try {
                provider.register(KeyStroke.getKeyStroke(hotKey), listener);
            } catch (Exception e) {
                System.err.println("Error init HotKey");
            }
        }
    }

    public void setSyncHotKey(String hotKey, Predicate<Object> func){
        final HotKeyListener listener = new HotKeyListener() {
            public void onHotKey(final HotKey key) {
                func.test(func);
            }
        };

        if (hotKey != null && hotKey.length() > 0) {
            try {
                provider.register(KeyStroke.getKeyStroke(hotKey), listener);
            } catch (Exception e) {
                System.err.println("Error init HotKey");
            }
        }
    }

    public void stopHotKey(){
        provider.reset();
        provider.stop();
    }

    @Override
    public int getKey(String code) {
        return keys.get(code);
    }

    public void startHotKey(){
        provider = Provider.getCurrentProvider(true);
    }

    public void resetHotKey(){
        provider.reset();
    }

}
