package com.devroic.jsonlite.runner;

import com.devroic.jsonlite.base.JsonLiteClientBaseTest;
import com.devroic.jsonlite.model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonLiteClientDeletionTest extends JsonLiteClientBaseTest {

    @BeforeEach
    void populateData() {
        client.insertMultiple(Arrays.asList(john, mark, alice));
    }

    @AfterEach
    void cleanData() {
        client.deleteAll();
    }

    @Test
    void testDeleteById() {
        boolean deleteResult = client.deleteById("1");
        assertTrue(deleteResult, "Delete by ID should return true");
        Assertions.assertNull(client.selectById("1"), "Person should be deleted");
    }

    @Test
    void testDeleteByKey() {
        boolean deleteResult = client.deleteByKey("name", "John");
        assertTrue(deleteResult, "Delete by key should return true");
        Assertions.assertEquals(2, client.selectAll().size(), "Person should be deleted");
    }

    @Test
    void testDeleteAll() {
        boolean deleteResult = client.deleteAll();
        assertTrue(deleteResult, "Delete all should return true");
        Assertions.assertTrue(client.selectAll().isEmpty(), "People list should be empty");
    }

    @Test
    void testDeleteWhere() {
        boolean deleteResult = client.deleteWhere(object -> ((Person) object).getCity().equals("New York"));
        assertTrue(deleteResult, "Delete Where should return true");
        Assertions.assertEquals(1, client.selectAll().size(), "People list should be empty");
    }

}
