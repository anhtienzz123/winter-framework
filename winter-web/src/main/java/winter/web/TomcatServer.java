package winter.web;

import java.io.File;
import java.util.List;
import java.util.Optional;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import jakarta.servlet.http.HttpServlet;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import winter.web.annotation.RequestMapping;
import winter.web.model.ControllerHttpServlet;

@NoArgsConstructor
@AllArgsConstructor
public class TomcatServer {

    private Tomcat tomcat;

    private Context context;

    private static final String CONTEXT_PATH = "/";

    private static final Optional<String> PORT = Optional.ofNullable(System.getenv("PORT"));

    private static final Optional<String> HOSTNAME = Optional.ofNullable(System.getenv("HOSTNAME"));

    public void start(List<Object> controllers) {
        this.tomcat = new Tomcat();
        this.tomcat.setBaseDir("temp");
        this.tomcat.setPort(Integer.valueOf(PORT.orElse("8080")));
        this.tomcat.setHostname(HOSTNAME.orElse("localhost"));

        String docBase = new File(".").getAbsolutePath();
        this.context = tomcat.addContext(CONTEXT_PATH, docBase);

        controllers.forEach(this::addServlet);

        // start server
        this.tomcat.getConnector();
        try {
            this.tomcat.start();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
        this.tomcat.getServer().await();
    }

    public void addServlet(Object controller) {
        HttpServlet servlet = new ControllerHttpServlet(controller);
        String servletName = controller.getClass().getSimpleName();
        String url =
                controller.getClass().getDeclaredAnnotation(RequestMapping.class).value() + "/*";

        tomcat.addServlet(CONTEXT_PATH, servletName, servlet);
        context.addServletMappingDecoded(url, servletName);
    }
}
