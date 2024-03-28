package com.bitexception.toml.parse;

import com.bitexception.toml.map.ToClass;
import com.bitexception.toml.map.ToMap;
import com.bitexception.toml.parse.bean.StringBean;
import java.io.InputStream;
import static java.lang.System.out;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author yourmom
 */
public class TomlTest {

    public TomlTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testParse() throws Exception {
        System.out.println("testParse");
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.toml");
        var t = Toml.parse(inputStream);
        out.println(t);

        assertTrue(true);
    }

    @Test
    public void testToMap() throws Exception {
        System.out.println("testToMap");
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.toml");
        var t = Toml.parse(inputStream);
        var m = ToMap.map(t);
        out.println(m);

        assertTrue(true);
    }

    @Test
    public void testToClass() throws Exception {
        System.out.println("testToClass");
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.toml");
        var t = Toml.parse(inputStream);
        out.println(ToClass.map(StringBean.class, t.get("string.1")));

        assertTrue(true);
    }

    @Test
    public void testToListClass() throws Exception {
        System.out.println("testToListClass");
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.toml");
        var t = Toml.parse(inputStream);
        out.println(ToClass.map(t, StringBean.class, "string."));

        assertTrue(true);
    }
}
