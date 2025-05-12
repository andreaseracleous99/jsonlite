/*
 * Copyright (C) 2025  Andreas Eracleous
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.devroic.jsonlite;

import com.devroic.jsonlite.client.JsonLiteClientBuilder;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * The main interface for the JSON Lite client operations.
 * This interface defines the CRUD operations (Create, Read, Update, Delete)
 * that can be performed on a JSON file.
 */
public interface JsonLiteClient {

    /**
     * Creates a new instance of the JsonLiteClientBuilder for building a JsonLiteClient.
     *
     * @return a new instance of JsonLiteClientBuilder.
     */
    static JsonLiteClientBuilder builder() {
        return new JsonLiteClientBuilder();
    }

    /**
     * Selects all the objects from the JSON file.
     *
     * @param <T> the type of object to be retrieved.
     * @return a list of all objects in the JSON file.
     */
    <T> List<T> selectAll();

    /**
     * Selects values associated with a specific key in the JSON file.
     *
     * @param key the key whose associated values are to be selected.
     * @return a list of lists of strings corresponding to the selected key.
     */
    List<List<String>> selectKey(String key);

    /**
     * Selects values associated with multiple keys in the JSON file.
     *
     * @param keys the keys whose associated values are to be selected.
     * @return a list of lists of strings corresponding to the selected keys.
     */
    List<List<String>> selectKeys(String... keys);

    /**
     * Selects an object from the JSON file by its ID.
     *
     * @param <T> the type of object to be retrieved.
     * @param id  the ID of the object to be selected.
     * @return the object with the specified ID.
     */
    <T> T selectById(String id);

    /**
     * Selects objects from the JSON file that match a specific key-value pair.
     *
     * @param <T>   the type of object to be retrieved.
     * @param key   the key to search for.
     * @param value the value that the key should match.
     * @return a list of objects that match the key-value pair.
     */
    <T> List<T> selectByKey(String key, String value);

    /**
     * Selects objects from the JSON file that match a specific key-value pair.
     *
     * @param <T>       the type of object to be retrieved.
     * @param condition the condition to match the objects.
     * @return a list of objects that match the condition.
     */
    <T> List<T> selectWhere(Predicate<T> condition);

    /**
     * Inserts a new object into the JSON file.
     *
     * @param <T>    the type of object to be inserted.
     * @param object the object to be inserted.
     */
    <T> void insert(T object);

    /**
     * Inserts multiple objects into the JSON file.
     *
     * @param <T>     the type of objects to be inserted.
     * @param objects the list of objects to be inserted.
     */
    <T> void insertMultiple(List<T> objects);

    /**
     * Updates the value of a specific key for all objects in the JSON file.
     *
     * @param key      the key whose value needs to be updated.
     * @param newValue the new value to set for the specified key.
     * @return true if the update was successful, false otherwise.
     */
    boolean updateKey(String key, Object newValue);

    /**
     * Updates an object/item in the JSON file by its ID.
     *
     * @param <T>           the type of object to be updated.
     * @param id            the ID of the object to be updated.
     * @param updatedObject the new object with updated values.
     * @return true if the object was updated, false otherwise.
     */
    <T> boolean updateById(String id, T updatedObject);

    /**
     * Updates the value of a specific key for all objects that match the given condition.
     *
     * @param <T>       the type of objects to be updated.
     * @param condition the condition used to filter the objects.
     * @param key       the key whose value should be updated.
     * @param newValue  the new value to assign to the specified key.
     * @return true if at least one object was updated, false otherwise.
     */
    <T> boolean updateWhere(Predicate<T> condition, String key, Object newValue);

    /**
     * Updates multiple key-value pairs for all objects that match the given condition.
     *
     * @param <T>       the type of objects to be updated.
     * @param condition the condition used to filter the objects.
     * @param updates   a map containing key-value pairs to update.
     * @return true if at least one object was updated, false otherwise.
     */
    <T> boolean updateWhere(Predicate<T> condition, Map<String, Object> updates);

    /**
     * Deletes all objects from the JSON file.
     *
     * @return true if all objects were deleted, false otherwise.
     */
    boolean deleteAll();

    /**
     * Deletes an object from the JSON file by its ID.
     *
     * @param id the ID of the object to be deleted.
     * @return true if an object was deleted, false otherwise.
     */
    boolean deleteById(String id);

    /**
     * Deletes objects from the JSON file that match a specific key-value pair.
     *
     * @param key   the key to search for.
     * @param value the value that the key should match.
     * @return true if objects were deleted, false otherwise.
     */
    boolean deleteByKey(String key, String value);

    /**
     * Deletes objects from the JSON file that match a given condition.
     *
     * @param <T>       the type of object to be deleted.
     * @param condition the condition to match the objects.
     * @return true if any objects were deleted, false otherwise.
     */
    <T> boolean deleteWhere(Predicate<T> condition);

}
