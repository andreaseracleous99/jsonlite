# JsonLite Library

**JsonLite** is a lightweight Java library designed to simplify the process of working with JSON files in Java applications. It provides easy-to-use operations for manipulating JSON objects in JSON files, including creating, reading, updating, and deleting records. This library integrates with popular JSON processing tools like Jackson, making it easy to integrate into your Java projects.

The JsonLite client provides a simple API for working with JSON files, allowing users to serialize objects to JSON, deserialize JSON to objects, and validate and manipulate JSON data.

You can find JavaDocs here: https://www.devroic.com/jsonlite/javadocs
---

# Table of Contents

1. [JsonLite Library](#jsonlite-library)
2. [Features](#features)
3. [Prerequisites](#prerequisites)
4. [Getting Started](#getting-started)
5. [Usage](#usage)
    - [Model Class](#model-class)
    - [Configurations](#configurations)
    - [Operations](#operations)
        - [Select Operations](#select-operations)
        - [Insert Operations](#insert-operations)
        - [Update Operations](#update-operations)
        - [Delete Operations](#delete-operations)
6. [License](#license)


---
## Features
- Create and manage JSON files.
- Serialize and deserialize objects to/from JSON.
- Support for performing CRUD-like operations (e.g., reading, saving, and querying data) on JSON data inside JSON files.
- Reflective operations for working with Java objects and their fields.

---

## Prerequisites

**Java Development Kit (JDK)**: requires **JDK 22** or later to compile and run. If you haven't installed JDK 22 or
later yet, you can download it from the [official Oracle website](https://www.oracle.com/java/technologies/downloads/)
or use a package manager like [Homebrew](https://brew.sh/) for macOS or [SDKMAN!](https://sdkman.io/) for Unix-based
systems.

---

## Getting Started

**Add Dependency**: Begin by adding the JsonLite Library as a dependency in your project. Find the latest version
on [Maven Central](https://central.sonatype.com/artifact/com.devroic/jsonlite/versions)

#### Maven

```xml

<dependency>
    <groupId>com.devroic</groupId>
    <artifactId>jsonlite</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### Gradle

```groovy
implementation group: 'com.devroic', name: 'jsonlite', version: '1.0.0'
```

---

## Usage

### Model Class
In order to use JsonLite you should have a model class that will map the data in the JSON file. In the model class is mandatory to have getters and setters for the fields.

You can see the Person class below as an example.

```java
public class Person {

    private String id;
    private String name;
    private String city;
    private List<String> cars;
    private List<String> brands;
    private String job;

    // Empty Constructor to be used from the Jackson ObjectMapper
    public Person() {}

    // Args Constructor
    public Person(String id, String name, String city, List<String> cars, List<String> brands, String job) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.cars = cars;
        this.brands = brands;
        this.job = job;
    }
    
    //Getters and Setters must be defined as well
```

### Configurations

Then you must create a JsonLiteClient, as below: 

```java
JsonLiteClient client = JsonLiteClient.builder()
        // Specifies the file path where the JSON file is located or will be created.
        .jsonFilePath("../test.json")
        // Sets the type of the objects that will be stored or retrieved from the JSON file (e.g., Person.class).
        .type(Person.class)
        // Specifies the key used for identifying unique objects in the JSON file (e.g., "id").
        .idKey("id")
        // If true, the JSON file will be created if it doesn't exist yet.
        .createFileIfNotExists(true)
        // Builds and returns the configured JsonLiteClient instance.
        .build();
```

### Operations
You can start calling operations using your JsonLiteClient. <Br>
You can see an example for each operation bellow:
### Select Operations

**selectAll()**
```java
// Retrieves all objects of the specified type from the JSON file.
List<Person> people = client.selectAll();
```

**selectKey()**
```java
// Retrieves a specific key from all objects in the JSON file.
var keyResults = client.selectKey("id");
```

**selectKeys()**
```java
// Retrieves multiple keys from all objects in the JSON file.
var keysResults = client.selectKeys("id", "name");
```

**selectById()**
```java
// Retrieves an object from the JSON file by its unique ID.
Person person = client.selectById("1");
```

**selectByKey()**
```java
// Retrieves a list of objects from the JSON file that match a specific key-value pair.
List<Person> people = client.selectByKey("name", "Test");
```

**selectWhere()**
```java
// Retrieves a list of objects from the JSON file where a condition is met (based on a lambda condition).
List<Person> people = client.selectWhere(
                        object -> ((Person) object).getName().equals("Test") // Condition: Select object where name is "Test"
);
```

### Insert Operations

**insert()**
```java
// Inserts a new object into the JSON file. Returns true if insertion is successful.
Person person = new Person();
boolean insertResult = client.insert(person);
```

**insertMultiple()**
```java
// Inserts multiple objects into the JSON file. Returns true if all insertions are successful.
Person person1 = new Person();
Person person2 = new Person();
List<Person> people = Arrays.asList(person1, person2);
boolean insertMultipleResult = client.insertMultiple(people);
```

### Update Operations

**updateKey()**
```java
// Updates the value of a specific key for all objects in the JSON file. Returns true if successful.
boolean updateResult = client.updateKey("name", "Test");
```

**updateById()**
```java
// Updates an object in the JSON file based on its unique ID. Returns true if successful.
Person person = new Person();
person.setId("1");
boolean updateResult = client.updateById("1", person);
```

**updateWhere()**
```java
// Updates the objects in the JSON file where a condition is met (based on a lambda condition), setting input value on the input key. Returns true if successful.
boolean updateResultSingle = client.updateWhere(
                                object -> ((Person) object).getId().equals("1"), // Condition: Select object where ID is 1
                                "name", // The key to be updated
                                "Test" // The new value for the key
                              );

Map<String, Object> updates = new HashMap<>();
updates.put("name", "UpdatedName");
updates.put("city", "UpdatedCity");
        
// Updates the objects in the JSON file where a condition is met (based on a lambda condition), setting input values on the input keys. Returns true if successful. 
boolean updateResultMultiple = client.updateWhere(
                                 object -> ((Person) object).getName().equals("Test"), updates) // Condition: Select object where name is "Test"
                               );
```

### Delete Operations

**deleteAll()**
```java
// Deletes all objects from the JSON file. Returns true if successful.
boolean deleteResult = client.deleteAll();
```

**deleteById()**
```java
// Deletes an object from the JSON file by its unique ID. Returns true if successful.
boolean deleteResult = client.deleteById("1");
```

**deleteByKey()**
```java
// Deletes an object from the JSON file by a specific key-value pair. Returns true if successful.
boolean deleteResult = client.deleteByKey("name", "Test");
```

**deleteWhere()**
```java
// Deletes the objects in the JSON file where a condition is met (based on a lambda condition). Returns true if successful.
boolean deleteResult = client.deleteWhere(
                            object -> ((Person) object).getName().equals("Test") // Condition: Select object where name is "Test"
                        );
```
---

## License

This project is licensed under the terms of the **GNU General Public License v3.0 (GPL-3.0)**.

See the [GNU General Public License v3.0](https://www.gnu.org/licenses/gpl-3.0.en.html) for more details.
