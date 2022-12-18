package winter.web.model;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import winter.web.annotation.GetMapping;

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

        String path = request.getPathInfo();
        try {
            Object result = null;
            Method[] methods = controller.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(GetMapping.class)) {
                    String urlPattern = method.getDeclaredAnnotation(GetMapping.class).value();

                    if (!isMatchUrl(urlPattern, path)) {
                        continue;
                    }

                    List<PathMapper> pathMappers = getPathMapper(urlPattern, path);
                    System.out.println("pathMappers: " + pathMappers);
                    result = method.invoke(controller);
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

    private boolean isMatchUrl(String urlPattern, String url) {
        return urlPattern.split("/").length == url.split("/").length;
    }

    private List<PathMapper> getPathMapper(String url, String path) {

        String urlPattern = url.replaceAll("\\{[^//]+\\}", "(.+)");
        System.out.println("urlPattern: " + urlPattern);

        List<String> urlSplit = Arrays.asList(url.split("/"));
        System.out.println("urlSplit: " + urlSplit);

        List<PathMapper> pathMappers =
                urlSplit.stream().filter(ele -> ele.matches("\\{.+\\}")).map(ele -> {
                    String key = ele.replace("{", "");
                    return new PathMapper(key, null);
                }).toList();

        // Create a Pattern object
        Pattern r = Pattern.compile(urlPattern);

        // Now create matcher object.
        Matcher m = r.matcher(path);

        if (m.find()) {
            for (int i = 1; i <= pathMappers.size(); i++) {
                pathMappers.get(i - 1).setValue(m.group(i));
            }
        }

        return pathMappers;
    }
}
