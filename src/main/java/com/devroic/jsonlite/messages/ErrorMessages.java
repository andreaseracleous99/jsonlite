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

package com.devroic.jsonlite.messages;

public class ErrorMessages {

    private ErrorMessages() {
    }

    //Builder Messages
    public static final String JSON_FILE_PATH_NULL = "jsonFilePath cannot be null.";
    public static final String TYPE_NULL = "type cannot be null.";
    public static final String FILE_CREATION_ERROR = "Failed to create the file: %s";
    public static final String MUST_BE_JSON_FILE = "The file must be a JSON file";
    public static final String INVALID_ID_KEY_TYPE = "The idKey '%s' is of type '%s', but only 'String' or numeric types are allowed.";
    public static final String ID_KEY_NOT_EXISTS = "The specified idKey '%s' does not exist in the class '%s'";

    //Operations Validations Messages
    public static final String FILE_NOT_EXIST = "JSON File %s does not exist in path %s.";
    public static final String MISSING_GETTER = "Class %s must have a getter method for '%s'.";
    public static final String ID_KEY_NOT_SET = "IdKey is not set. Please use JsonLiteClientBuilder.idKey() to set it.";
    public static final String MULTIPLE_OBJECTS_FOUND = "Multiple (%s) objects with id '%s' found.";
    public static final String DUPLICATE_ID = "Duplicate value found for '%s' on key '%s'. Each value must be unique.";
    public static final String EMPTY_OR_NULL_ID = "ID cannot be empty or null.";
    public static final String INVALID_TYPE = "Invalid type: Expected '%s' but got '%s'.";
    public static final String KEY_NOT_FOUND_IN_CLASS = "The key '%s' does not exist in class '%s'.";
    public static final String ID_MISMATCH_UPDATE = "The ID of the updated object does not match the provided ID: expected '%s', but got '%s'.";

    //Operations Messages
    public static final String FAILED_READING = "Failed to read or parse the JSON file.";
    public static final String FAILED_SAVING = "Failed to save into JSON file.";
    public static final String FAILED_SELECT_KEY = "Failed to extract values for key: %s";
    public static final String FAILED_SELECT_KEYS = "Failed to extract values for keys: %s";
    public static final String FAILED_SELECT_BY_ID = "Failed to extract values from id";
    public static final String FAILED_SELECT_BY_KEY = "Failed to select objects by key: %s, value: %s";
    public static final String FAILED_SELECT_WHERE = "Failed to select objects based on condition";
    public static final String FAILED_DELETE_ALL = "Failed to delete all objects.";
    public static final String FAILED_DELETE_BY_ID = "Failed to delete values from id";
    public static final String FAILED_DELETE_BY_KEY = "Failed to delete by key value";
    public static final String FAILED_DELETE_BY_CONDITION = "Failed to delete objects based on condition.";
    public static final String FAILED_INSERT = "Failed to insert object into JSON file";
    public static final String FAILED_UPDATE_BY_ID = "Failed to update object with ID %s.";
    public static final String FAILED_UPDATE_BY_KEY = "Failed to update objects with key %s.";
    public static final String FAILED_UPDATE_WHERE = "Failed to update the value for key where condition matches.";
}