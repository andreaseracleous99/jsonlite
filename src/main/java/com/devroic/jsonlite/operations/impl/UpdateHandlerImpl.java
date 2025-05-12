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

package com.devroic.jsonlite.operations.impl;

import com.devroic.jsonlite.messages.ErrorMessages;
import com.devroic.jsonlite.messages.InfoMessages;
import com.devroic.jsonlite.messages.WarningMessages;
import com.devroic.jsonlite.operations.OperationsBaseHandler;
import com.devroic.jsonlite.operations.interfaces.SelectHandler;
import com.devroic.jsonlite.operations.interfaces.UpdateHandler;
import com.devroic.jsonlite.utils.JsonLiteClientOperationsHelper;
import com.devroic.jsonlite.utils.JsonLiteClientValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.devroic.jsonlite.exceptions.JsonLiteClientOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

public class UpdateHandlerImpl extends OperationsBaseHandler implements UpdateHandler {

    private static final Logger logger = LoggerFactory.getLogger(UpdateHandlerImpl.class);
    private final SelectHandler selectHandler;

    public UpdateHandlerImpl(File jsonFile, Class<?> type, String idKey) {
        super(jsonFile, type, idKey);
        this.selectHandler = new SelectHandlerImpl(this.jsonFile, this.type, this.idKey);
    }

    @Override
    public <T> boolean handleUpdateKey(String key, Object newValue) {
        try {
            JsonLiteClientValidator.validateJsonFileExistence(this.jsonFile);
            JsonLiteClientValidator.validateKey(this.type, key);

            List<T> objects = this.selectHandler.handleSelectAll();
            boolean updated = false;
            int updatedCount = 0;

            // Iterate through the objects and update the specific key
            for (T object : objects) {
                JsonNode jsonNode = objectMapper.valueToTree(object);
                JsonNode targetNode = jsonNode.get(key);

                // Check if the key exists and update the value
                if (targetNode != null) {
                    if (newValue instanceof List<?>) {
                        // If the new value is a list, convert it to a JSON array
                        ((ObjectNode) jsonNode).set(key, objectMapper.valueToTree(newValue));
                    } else {
                        // Otherwise, just update the key with the new value
                        ((ObjectNode) jsonNode).put(key, newValue.toString());
                    }
                    updated = true;
                    updatedCount++;

                    // Update the object in the list after modifying its JSON representation
                    T updatedObject = objectMapper.treeToValue(jsonNode, (Class<T>) object.getClass());
                    objects.set(objects.indexOf(object), updatedObject);
                }
            }

            // If any objects were updated, save the updated list to the file
            if (updated) {
                JsonLiteClientOperationsHelper.saveToFile(objectMapper, objects, jsonFile);
                logger.info(InfoMessages.OBJECTS_UPDATED_BY_KEY, updatedCount, key, newValue);
            }

            return updated;
        } catch (Exception e) {
            throw new JsonLiteClientOperationException(String.format(ErrorMessages.FAILED_UPDATE_BY_KEY, key), e);
        }
    }

    @Override
    public <T> boolean handleUpdateById(String id, T updatedObject) {
        try {
            JsonLiteClientValidator.validateJsonFileExistence(this.jsonFile);
            JsonLiteClientValidator.validateIdKeyExistence(this.idKey);
            JsonLiteClientValidator.validateClassMatch(this.type, updatedObject);

            List<T> objects = this.selectHandler.handleSelectAll();
            AtomicBoolean updated = new AtomicBoolean(false);

            List<T> updatedObjects = objects.stream().map(object -> {
                String objectId = JsonLiteClientOperationsHelper.getFieldValue(object, idKey);
                if (objectId.equals(id)) {
                    String updatedObjectId = JsonLiteClientOperationsHelper.getFieldValue(updatedObject, idKey);
                    if (!updatedObjectId.equals(id)) {
                        throw new JsonLiteClientOperationException(
                                String.format(ErrorMessages.ID_MISMATCH_UPDATE, id, updatedObjectId));
                    }
                    updated.set(true);
                    return updatedObject;
                }
                return object;
            }).toList();

            if (updated.get()) {
                JsonLiteClientOperationsHelper.saveToFile(objectMapper, updatedObjects, jsonFile);
                logger.info(InfoMessages.OBJECT_UPDATED_BY_ID, id);
            } else {
                logger.warn(WarningMessages.OBJECT_NOT_FOUND_BY_ID, id);
            }

            return updated.get();
        } catch (Exception e) {
            throw new JsonLiteClientOperationException(String.format(ErrorMessages.FAILED_UPDATE_BY_ID, id), e);
        }
    }

