package winter.web;

public class WinterWebApplication {

    public static void run(Class<?> clazz) {
        TomcatServer tomcatServer = new TomcatServer();
        tomcatServer.start();
    }
}
