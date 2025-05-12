package com.devroic.jsonlite.runner;

import com.devroic.jsonlite.base.JsonLiteClientBaseTest;
import com.devroic.jsonlite.model.Person;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JsonLiteClientSelectionTest extends JsonLiteClientBaseTest {

    @BeforeAll
    static void populateData() {
        client.insertMultiple(Arrays.asList(john, mark, alice));
    }

    @Test
    void testSelectById() {
        Person person = client.selectById("1");
        assertNotNull(person, "Person should not be null.");
        assertEquals("John", person.getName(), "Person's name should be 'John'.");
    }

    @Test
    void testSelectByKey() {
        var people = client.selectByKey("city", "New York");
        assertNotNull(people, "People list should not be null.");
        assertEquals(2, people.size(), "People list should be 2.");
    }

    @Test
    void testSelectKey() {
        var keysResults = client.selectKey("id");
        assertNotNull(keysResults, "People list should not be null.");
        assertEquals(3, keysResults.size(), "People list should be 3.");
    }

    @Test
    void testSelectKeys() {
        var keysResults = client.selectKeys("id", "name");
        assertNotNull(keysResults, "People list should not be null.");
        assertEquals(3, keysResults.size(), "People list should be 3.");
    }

    @Test
    void testSelectWhere() {
        List<Person> people = client.selectWhere(p -> ((Person) p).getCity().equals("New York"));
        assertNotNull(people, "People list should not be null.");
        assertEquals(2, people.size(), "People list should be 2.");
    }
}
