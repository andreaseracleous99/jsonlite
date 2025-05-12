package com.devroic.jsonlite.runner;

import com.devroic.jsonlite.base.JsonLiteClientBaseTest;
import com.devroic.jsonlite.JsonLiteClient;
import com.devroic.jsonlite.exceptions.JsonLiteClientOperationException;
import com.devroic.jsonlite.model.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonLiteClientExceptionTest extends JsonLiteClientBaseTest {

    @Test
    void testJsonFileNotExist() {
        var invalidClient = JsonLiteClient.builder()
                .jsonFilePath("invalid_file.json")
                .type(Person.class)
                .idKey("id");
        assertThrows(JsonLiteClientOperationException.class, invalidClient::build, "Should throw exception if a JSON file does not exist.");
    }
}
