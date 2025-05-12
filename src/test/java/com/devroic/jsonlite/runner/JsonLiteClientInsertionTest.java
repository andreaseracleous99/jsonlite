package com.devroic.jsonlite.runner;

import com.devroic.jsonlite.base.JsonLiteClientBaseTest;
import com.devroic.jsonlite.exceptions.JsonLiteClientOperationException;
import com.devroic.jsonlite.model.Person;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JsonLiteClientInsertionTest extends JsonLiteClientBaseTest {

    @Test
    @Order(1)
    void testInsert() {
        client.insert(john);
        var people = client.selectAll();
        assertNotNull(people, "People list should not be null");
        Assertions.assertEquals(1, people.size(), "Insert should return one person");
    }

    @Test
    @Order(2)
    void testInsertMultiple() {
        List<Person> people = Arrays.asList(mark, alice);
        client.insertMultiple(people);
        people = client.selectAll();
        assertNotNull(people, "People list should not be null");
        assertEquals(3, people.size(), "Insert should return three people");
    }

    @Test
    @Order(3)
    void testInsertInvalidObject() {
        Person person = new Person();
        person.setName("InvalidPerson");
        Assertions.assertThrows(JsonLiteClientOperationException.class, () -> client.insert(person),
                "Should throw exception if inserted object is missing required fields (like ID)");
    }

    @Test
    @Order(4)
    void testInsertDuplicateId() {
        Person person1 = new Person();
        person1.setId("1");
        person1.setName("John");
        assertThrows(JsonLiteClientOperationException.class, () -> client.insert(person1),
                "Should throw exception if a duplicate ID is found during insertion");
    }
}
