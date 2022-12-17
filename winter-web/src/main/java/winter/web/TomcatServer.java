package winter.web;

import java.io.File;
import java.util.Optional;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import jakarta.servlet.http.HttpServlet;

public class TomcatServer {

    private Tomcat tomcat;

    private Context context;

    private static final String CONTEXT_PATH = "/";

    private static final Optional<String> PORT = Optional.ofNullable(System.getenv("PORT"));

    private static final Optional<String> HOSTNAME = Optional.ofNullable(System.getenv("HOSTNAME"));

    public void start() {
        this.tomcat = new Tomcat();
        this.tomcat.setBaseDir("temp");
        this.tomcat.setPort(Integer.valueOf(PORT.orElse("8080")));
        this.tomcat.setHostname(HOSTNAME.orElse("localhost"));

        String docBase = new File(".").getAbsolutePath();
        this.context = tomcat.addContext(CONTEXT_PATH, docBase);

        addServlet();

        // start server
        this.tomcat.getConnector();
        try {
            this.tomcat.start();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
        this.tomcat.getServer().await();
    }

    public void addServlet() {
        // add TestServlet
        HttpServlet servlet = new TestServlet();
        String servletName = "test";
        String urlPattern = "/test";

        tomcat.addServlet(CONTEXT_PATH, servletName, servlet);
        context.addServletMappingDecoded(urlPattern, servletName);
    }
}
