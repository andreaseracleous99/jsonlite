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

public class InfoMessages {

    private InfoMessages() {
    }

    //Builder Messages
    public static final String BUILDING_CLIENT = "Building JsonLiteClient with jsonFilePath {} and model type {}.";
    public static final String BUILDING_CLIENT_COMPLETED = "JsonLiteClient building completed!";
    public static final String CREATING_FILE = "JSON file not found, creating a new JSON file {}.";

    //Operations Messages
    public static final String OBJECT_FOUND_BY_ID = "Object with ID {} found.";
    public static final String OBJECT_DELETED_BY_ID = "Object with ID {} deleted successfully.";
    public static final String OBJECTS_DELETED_BY_KEY = "Deleted {} object(s) where {} = '{}'.";
    public static final String OBJECT_INSERTED = "Object '{}' added successfully.";
    public static final String OBJECTS_UPDATED_BY_KEY = "Updated {} object(s) by setting key '{}' to value '{}'.";
    public static final String OBJECT_UPDATED_BY_ID = "Object with ID '{}' updated successfully.";
    public static final String OBJECTS_UPDATED_WHERE = "Updated {} object(s) by setting key '{}' where condition matched.";
    public static final String ALL_OBJECTS_DELETED = "All objects deleted successfully.";
    public static final String OBJECTS_DELETED_BY_CONDITION = "Deleted {} objects that matched the condition.";
}
