package com.bitexception.toml.map;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * .
 * These methods already use unsafe reflection and are not suitable for production environments, secure environments, or large applications.
 * However, they can be useful in other cases and here they are. You can solve all cases efficiently using the Toml.parse method, lambda parallelism and the maps created in your application.
 * Remember replace placeholders like StringBean and method names with the actual names used by your library and toml file.
 * 
 * @author yourmom
 */
public class ToClass {

    /**
     * .
     * It need a class with public fields and same names than toml keywords of sections
     * 
     * @param <R> the expected return class with public fields and same names than toml keywords of sections
     * @param toml retrived toml data
     * @param clazz the class type you want to use
     * @param filter the filter to use on toml file sections ([filter-this]). it will try all finds.
     * @return the reflected class with injected data
     * @throws IOException if input file bad
     * @throws URISyntaxException if input file bad
     */
    public static <R> List<R> map(Map<String, Properties> toml, Class<R> clazz, String filter) throws IOException, URISyntaxException {
        List<R> result = new ArrayList<>();

        for (Map.Entry<String, Properties> e : toml.entrySet()) {
            if (e.getKey().contains(filter)) {
                result.add(map(clazz, e.getValue()));
            }
        }

        return result;
    }

    /**
     * .
     * It need a class with public fields and same names than toml keywords of sections
     * 
     * @param <R> the expected return class with public fields and same names than toml keywords of sections
     * @param clazz the class type you want to use
     * @param properties the sectin you want to convert into a class
     * @return the reflected class with injected data
     * @throws IOException if input file bad
     * @throws URISyntaxException if input file bad
     */
    public static <R> R map(Class<R> clazz, Properties properties) throws IOException, URISyntaxException {
        R result = null;
        try {
            result = clazz.getConstructor().newInstance();

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();

                if (properties.containsKey(fieldName)) {
                    Class<?> fieldType = field.getType();

                    if (fieldType == String.class) {
                        String value = properties.getProperty(fieldName);
                        field.setAccessible(true);
                        field.set(result, value);
                    } else if (fieldType == List.class) {
                        List<String> values = new ArrayList<>();
                        List<String> pValues = (List<String>) properties.get(fieldName);
                        for (String pv : pValues) {
                            values.add(pv);
                        }
                        field.setAccessible(true);
                        field.set(result, values);
                    }
                }
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(ToClass.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }
}
