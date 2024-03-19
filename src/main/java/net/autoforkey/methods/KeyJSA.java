package net.autoforkey.methods;

import java.util.function.Predicate;

/**
 * Created by andrei on 05.07.15.
 */
public interface KeyJSA {


    public void press(int key);

    public void press(String key);

    public void down(int key);

    public void up(int key);

    public void setClipboard(String text);

    public String getClipboard();

    public int getKey(String code);
}
