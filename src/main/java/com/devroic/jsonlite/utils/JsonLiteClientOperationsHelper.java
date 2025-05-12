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

package com.devroic.jsonlite.utils;

import com.devroic.jsonlite.messages.ErrorMessages;
import com.devroic.jsonlite.messages.InfoMessages;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.devroic.jsonlite.exceptions.JsonLiteClientBuilderException;
import com.devroic.jsonlite.exceptions.JsonLiteClientOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class JsonLiteClientOperationsHelper {

    // Logger to log information, errors, and other relevant messages
    private static final Logger logger = LoggerFactory.getLogger(JsonLiteClientOperationsHelper.class);

    // Private constructor to prevent instantiation of this utility class
    private JsonLiteClientOperationsHelper() {
    }

    // Method to create a JSON file if it doesn't exist, ensuring it's a valid JSON file
    public static void createJsonFile(File jsonFile) {
        try {
            // Check if the file name ends with .json
            if (!JsonLiteClientValidator.isJson(jsonFile.getName())) {
                throw new JsonLiteClientBuilderException(String.format(ErrorMessages.MUST_BE_JSON_FILE));
            }
            // Create the file if it doesn't exist
            if (jsonFile.createNewFile()) {
                logger.info(InfoMessages.CREATING_FILE, jsonFile.getPath()); // Log the file creation path
            } else {
                throw new JsonLiteClientBuilderException(String.format(ErrorMessages.FILE_CREATION_ERROR, jsonFile.getPath()));
            }
        } catch (IOException e) {
            // Catch any IO exceptions and throw a custom exception with the error message
            throw new JsonLiteClientBuilderException(String.format(ErrorMessages.FILE_CREATION_ERROR, jsonFile.getPath()), e);
        }
    }

    // Method to save a list of objects to a JSON file using ObjectMapper
    public static <T> void saveToFile(ObjectMapper objectMapper, List<T> objects, File jsonFile) {
        try {
            // Use ObjectMapper to write the list of objects to the specified file
            objectMapper.writeValue(jsonFile, objects);
        } catch (IOException e) {
            // Handle IOException and throw a custom exception
            throw new JsonLiteClientOperationException(ErrorMessages.FAILED_SAVING, e);
        }
    }

    // Method to get the value of a field from an object using reflection
    public static <T> String getFieldValue(T object, String key) {
        try {
            // Generate the getter method name based on the field name (e.g., getFieldName)
            String getterMethodName = "get" + key.substring(0, 1).toUpperCase() + key.substring(1);
            // Use reflection to find the getter method
            Method getterMethod = object.getClass().getMethod(getterMethodName);
            // Invoke the getter method to retrieve the value
            Object invokeObject = getterMethod.invoke(object);
            if (invokeObject != null) {
                return invokeObject.toString(); // Return the field value as a string
            }
            return ""; // Return an empty string if the field value is null
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            // Handle exceptions when the getter method is not found or cannot be accessed
            throw new JsonLiteClientOperationException(
                    String.format(ErrorMessages.MISSING_GETTER, object.getClass().getSimpleName(), key), e);
        }
    }

    // Method to check if a given object has a field matching a key-value pair
    public static <T> boolean hasMatchingKeyValue(T object, String key, String value) {
        // Get the field value for the given key
        Object fieldValue = JsonLiteClientOperationsHelper.getFieldValue(object, key);
        // Return true if the field value matches the given value, otherwise false
        return fieldValue != null && fieldValue.toString().equals(value);
    }

}
