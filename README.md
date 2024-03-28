# tomlstrings4j

A modular lightweight and simple Java library with limited functionality for transforming TOML files into useful data within a Java application.

## Features

Parse TOML files into a Java object model
Convert TOML data to Java String or List<String> types

## Benefits

Easy to use
Lightweight and efficient
Modular

## Getting Started

To get started, add the following dependency to your project's pom.xml file:

```
<dependency>
    <groupId>com.bitexception.toml</groupId>
    <artifactId>tomlstrings4j</artifactId>
    <version>1.0.0</version>
</dependency>
```

And download this project to mvn install in your local maven.

## Use cases explained

Working with TOML data in your Java application

This code snippet showcases different ways to work with TOML data using the provided Toml library:

1. Parsing TOML to a Map of Properties:

```Java
Map<String, Properties> toml = Toml.parse(inputStream);
```

Toml.parse(inputStream): This line assumes the Toml class has a static method called parse that takes an InputStream as input. This method reads the TOML data from the stream and parses it into a Java object representing the data structure.
The return value, assigned to toml, is likely a Map<String, Properties>. This means the parsed data is organized as a map where keys are strings and values are instances of the Properties class.
Note: It's important to check the library documentation to see exactly how the parsed data is structured.

2. Converting TOML to a Map of Maps:

```Java
Map<String, Properties> toml = Toml.parse(inputStream);
Map<String, Map<String,String>> tomlMap = ToMap.map(toml);
```

ToMap.map(Toml.parse(inputStream)) takes the parsed data and converts it to a simpler structure: a Map<String, Map<String, String>>. This new map has string keys representing top-level elements in the TOML file. The values are now maps with string keys and string values, likely representing nested key-value pairs in the TOML data.

3[^1]. Converting a specific TOML ID to a Class:

```Java
Map<String, Properties> toml = Toml.parse(inputStream);
StringBean stringBean = ToClass.map(StringBean.class, toml.get("string.1"));
```

ToClass.toClass(StringBean.class, Toml.parse(inputStream).get("string.1")) attempts to convert a specific section of the parsed data (Properties.class) into an instance of the StringBean class.
Here, "string.1" likely refers to a key in the TOML data that points to a section containing properties that can be mapped to the fields of a StringBean object. 

4[^1]. Converting all TOML groups with a "string" to a list of Classes:

```Java
Map<String, Properties> toml = Toml.parse(inputStream);
List<StringBean> stringBeans = ToClass.map(StringBean.class, toml, "string.");
```

Similar to the previous case.
ToClass.map(StringBean.class, Toml.parse(inputStream).get("string.")) tries to convert all entries with the key containing "string." in the TOML data. It likely retrieves a list of Properties objects for each entry under that prefix.

The code then attempts to convert each Properties object to a StringBean instance, creating a final list of StringBean objects. This assumes the StringBean class can be constructed from the data stored under each "string." key.

## Keywords

TOML, Java, library, parser, converter, data, configuration

[1]: These methods already use unsafe reflection and are not suitable for production environments, secure environments, or large applications.

 However, they can be useful in other cases and here they are. You can solve all cases efficiently using the Toml.parse method, lambda parallelism and the maps created in your application.
 
 Remember replace placeholders like StringBean and method names with the actual names used by your library and toml file.
