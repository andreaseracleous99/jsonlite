package com.devroic.jsonlite.runner;

import com.devroic.jsonlite.base.JsonLiteClientBaseTest;
import com.devroic.jsonlite.model.Person;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JsonLiteClientUpdateTest extends JsonLiteClientBaseTest {

    @BeforeAll
    static void populateData() {
        client.insertMultiple(Arrays.asList(john, mark, alice));
    }

    @Test
    @Order(1)
    void testUpdateById() {
        Person updateByIdPerson = new Person("1", "UpdatedName", "New York", List.of("Tesla"), List.of("Nike"), "Engineer");
        assertTrue(client.updateById("1", updateByIdPerson), "Update By Id should return true.");
        Person updatedPerson = client.selectById("1");
        assertNotNull(updatedPerson, "Person should not be null");
        assertEquals("UpdatedName", updatedPerson.getName(), "Person's name should be 'UpdatedName'.");
    }

    @Test
    @Order(2)
    void testUpdateKey() {
        assertTrue(client.updateKey("name", "UpdatedName"), "Update key should return true.");
        Person updatedPerson = client.selectById("1");
        assertEquals("UpdatedName", updatedPerson.getName(), "Person's name should be 'UpdatedName'");
    }

    @Test
    @Order(3)
    void testUpdateWhereSingle() {
        assertTrue(client.updateWhere(object -> ((Person) object).getCity().equals("New York"), "city", "California"), "Update where should return true.");
        assertEquals(2, client.selectByKey("city", "California").size(), "People list size should be 2.");
    }

    @Test
    @Order(4)
    void testUpdateWhereMultiple() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", "George");
        updates.put("city", "Texas");
        assertTrue(client.updateWhere(obj -> ((Person) obj).getId().equals("2"), updates), "Update where should return true.");
        Person person = client.selectById("2");
        assertEquals("George", person.getName(), "Person's name should be 'George'");
        assertEquals("Texas", person.getCity(), "Person's city should be 'Texas'");
    }
}
