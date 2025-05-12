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

import com.devroic.jsonlite.messages.ErrorMessages;
import com.devroic.jsonlite.messages.InfoMessages;
import com.devroic.jsonlite.JsonLiteClient;
import com.devroic.jsonlite.exceptions.JsonLiteClientBuilderException;
import com.devroic.jsonlite.utils.JsonLiteClientValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonLiteClientBuilder {
    private static final Logger logger = LoggerFactory.getLogger(JsonLiteClientBuilder.class);

    // The path to the JSON file to be used by the client.
    private String jsonFilePath;

    // The class type that will be handled by the client.
    private Class<?> type;

    // The key used as the identifier (ID) in the JSON objects.
    private String idKey;

    // Flag indicating whether the file should be created if it doesn't exist.
    // Default value = false
    private boolean createFileIfNotExists = false;

    /**
     * Sets the file path for the JSON file to be used.
     *
     * @param jsonFilePath The path to the JSON file.
     * @return The builder instance for method chaining.
     */
    public JsonLiteClientBuilder jsonFilePath(String jsonFilePath) {
        this.jsonFilePath = jsonFilePath;
        return this;
    }

    /**
     * Sets the class type for the JSON client.
     *
     * @param type The class type to be used (e.g., Person.class).
     * @return The builder instance for method chaining.
     */
    public JsonLiteClientBuilder type(Class<?> type) {
        this.type = type;
        return this;
    }

    /**
     * Sets the key for the ID field in the JSON objects.
     *
     * @param idKey The key used for identifying the object (e.g., "id").
     * @return The builder instance for method chaining.
     */
    public JsonLiteClientBuilder idKey(String idKey) {
        this.idKey = idKey;
        return this;
    }

    /**
     * Sets the flag to indicate whether to create the JSON file if it doesn't exist.
     *
     * @param createFileIfNotExists Boolean flag to create the file if it does not exist.
     * @return The builder instance for method chaining.
     */
    public JsonLiteClientBuilder createFileIfNotExists(boolean createFileIfNotExists) {
        this.createFileIfNotExists = createFileIfNotExists;
        return this;
    }

    /**
     * Builds and returns a JsonLiteClient instance based on the specified configurations.
     *
     * @return A fully constructed JsonLiteClient.
     * @throws JsonLiteClientBuilderException If any required property is missing or invalid.
     */
    public JsonLiteClient build() {
        // Validate required parameters
        if (this.jsonFilePath == null || this.jsonFilePath.isBlank()) {
            throw new JsonLiteClientBuilderException(ErrorMessages.JSON_FILE_PATH_NULL);
        }
        if (this.type == null) {
            throw new JsonLiteClientBuilderException(ErrorMessages.TYPE_NULL);
        }
        if (this.idKey != null && !this.idKey.isBlank()) {
            JsonLiteClientValidator.validateIdKey(type, idKey);
        }
        // Log the building process
        logger.info(InfoMessages.BUILDING_CLIENT, this.jsonFilePath, this.type);

        // Return the constructed JsonLiteClientImpl instance
        return new JsonLiteClientImpl(this.jsonFilePath, this.type, this.idKey, this.createFileIfNotExists);
    }
}
