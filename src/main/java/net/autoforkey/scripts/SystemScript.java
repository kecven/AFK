package net.autoforkey.scripts;

public class SystemScript {
    public static final String SCRIPT_NAME = "SystemScript";
    public static final String SCRIPT = """
            var sys = Java.type("net.autoforkey.methods.SystemJSAImpl")
            sys = new sys()
            var mouse = Java.type("net.autoforkey.methods.MouseJSAImpl")
            mouse = new mouse()
                        
            var key = Java.type("net.autoforkey.methods.KeyJSAImpl")
            key = new key()
            //----------------------------------------------------------------
                        
            var byteArray = Java.type("byte[]");
            var String = Java.type("java.lang.String");
                        
            //----------------------------------------------------------------
                        
            function print(msg) {
                return sys.print(msg)
            }
                        
            function println() {
                return sys.println()
            }
                        
            function println(msg) {
                return sys.println(msg)
            }
                        
            function echo(name) {
                println(name)
            }
                        
            function printError(msg) {
                return sys.printError(msg)
            }
                        
            function printlnError(msg) {
                return sys.printlnError(msg)
            }
                        
            function sleep(time) {
                return sys.sleep(time)
            }
                        
            function time() {
                return sys.time()
            }
                        
            function exec(command) {
                return sys.exec(command)
            }
                        
            function getPixelColor(x, y) {
                return sys.getPixelColor(x, y)
            }
                        
            function readInputStream(stream) {
                var result = new String();
                        
                var myByteArray = new byteArray(1024);
                while (stream.read(myByteArray) != -1) {
                    result += new String(myByteArray)
                }
                        
                return result;
            }
                        
            function sysInfo(info) {
                        
            //        java.version Java Runtime Environment version
            //        java.vendor Java Runtime Environment vendor
            //        java.vendor.url Java vendor URL
            //        java.home Java installation directory
            //        java.vm.specification.version Java Virtual Machine specification version
            //        java.vm.specification.vendor Java Virtual Machine specification vendor
            //        java.vm.specification.name Java Virtual Machine specification name
            //        java.vm.version Java Virtual Machine implementation version
            //        java.vm.vendor Java Virtual Machine implementation vendor
            //        java.vm.name Java Virtual Machine implementation name
            //        java.specification.version Java Runtime Environment specification version
            //        java.specification.vendor Java Runtime Environment specification vendor
            //        java.specification.name Java Runtime Environment specification name
            //        java.class.version Java class format version number
            //        java.class.path Java class path
            //        java.library.path List of paths to search when loading libraries
            //        java.io.tmpdir Default temp file path
            //        java.compiler Name of JIT compiler to use
            //        java.ext.dirs Path of extension directory or directories
            //        os.name Operating system name
            //        os.arch Operating system architecture
            //        os.version Operating system version
            //        file.separator File separator ('/' on UNIX)
            //        path.separator Path separator (':' on UNIX)
            //        line.separator Line separator ('' on UNIX)
            //        user.name User's account name
            //        user.home User's home directory
            //        user.dir User's current working directory
                        
                return sys.sysInfo(info)
            }
                        
            function run(scriptName, func) {
                if (func == undefined){
                    sys.run(scriptName)
                } else {
                    sys.run(scriptName, func)
                }
            }
                        
                        
            function alert(text, time) {
                if (time == undefined){
                    time = 0
                }
                return sys.alert(text, time)
            }
                        
            function getKey(code){
                return key.getKey(code)
            }
            """;
}
