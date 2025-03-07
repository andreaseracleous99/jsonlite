package com.techbasecore.jsonlite.runner;

import com.techbasecore.jsonlite.JsonLiteClient;
import com.techbasecore.jsonlite.base.JsonLiteClientBaseTest;
import com.techbasecore.jsonlite.exceptions.JsonLiteClientOperationException;
import com.techbasecore.jsonlite.model.Person;
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
