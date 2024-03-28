package com.bitexception.toml.map;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author yourmom
 */
public class ToMap {

    public static Map<String, Map<String, String>> map(Map<String, Properties> input) throws IOException, URISyntaxException {
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

}
