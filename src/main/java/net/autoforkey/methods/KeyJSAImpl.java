package net.autoforkey.methods;

import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;
import net.autoforkey.Main;
import net.autoforkey.ScriptRunnable;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by andrei on 05.07.15.
 */
public class KeyJSAImpl extends SystemJSAImpl implements KeyJSA, ClipboardOwner {

    //private Robot robot = SystemJSAImpl.robot;
    private static Provider provider = null;
    private static final List<Integer> MODIFIERS = Arrays.asList(KeyEvent.VK_ALT, KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT, KeyEvent.VK_META);
    private static Map<String, Integer> staticKeys = initKeys();

    private static Map<String, Integer> initKeys() {
        Map<String, Integer> keys;
        Class c = java.awt.event.KeyEvent.class;
        Field[] fields = c.getFields();

        keys = new java.util.concurrent.ConcurrentHashMap<>(256);

        for (int i = 0; i < fields.length; i++) {
            if ((fields[i].getName().indexOf("VK_")) == 0 && (fields[i].getType().getTypeName().equals("int"))) {
                String key = fields[i].getName().substring(3);
                if ("ALT".equals(key) || "CTRL".equals(key) || "SHIFT".equals(key)) {
                    key = key.toLowerCase();
                }

                Field field = null;
                try {
                    field = KeyEvent.class.getDeclaredField(fields[i].getName());
                } catch (NoSuchFieldException e) {
                }

                Integer value = null;
                try {
                    value = (Integer) field.get(fields[i]);
                } catch (IllegalAccessException e) {
                }

                keys.put(key, value);
            }
        }

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

        for (String s : keys) {
            down(getKey(s));
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
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }

    public void setClipboard(String data) {
        stringSelection = new StringSelection(data);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, this);
    }

    public String getClipboard() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        try {
            return (String) clipboard.getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException | IOException ignored) {
        }
        return null;
    }

    public void setHotKey(String hotKey, Predicate<Object> func) {
        startHotKey();
        final HotKeyListener listener = key -> Main.runScript(new ScriptRunnable(hotKey, func));

        if (hotKey != null && hotKey.length() > 0) {
            try {
                provider.register(KeyStroke.getKeyStroke(hotKey), listener);
            } catch (Exception e) {
                System.err.println("Error init HotKey");
            }
        }
    }

    public void setSyncHotKey(String hotKey, Predicate<Object> func) {
        startHotKey();
        final HotKeyListener listener = key -> func.test(func);

        if (hotKey != null && hotKey.length() > 0) {
            try {
                provider.register(KeyStroke.getKeyStroke(hotKey), listener);
            } catch (Exception e) {
                System.err.println("Error init HotKey");
            }
        }
    }

    public void stopHotKey() {
        provider.reset();
        provider.stop();
        provider = null;
    }

    @Override
    public int getKey(String code) {
        return staticKeys.get(code);
    }

    public void startHotKey() {
        if (provider == null)
            provider = Provider.getCurrentProvider(true);
    }

    public void resetHotKey() {
        provider.reset();
    }

}
