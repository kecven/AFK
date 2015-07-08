package net.autoforkey;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
набор модулей:

 */

/**
 *
 * Created by andrei on 05.07.15.
 */
public class Main {
    public static ExecutorService executors = Executors.newCachedThreadPool();
    public static void main(String[] args) {
        //executors.execute(new ScriptRunnable("main", "script.afk"));
        //executors.execute(new ScriptRunnable("notSpoilEye", "notSpoilEye.afk"));

        //executors.execute(new ScriptRunnable("main", "script2.afk"));
        for(int i = 0; i < args.length; i++){
            executors.execute(new ScriptRunnable("main", args[i]));
        }

    }


}