    @Override
    public <T> boolean handleUpdateWhere(Predicate<T> condition, String key, Object newValue) {
        try {
            JsonLiteClientValidator.validateJsonFileExistence(this.jsonFile);
            JsonLiteClientValidator.validateKey(this.type, key);

            List<T> objects = this.selectHandler.handleSelectAll();
            List<T> matchingObjects = objects.stream()
                    .filter(condition)
                    .toList();

            if (matchingObjects.isEmpty()) {
                logger.warn(WarningMessages.OBJECT_NOT_FOUND_BY_CONDITION);
                return false;
            }

            boolean updated = false;
            int updatedCount = 0;

            // Iterate only over matching objects instead of all objects
            for (T object : matchingObjects) {
                JsonNode jsonNode = objectMapper.valueToTree(object);
                JsonNode targetNode = jsonNode.get(key);

                // Check if the key exists and update the value
                if (targetNode != null) {
                    if (newValue instanceof List<?>) {
                        // If the new value is a list, convert it to a JSON array
                        ((ObjectNode) jsonNode).set(key, objectMapper.valueToTree(newValue));
                    } else {
                        // Otherwise, just update the key with the new value
                        ((ObjectNode) jsonNode).put(key, newValue.toString());
                    }
                    updated = true;
                    updatedCount++;

                    // Update the object in the list after modifying its JSON representation
                    T updatedObject = objectMapper.treeToValue(jsonNode, (Class<T>) object.getClass());
                    objects.set(objects.indexOf(object), updatedObject);
                }
            }

            // If any objects were updated, save the updated list to the file
            if (updated) {
                JsonLiteClientOperationsHelper.saveToFile(objectMapper, objects, jsonFile);
                logger.info(InfoMessages.OBJECTS_UPDATED_WHERE, updatedCount, key, newValue);
            }

            return updated;
        } catch (Exception e) {
            throw new JsonLiteClientOperationException(String.format(ErrorMessages.FAILED_UPDATE_WHERE, key), e);
        }
    }

    @Override
    public <T> boolean handleUpdateWhere(Predicate<T> condition, Map<String, Object> updates) {
        try {
            JsonLiteClientValidator.validateJsonFileExistence(this.jsonFile);

            List<T> objects = this.selectHandler.handleSelectAll();
            List<T> matchingObjects = objects.stream()
                    .filter(condition)
                    .toList();

            if (matchingObjects.isEmpty()) {
                logger.warn(WarningMessages.OBJECT_NOT_FOUND_BY_CONDITION);
                return false;
            }

            boolean updated = false;
            int updatedCount = 0;

            for (T object : matchingObjects) {
                JsonNode jsonNode = objectMapper.valueToTree(object);
                ObjectNode objectNode = (ObjectNode) jsonNode;

                for (var entry : updates.entrySet()) {
                    String key = entry.getKey();
                    JsonLiteClientValidator.validateKey(this.type, key);
                    Object newValue = entry.getValue();

                    if (jsonNode.has(key)) {
                        if (newValue instanceof List<?>) {
                            objectNode.set(key, objectMapper.valueToTree(newValue));
                        } else {
                            objectNode.put(key, newValue.toString());
                        }
                        updated = true;
                    }
                }

                if (updated) {
                    T updatedObject = objectMapper.treeToValue(jsonNode, (Class<T>) object.getClass());
                    objects.set(objects.indexOf(object), updatedObject);
                    updatedCount++;
                }
            }

            if (updated) {
                JsonLiteClientOperationsHelper.saveToFile(objectMapper, objects, jsonFile);
                logger.info(InfoMessages.OBJECTS_UPDATED_WHERE, updatedCount, updates);
            }

            return updated;
        } catch (Exception e) {
            throw new JsonLiteClientOperationException(ErrorMessages.FAILED_UPDATE_WHERE, e);
        }
    }


}
