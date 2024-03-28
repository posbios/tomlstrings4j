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
 *
 * @author yourmom
 */
public class ToClass {

    public static <R> List<R> map(Map<String, Properties> toml, Class<R> clazz, String id) throws IOException, URISyntaxException {
        List<R> result = new ArrayList<>();

        for (Map.Entry<String, Properties> e : toml.entrySet()) {
            if (e.getKey().contains(id)) {
                result.add(map(clazz, e.getValue()));
            }
        }

        return result;
    }

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
