package net.autoforkey;

/*
набор модулей:

 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * Created by andrei on 05.07.15.
 */
public class Main {
    public static ExecutorService executors = Executors.newCachedThreadPool();
    public static void main(String[] args) {

        for(int i = 0; i < args.length; i++){
            runScript(new ScriptRunnable("main", args[i]));
        }

    }

    public static void runScript(ScriptRunnable thread) {
        try {
            executors.submit(thread);
            executors.awaitTermination(10_000, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //executors.execute(thread);
//        new Thread(thread).start();

    }


}
