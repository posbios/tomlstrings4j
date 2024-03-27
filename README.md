# tomlstrings4j

A lightweight and simple Java library with limited functionality for transforming TOML files into useful data within a Java application.

## Features

Parse TOML files into a Java object model
Convert TOML data to Java String or List<String> types

## Benefits

Easy to use
Lightweight and efficient

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
Map<String, Map<String,String>> tomlMap = Toml.toMap(toml);
```

This assumes a method called toMap exists in the Toml class.
Toml.toMap(Toml.parse(inputStream)) takes the parsed data (t from the previous step) and converts it to a simpler structure: a Map<String, Map<String, String>>. This new map has string keys representing top-level elements in the TOML file. The values are now maps with string keys and string values, likely representing nested key-value pairs in the TOML data.

3. Converting a specific TOML ID to a Class:

```Java
Map<String, Properties> toml = Toml.parse(inputStream);
StringBean stringBean = Toml.toClass(StringBean.class, toml.get("string.1"));
```

This example uses a hypothetical toClass method.
Toml.toClass(StringBean.class, Toml.parse(inputStream).get("string.1")) attempts to convert a specific section of the parsed data (Toml.parse(inputStream).get("string.1")) into an instance of the StringBean class.
Here, "string.1" likely refers to a key in the TOML data that points to a section containing properties that can be mapped to the fields of a StringBean object. This functionality depends on the library supporting automatic conversion between TOML data and Java classes.

4. Converting all TOML groups with a "string" to a list of Classes:

```Java
Map<String, Properties> toml = Toml.parse(inputStream);
List<StringBean> stringBeans = Toml.toClass(StringBean.class, toml, "string.");
```

Similar to the previous case, this uses a potential toClass method.
Toml.toClass(StringBean.class, Toml.parse(inputStream).get("string.")) tries to convert all entries under the key prefix "string." in the TOML data. It likely retrieves a list of Properties objects for each entry under that prefix.
The code then attempts to convert each Properties object to a StringBean instance, creating a final list of StringBean objects. This assumes the StringBean class can be constructed from the data stored under each "string." key.
Remember:

Replace placeholders like StringBean and method names with the actual names used by your library. Consult the library documentation for details on available methods and data structures used to represent TOML data.


## Keywords

TOML, Java, library, parser, converter, data, configuration

