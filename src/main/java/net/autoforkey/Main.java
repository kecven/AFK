package net.autoforkey;

/*
набор модулей:

 */

/**
 *
 * Created by andrei on 05.07.15.
 */
public class Main {
    //public static ExecutorService executors = Executors.newCachedThreadPool();
    public static void main(String[] args) throws InterruptedException {

    //    Thread.sleep(10000);
        //executors.submit()
        for(int i = 0; i < args.length; i++){
            //executors.submit(new ScriptRunnable("main", args[i]));
            //new Thread(new ScriptRunnable("main", args[i])).start();
            runScript(new ScriptRunnable("main", args[i]));
            //System.out.println(args[i]);
        }

    }

    public static void runScript(ScriptRunnable thread){
        new Thread(thread).start();

    }


}
