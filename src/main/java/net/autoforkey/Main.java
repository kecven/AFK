package net.autoforkey;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by andrei on 05.07.15.
 */
public class Main {
    public static ExecutorService executors = Executors.newCachedThreadPool();
    public static void main(String[] args) {
        for(int i = 0; i < args.length; i++){
            runScript(new ScriptRunnable("main", args[i]));
        }
    }

    public static void runScript(ScriptRunnable thread){
        //executors.submit(thread);
        //executors.execute(thread);
        new Thread(thread).start();
    }
}
