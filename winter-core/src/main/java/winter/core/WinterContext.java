package winter.core;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import winter.core.annotation.Bean;
import winter.core.annotation.Component;
import winter.core.annotation.Configuration;
import winter.core.annotation.Controller;
import winter.core.annotation.Repository;
import winter.core.annotation.Service;

@Data
public class WinterContext {

    private static HashMap<Class<?>, Object> beans;

    private static List<Class<? extends Annotation>> beanAnnotations;

    static {
        beans = new HashMap<>();
        beanAnnotations = Arrays.asList(Component.class, Configuration.class, Controller.class,
                Service.class, Repository.class);
    }

    public static void scan(List<String> packageNames) {
        try {
            List<Class<?>> classes = getAllClasses(packageNames);

            for (Class<?> clazz : classes) {
                createComponent(clazz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<Class<?>, Object> getBeans() {
        return beans;
    }

    private static void createComponent(Class<?> clazz) {
        if (!isComponent(clazz)) {
            return;
        }

        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            beans.put(clazz, instance);

            if (clazz.isAnnotationPresent(Configuration.class)) {
                scanBeanConfiguration(clazz, instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isComponent(Class<?> clazz) {
        return beanAnnotations.stream().anyMatch(clazz::isAnnotationPresent);
    }

    private static void scanBeanConfiguration(Class<?> clazz, Object instance) {
        List<Method> methods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(ele -> ele.isAnnotationPresent(Bean.class)).toList();


        methods.forEach(ele -> {
            try {
                Class<?> returnType = ele.getReturnType();
                Object beanInstance = ele.invoke(instance);

                beans.put(returnType, beanInstance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static List<Class<?>> getAllClasses(List<String> packageNames)
            throws IOException, ClassNotFoundException {
        List<Class<?>> result = new ArrayList<>();

        for (String packageName : packageNames) {
            String packageUri = String.format("target/classes/%s", packageName.replace(".", "/"));
            Path packageFullPath = Paths.get(packageUri);
            List<Class<?>> classes = getAllPackageClasses(packageFullPath, packageName);

            result.addAll(classes);
        }

        return result;
    }

    private static List<Class<?>> getAllPackageClasses(Path packagePath, String packageName)
            throws IOException, ClassNotFoundException {

        if (!Files.exists(packagePath)) {
            return Collections.emptyList();
        }

        List<Path> files = Files.list(packagePath).filter(Files::isRegularFile).toList();

        List<Class<?>> classes = new ArrayList<>();

        for (Path filePath : files) {
            String fileName = filePath.getFileName().toString();

            if (fileName.endsWith(".class")) {
                String classFullName =
                        packageName.isBlank() ? fileName.replaceFirst("\\.class$", "")
                                : packageName + "." + fileName.replaceFirst("\\.class$", "");

                Class<?> clazz = Class.forName(classFullName);
                classes.add(clazz);
            }
        }
        return classes;
    }
}
