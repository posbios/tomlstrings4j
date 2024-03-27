package com.bitexception.toml.parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author yourmom
 */
public class Toml {

    public static <R> List<R> toClass(Map<String, Properties> toml, Class<R> clazz, String id) throws IOException, URISyntaxException {
        List<R> result = new ArrayList<>();

        for (Map.Entry<String, Properties> e : toml.entrySet()) {
            if (e.getKey().contains(id)) {
                result.add(toClass(clazz, e.getValue()));
            }
        }

        return result;
    }

    public static <R> R toClass(Class<R> clazz, Properties properties) throws IOException, URISyntaxException {
        R result = null;
        try {
//            Class<R> clazz = (Class<R>) Class.forName(classname);
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
            Logger.getLogger(Toml.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public static Map<String, Map<String, String>> toMap(Map<String, Properties> input) throws IOException, URISyntaxException {
        final Map<String, Map<String, String>> result = new HashMap<>();
        for (Map.Entry<String, Properties> ie : input.entrySet()) {
            Map<String, String> section = new HashMap<>();
            for (Map.Entry<Object, Object> pe : ie.getValue().entrySet()) {
                section.put(String.valueOf(pe.getKey()), String.valueOf(pe.getValue()));
            }
            result.put(ie.getKey(), section);
        }

        return result;
    }

    public static Map<String, Properties> parse(InputStream inputStream) throws IOException, URISyntaxException {
        final Map<String, Properties> toml = new HashMap<>();
        Pattern pattern = Pattern.compile("(?<key>.*)=(?<value>.*)");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            String currentSection = null;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().startsWith("#")) {
                    if (line.trim().startsWith("[")) {
                        // comienzo nueva seccion
                        currentSection = line.replace("[", "").replace("]", "");
                        toml.put(currentSection, new Properties());
                    }
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        String key = matcher.group("key").trim();
                        String value = matcher.group("value").trim();
                        if (value.startsWith("\"")) {
                            toml.getOrDefault(currentSection, new Properties()).put(key, noescape(wrappedtoString(value)));
                        } else if (value.startsWith("[")) {
                            toml.getOrDefault(currentSection, new Properties()).put(key, stringtoList(value));
                        }
                    }
                }
            }

            return toml;
        }
    }

    private static String noescape(String s) {
        return s.replace("\\\\", "\\")
                .replace("\'", "'")
                .replace("\\\"", "\"");
    }

    private static String wrappedtoString(String input) {
        String cadena = input.trim();
        return ((cadena.charAt(0) == '"' || cadena.charAt(0) == '\'') && (cadena.charAt(0) == '"' || cadena.charAt(0) == '\'')) ? cadena.substring(1, cadena.length() - 1) : cadena;
    }

    private static List<String> stringtoList(String cadena) {
        return Stream.of(cadena.substring(1, cadena.length() - 1).split(",")).map(Toml::wrappedtoString).collect(Collectors.toList());
    }

}
