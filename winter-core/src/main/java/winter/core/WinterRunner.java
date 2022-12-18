package winter.core;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import winter.core.annotation.Controller;
import winter.core.annotation.WinterApplication;

public class WinterRunner {

    public static void run(Class<?> clazz) {
        // scan bean
        List<String> scanPackageNames = Arrays
                .asList(clazz.getDeclaredAnnotation(WinterApplication.class).scanBasePackages());
        WinterContext.scan(scanPackageNames);

        // run server web
        runWinterWeb();
    }

    public static void runWinterWeb() {

        // get controllers
        Map<Class<?>, Object> beans = WinterContext.getBeans();
        List<Object> controllers = beans.keySet().stream()
                .filter(ele -> ele.isAnnotationPresent(Controller.class)).map(beans::get).toList();

        try {
            Class<?> winterWebClass = Class.forName("winter.web.WinterWeb");
            Object winterWeb =
                    winterWebClass.getDeclaredConstructor(List.class).newInstance(controllers);
            Method runWeb = winterWebClass.getDeclaredMethod("run");
            runWeb.invoke(winterWeb);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
