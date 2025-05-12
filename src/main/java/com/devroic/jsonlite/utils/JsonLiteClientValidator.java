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
import com.devroic.jsonlite.exceptions.JsonLiteClientOperationException;

import java.io.File;
import java.lang.reflect.Field;

public class JsonLiteClientValidator {

    // Private constructor to prevent instantiation of this utility class
    private JsonLiteClientValidator() {
    }

    // Method to check if the given file name ends with ".json"
    public static boolean isJson(String fileName) {
        return fileName.endsWith(".json"); // Returns true if it's a JSON file
    }

    // Method to validate if the JSON file exists
    public static void validateJsonFileExistence(File jsonFile) {
        if (!jsonFile.exists()) {
            // Throws an exception if the file doesn't exist
            throw new JsonLiteClientOperationException(String.format(ErrorMessages.FILE_NOT_EXIST, jsonFile.getName(), jsonFile.getAbsolutePath()));
        }
    }

    // Method to validate if the ID key is valid (not null or empty)
    public static void validateIdKeyExistence(String idKey) {
        if (idKey == null || idKey.isBlank()) {
            // Throws an exception if the ID key is null or blank
            throw new JsonLiteClientOperationException(ErrorMessages.ID_KEY_NOT_SET);
        }
    }

    // Method to validate that the given object is an instance of the specified class type
    public static void validateClassMatch(Class<?> type, Object object) {
        if (!type.isInstance(object)) {
            // Throws an exception if the object is not an instance of the specified class type
            throw new JsonLiteClientOperationException(
                    String.format(ErrorMessages.INVALID_TYPE, type.getSimpleName(), object.getClass().getSimpleName())
            );
        }
    }

    // Method to validate that the specified ID key exists in the class and that its type is valid (String or numeric)
    public static void validateIdKey(Class<?> type, String idKey) {
        try {
            // Attempt to get the field corresponding to the ID key
            Field field = type.getDeclaredField(idKey);

            // Check if the field type is either String or a numeric type
            Class<?> fieldType = field.getType();
            if (!(fieldType == String.class || Number.class.isAssignableFrom(fieldType))) {
                throw new IllegalArgumentException(String.format(
                        ErrorMessages.INVALID_ID_KEY_TYPE, idKey, fieldType.getSimpleName()
                ));
            }
        } catch (NoSuchFieldException e) {
            // If the field doesn't exist in the class, throw an error
            throw new IllegalArgumentException(String.format(ErrorMessages.ID_KEY_NOT_EXISTS, idKey, type.getName()), e);
        }
    }

    // Method to check if the provided key exists as a valid field in the given class
    public static void validateKey(Class<?> type, String key) {
        Field[] fields = type.getDeclaredFields(); // Get all declared fields in the class
        for (Field field : fields) {
            // If any field name matches the provided key (case-insensitive), throw exception
            if (field.getName().equalsIgnoreCase(key)) {
                return;
            }
        }
        throw new JsonLiteClientOperationException(String.format(ErrorMessages.KEY_NOT_FOUND_IN_CLASS, key, type.getSimpleName()));
    }
}