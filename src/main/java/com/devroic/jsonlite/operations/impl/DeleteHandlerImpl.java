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

import com.devroic.jsonlite.exceptions.JsonLiteClientOperationException;
import com.devroic.jsonlite.messages.ErrorMessages;
import com.devroic.jsonlite.messages.InfoMessages;
import com.devroic.jsonlite.messages.WarningMessages;
import com.devroic.jsonlite.operations.OperationsBaseHandler;
import com.devroic.jsonlite.operations.interfaces.DeleteHandler;
import com.devroic.jsonlite.operations.interfaces.SelectHandler;
import com.devroic.jsonlite.utils.JsonLiteClientOperationsHelper;
import com.devroic.jsonlite.utils.JsonLiteClientValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class DeleteHandlerImpl extends OperationsBaseHandler implements DeleteHandler {

    private final SelectHandler selectHandler;

    public DeleteHandlerImpl(File jsonFile, Class<?> type, String idKey) {
        super(jsonFile, type, idKey);
        this.selectHandler = new SelectHandlerImpl(this.jsonFile, this.type, this.idKey);
    }

    private static final Logger logger = LoggerFactory.getLogger(DeleteHandlerImpl.class);

    @Override
    public <T> boolean handleDeleteAll() {
        try {
            JsonLiteClientValidator.validateJsonFileExistence(this.jsonFile);
            List<T> emptyList = new ArrayList<>();
            JsonLiteClientOperationsHelper.saveToFile(objectMapper, emptyList, jsonFile);
            logger.info(InfoMessages.ALL_OBJECTS_DELETED);
            return true;
        } catch (RuntimeException e) {
            logger.error(ErrorMessages.FAILED_DELETE_ALL, e);
            return false;
        }
    }

    @Override
    public <T> boolean handleDeleteById(String id) {
        try {
            JsonLiteClientValidator.validateJsonFileExistence(this.jsonFile);
            JsonLiteClientValidator.validateIdKeyExistence(this.idKey);

            List<T> objects = this.selectHandler.handleSelectAll();
            long count = objects.stream()
                    .filter(object -> JsonLiteClientOperationsHelper.getFieldValue(object, idKey).equals(id))
                    .count();

            if (count > 1) {
                throw new JsonLiteClientOperationException(String.format(ErrorMessages.MULTIPLE_OBJECTS_FOUND, count, id));
            } else if (count == 0) {
                logger.warn(WarningMessages.OBJECT_NOT_FOUND_BY_ID, id);
                return false;
            }

            boolean isDeleted = objects.removeIf(object -> JsonLiteClientOperationsHelper.getFieldValue(object, idKey).equals(id));
            if (isDeleted) {
                JsonLiteClientOperationsHelper.saveToFile(this.objectMapper, objects, this.jsonFile);
                logger.info(InfoMessages.OBJECT_DELETED_BY_ID, id);
            }
            return isDeleted;
        } catch (RuntimeException e) {
            throw new JsonLiteClientOperationException(ErrorMessages.FAILED_DELETE_BY_ID, e);
        }
    }

    @Override
    public <T> boolean handleDeleteByKey(String key, String value) {
        try {
            JsonLiteClientValidator.validateJsonFileExistence(this.jsonFile);
            JsonLiteClientValidator.validateKey(this.type, key);

            List<T> objects = this.selectHandler.handleSelectAll();
            long count = objects.stream()
                    .filter(object -> JsonLiteClientOperationsHelper.hasMatchingKeyValue(object, key, value))
                    .count();

            if (count == 0) {
                logger.warn(WarningMessages.OBJECT_NOT_FOUND_BY_KEY, key, value);
                return false;
            }

            boolean isDeleted = objects.removeIf(object -> JsonLiteClientOperationsHelper.hasMatchingKeyValue(object, key, value));
            if (isDeleted) {
                JsonLiteClientOperationsHelper.saveToFile(this.objectMapper, objects, this.jsonFile);
                logger.info(InfoMessages.OBJECTS_DELETED_BY_KEY, count, key, value);
            }
            return isDeleted;
        } catch (RuntimeException e) {
            throw new JsonLiteClientOperationException(ErrorMessages.FAILED_DELETE_BY_KEY, e);
        }
    }

    public <T> boolean handleDeleteWhere(Predicate<T> condition) {
        try {
            List<T> objects = this.selectHandler.handleSelectAll();

            List<T> matchingObjects = objects.stream()
                    .filter(condition)
                    .toList();

            if (matchingObjects.isEmpty()) {
                logger.warn(WarningMessages.OBJECT_NOT_FOUND_BY_CONDITION);
                return false;
            }

            boolean isDeleted = objects.removeAll(matchingObjects);

            if (isDeleted) {
                JsonLiteClientOperationsHelper.saveToFile(objectMapper, objects, jsonFile);
                logger.info(InfoMessages.OBJECTS_DELETED_BY_CONDITION, matchingObjects.size());
            }

            return isDeleted;
        } catch (Exception e) {
            throw new JsonLiteClientOperationException(ErrorMessages.FAILED_DELETE_BY_CONDITION, e);
        }
    }

}
