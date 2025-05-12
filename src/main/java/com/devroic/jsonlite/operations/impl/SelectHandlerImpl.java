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
import com.devroic.jsonlite.utils.JsonLiteClientOperationsHelper;
import com.devroic.jsonlite.utils.JsonLiteClientValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.devroic.jsonlite.exceptions.JsonLiteClientOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class SelectHandlerImpl extends OperationsBaseHandler implements SelectHandler {
    private static final Logger logger = LoggerFactory.getLogger(SelectHandlerImpl.class);

    public SelectHandlerImpl(File jsonFile, Class<?> type, String idKey) {
        super(jsonFile, type, idKey);
    }

    @Override
    public <T> List<T> handleSelectAll() {
        try {
            JsonLiteClientValidator.validateJsonFileExistence(this.jsonFile);
            if (jsonFile.length() == 0) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(jsonFile,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, this.type));
        } catch (IOException e) {
            throw new JsonLiteClientOperationException(ErrorMessages.FAILED_READING, e);
        }
    }

    @Override
    public List<List<String>> handleSelectKey(String key) {
        try {
            JsonLiteClientValidator.validateJsonFileExistence(this.jsonFile);
            JsonLiteClientValidator.validateKey(this.type, key);

            List<Object> allObjects = handleSelectAll();
            List<List<String>> values = new ArrayList<>();
            String lowerCaseKey = key.toLowerCase();

            for (Object obj : allObjects) {
                JsonNode jsonNode = objectMapper.valueToTree(obj);
                jsonNode.fieldNames().forEachRemaining(field -> {
                    if (field.toLowerCase().equals(lowerCaseKey)) {
                        JsonNode valueNode = jsonNode.get(field);
                        if (valueNode != null) {
                            List<String> valueList = new ArrayList<>();
                            if (valueNode.isArray()) {
                                for (JsonNode arrayElement : valueNode) {
                                    valueList.add(arrayElement.asText());
                                }
                                values.add(valueList);
                            } else {
                                if (!valueNode.asText().equals("null")) {
                                    valueList.add(valueNode.asText());
                                    values.add(valueList);
                                }
                            }
                        }
                    }
                });
            }
            return values;
        } catch (RuntimeException e) {
            throw new JsonLiteClientOperationException(String.format(ErrorMessages.FAILED_SELECT_KEY, key), e);
        }
    }

    @Override
    public List<List<String>> handleSelectKeys(String... keys) {
        try {
            JsonLiteClientValidator.validateJsonFileExistence(this.jsonFile);

            if (keys.length == 1) {
                return handleSelectKey(keys[0]);
            }

            List<List<String>> groupedValues = new ArrayList<>();
            List<Object> allObjects = handleSelectAll();

            for (Object obj : allObjects) {
                List<String> groupedValuesForObject = new ArrayList<>();

                for (String key : keys) {
                    JsonLiteClientValidator.validateKey(this.type, key);

                    String lowerCaseKey = key.toLowerCase();
                    JsonNode jsonNode = objectMapper.valueToTree(obj);
                    JsonNode valueNode = jsonNode.get(lowerCaseKey);

                    if (valueNode != null) {
                        if (valueNode.isArray()) {
                            List<String> valueList = new ArrayList<>();
                            for (JsonNode arrayElement : valueNode) {
                                valueList.add(arrayElement.asText());
                            }
                            groupedValuesForObject.add(valueList.toString());
                        } else {
                            if (!valueNode.asText().equals("null")) {
                                groupedValuesForObject.add(valueNode.asText());
                            }
                        }
                    }
                }
                groupedValues.add(groupedValuesForObject);
            }

            return groupedValues;
        } catch (Exception e) {
            throw new JsonLiteClientOperationException(
                    String.format(ErrorMessages.FAILED_SELECT_KEYS, String.join(", ", keys)), e);
        }
    }

    public <T> T handleSelectById(String id) {
        try {
            JsonLiteClientValidator.validateJsonFileExistence(this.jsonFile);
            JsonLiteClientValidator.validateIdKeyExistence(this.idKey);

            List<T> allObjects = handleSelectAll();
            T foundObject = allObjects.stream()
                    .filter(object -> JsonLiteClientOperationsHelper.getFieldValue(object, idKey).equals(id))
                    .findFirst()
                    .orElse(null);
            if (foundObject != null) {
                logger.info(InfoMessages.OBJECT_FOUND_BY_ID, id);
            } else {
                logger.warn(WarningMessages.OBJECT_NOT_FOUND_BY_ID, id);
            }
            return foundObject;
        } catch (RuntimeException e) {
            throw new JsonLiteClientOperationException(ErrorMessages.FAILED_SELECT_BY_ID, e);
        }
    }

    @Override
    public <T> List<T> handleSelectByKey(String key, String value) {
        try {
            JsonLiteClientValidator.validateJsonFileExistence(this.jsonFile);
            JsonLiteClientValidator.validateKey(this.type, key);

            List<T> objects = handleSelectAll();
            String lowerCaseKey = key.toLowerCase();

            List<T> matchedObjects = new ArrayList<>();
            for (T object : objects) {
                JsonNode jsonNode = objectMapper.valueToTree(object);
                JsonNode valueNode = jsonNode.get(lowerCaseKey);

                if (valueNode != null && valueNode.asText().equalsIgnoreCase(value)) {
                    matchedObjects.add(object);
                }
            }
            if (matchedObjects.isEmpty()) {
                logger.warn(WarningMessages.OBJECT_NOT_FOUND_BY_KEY, key, value);
            }
            return matchedObjects;
        } catch (RuntimeException e) {
            throw new JsonLiteClientOperationException(String.format(ErrorMessages.FAILED_SELECT_BY_KEY, key, value), e);
        }
    }

    @Override
    public <T> List<T> handleSelectWhere(Predicate<T> condition) {
        try {
            JsonLiteClientValidator.validateJsonFileExistence(this.jsonFile);
            List<T> objects = handleSelectAll();
            List<T> matchingObjects = objects.stream()
                    .filter(condition)
                    .toList();
            if (matchingObjects.isEmpty()) {
                logger.warn(WarningMessages.OBJECT_NOT_FOUND_BY_CONDITION);
            }
            return matchingObjects;
        } catch (Exception e) {
            throw new JsonLiteClientOperationException(ErrorMessages.FAILED_SELECT_WHERE, e);
        }
    }
}
