package com.bitexception.toml.parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author yourmom
 */
public class Toml {

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
                .replace("\\\"", "\"")
                .replace("\\t", "\t")
                .replace("\\b", "\b")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\f", "\f");
    }

    private static String wrappedtoString(String input) {
        String cadena = input.trim();
        return ((cadena.charAt(0) == '"' || cadena.charAt(0) == '\'') && (cadena.charAt(0) == '"' || cadena.charAt(0) == '\'')) ? cadena.substring(1, cadena.length() - 1) : cadena;
    }

    private static List<String> stringtoList(String cadena) {
        return Stream.of(cadena.substring(1, cadena.length() - 1).split(",")).map(Toml::wrappedtoString).collect(Collectors.toList());
    }

}
