package com.devroic.jsonlite.base;

import com.devroic.jsonlite.JsonLiteClient;
import com.devroic.jsonlite.model.Person;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public abstract class JsonLiteClientBaseTest {
    protected static JsonLiteClient client;
    protected static Person john, mark, alice;
    private static final String JSON_FILE_PATH = "src/test/java/com/devroic/jsonlite/resources/junits.json";

    @BeforeAll
    static void setUp() {
        client = JsonLiteClient.builder()
                .jsonFilePath(JSON_FILE_PATH)
                .type(Person.class)
                .idKey("id")
                .createFileIfNotExists(true)
                .build();

        john = new Person("1", "John", "New York", Arrays.asList("Bmw", "Audi"), Arrays.asList("Adidas", "Nike"), "Software Engineer");
        mark = new Person("2", "Mark", "San Francisco", Arrays.asList("Mercedes", "Nissan"), Arrays.asList("Reebok", "Puma"), "Data Scientist");
        alice = new Person("3", "Alice", "New York", Arrays.asList("Tesla", "Ford"), Arrays.asList("Apple", "Google"), "Product Manager");
    }

    @AfterAll
    static void cleanUp() {
        try {
            Files.deleteIfExists(Paths.get(JSON_FILE_PATH));
        } catch (Exception e) {
            System.err.println("Failed to delete test JSON file: " + e.getMessage());
        }
    }
}
