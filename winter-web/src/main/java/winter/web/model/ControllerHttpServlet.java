package winter.web.model;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import winter.web.annotation.GetMapping;
import winter.web.annotation.RequestMapping;

@NoArgsConstructor
@AllArgsConstructor
public class ControllerHttpServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private Object controller;

    private static final String APPLICATION_JSON = "application/json";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setApplicationJson(response);

        Gson gson = new Gson();
        PrintWriter writer = response.getWriter();

        String prefixUrl = controller.getClass().getAnnotation(RequestMapping.class).value();
        String url = request.getRequestURI();

        try {
            Object result = null;
            Method[] methods = controller.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(GetMapping.class)) {
                    String methodUrl =
                            prefixUrl + method.getDeclaredAnnotation(GetMapping.class).value();

                    if (methodUrl.equals(url)) {
                        result = method.invoke(controller);
                    }
                }
            }

            writer.print(gson.toJson(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setApplicationJson(response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setApplicationJson(response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setApplicationJson(response);
    }

    private void setApplicationJson(HttpServletResponse response) {
        response.setContentType(APPLICATION_JSON);
    }
}
