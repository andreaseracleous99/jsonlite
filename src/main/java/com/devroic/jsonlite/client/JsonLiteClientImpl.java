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

package com.devroic.jsonlite.client;

import com.devroic.jsonlite.JsonLiteClient;
import com.devroic.jsonlite.messages.InfoMessages;
import com.devroic.jsonlite.operations.impl.DeleteHandlerImpl;
import com.devroic.jsonlite.operations.impl.InsertHandlerImpl;
import com.devroic.jsonlite.operations.impl.SelectHandlerImpl;
import com.devroic.jsonlite.operations.impl.UpdateHandlerImpl;
import com.devroic.jsonlite.operations.interfaces.DeleteHandler;
import com.devroic.jsonlite.operations.interfaces.SelectHandler;
import com.devroic.jsonlite.operations.interfaces.UpdateHandler;
import com.devroic.jsonlite.utils.JsonLiteClientOperationsHelper;
import com.devroic.jsonlite.utils.JsonLiteClientValidator;
import com.devroic.jsonlite.operations.interfaces.InsertHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class JsonLiteClientImpl implements JsonLiteClient {

    private static final Logger logger = LoggerFactory.getLogger(JsonLiteClientImpl.class);
    private final SelectHandler selectHandler;
    private final InsertHandler insertHandler;
    private final UpdateHandler updateHandler;
    private final DeleteHandler deleteHandler;

    // Constructor initializes the client, and validates or creates the JSON file if needed.
    public JsonLiteClientImpl(String jsonFilePath, Class<?> type, String idKey, boolean createFileIfNotExists) {
        File jsonFile = new File(jsonFilePath);

        if (createFileIfNotExists && !jsonFile.exists()) {
            JsonLiteClientOperationsHelper.createJsonFile(jsonFile);  // Create the file if it doesn't exist
        }
        JsonLiteClientValidator.validateJsonFileExistence(jsonFile);  // Validate file existence

        //Initialize operations handlers
        this.selectHandler = new SelectHandlerImpl(jsonFile, type, idKey);
        this.insertHandler = new InsertHandlerImpl(jsonFile, type, idKey);
        this.updateHandler = new UpdateHandlerImpl(jsonFile, type, idKey);
        this.deleteHandler = new DeleteHandlerImpl(jsonFile, type, idKey);

        logger.info(InfoMessages.BUILDING_CLIENT_COMPLETED);
    }

    // ** Select Operations **

    // Selects all objects from the JSON file.
    @Override
    public <T> List<T> selectAll() {
        return this.selectHandler.handleSelectAll();
    }

    // Selects all values associated with a specific key across all objects in the JSON file.
    @Override
    public List<List<String>> selectKey(String key) {
        return this.selectHandler.handleSelectKey(key);
    }

    // Selects multiple keys and their corresponding values across all objects in the JSON file.
    @Override
    public List<List<String>> selectKeys(String... keys) {
        return this.selectHandler.handleSelectKeys(keys);
    }

    // Selects a specific object by its unique ID.
    public <T> T selectById(String id) {
        return this.selectHandler.handleSelectById(id);
    }

    // Selects objects where the given key matches the provided value.
    @Override
    public <T> List<T> selectByKey(String key, String value) {
        return this.selectHandler.handleSelectByKey(key, value);
    }

    // Selects objects where a given condition is met.
    @Override
    public <T> List<T> selectWhere(Predicate<T> condition) {
        return this.selectHandler.handleSelectWhere(condition);
    }

    // ** Insert Operations **

    // Inserts a new object into the JSON file.
    @Override
    public <T> void insert(T object) {
        this.insertHandler.handleInsert(object);
    }

    // Inserts multiple objects into the JSON file.
    @Override
    public <T> void insertMultiple(List<T> objects) {
        this.insertHandler.handleInsertMultiple(objects);
    }

    // ** Update Operations **

    // Updates a key's value in all objects where the key is found.
    @Override
    public boolean updateKey(String key, Object newValue) {
        return this.updateHandler.handleUpdateKey(key, newValue);
    }

    // Updates an object by its unique ID.
    @Override
    public <T> boolean updateById(String id, T updatedObject) {
        return this.updateHandler.handleUpdateById(id, updatedObject);
    }

    // Updates objects that match the given condition by modifying the value of a specific key.
    @Override
    public <T> boolean updateWhere(Predicate<T> condition, String key, Object newValue) {
        return this.updateHandler.handleUpdateWhere(condition, key, newValue);
    }

    // Updates objects that match the given condition by modifying multiple key-value pairs.
    @Override
    public <T> boolean updateWhere(Predicate<T> condition, Map<String, Object> updates) {
        return this.updateHandler.handleUpdateWhere(condition, updates);
    }

    // ** Delete Operations **

    // Deletes all objects from the JSON file.
    public boolean deleteAll() {
        return this.deleteHandler.handleDeleteAll();
    }

    // Deletes an object by its unique ID.
    @Override
    public boolean deleteById(String id) {
        return this.deleteHandler.handleDeleteById(id);
    }

    // Delete objects where a given condition is met, modifying the value of a specific key.
    @Override
    public boolean deleteByKey(String key, String value) {
        return this.deleteHandler.handleDeleteByKey(key, value);
    }

    // Delete objects where a given condition is met.
    @Override
    public <T> boolean deleteWhere(Predicate<T> condition) {
        return this.deleteHandler.handleDeleteWhere(condition);
    }

}
