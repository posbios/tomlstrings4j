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
 * Use the parse method to get toml file data
 * 
 * @author yourmom
 */
public class Toml {

    /**
     * Toml parse each .toml file section into a java Properties instance.
     * 
     * @param inputStream to toml file
     * @return each .toml file section into a java Properties instance.
     * @throws IOException if input file bad
     * @throws URISyntaxException if input file bad
     */
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
                        if (value.startsWith("[")) {
                            toml.getOrDefault(currentSection, new Properties()).put(key, csvtoStringlist(value));
                        } else {
                            toml.getOrDefault(currentSection, new Properties()).put(key, noescape(cleanTomlStringfields(value)));
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

    private static String cleanTomlStringfields(String input) {
        String s = input.trim();
        return ((s.startsWith("\"") && s.endsWith("\"")) || (s.startsWith("\'") && s.endsWith("\'"))) ? removeFirstandLastchar(s) : s;
    }

    private static String removeFirstandLastchar(String input) {
        String s = input.trim();
        return s.substring(1, s.length() - 1);
    }

    private static List<String> csvtoStringlist(String s) {
        return Stream.of(Toml.removeFirstandLastchar(s).split(",")).map(Toml::cleanTomlStringfields).collect(Collectors.toList());
    }

}
