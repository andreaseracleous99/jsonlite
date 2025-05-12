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

import com.devroic.jsonlite.messages.InfoMessages;
import com.devroic.jsonlite.operations.OperationsBaseHandler;
import com.devroic.jsonlite.exceptions.JsonLiteClientOperationException;
import com.devroic.jsonlite.messages.ErrorMessages;
import com.devroic.jsonlite.operations.interfaces.InsertHandler;
import com.devroic.jsonlite.operations.interfaces.SelectHandler;
import com.devroic.jsonlite.utils.JsonLiteClientOperationsHelper;
import com.devroic.jsonlite.utils.JsonLiteClientValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

public class InsertHandlerImpl extends OperationsBaseHandler implements InsertHandler {

    private final SelectHandler selectHandler;

    public InsertHandlerImpl(File jsonFile, Class<?> type, String idKey) {
        super(jsonFile, type, idKey);
        this.selectHandler = new SelectHandlerImpl(this.jsonFile, this.type, this.idKey);
    }

    private static final Logger logger = LoggerFactory.getLogger(InsertHandlerImpl.class);

    @Override
    public <T> void handleInsert(T object) {
        try {
            JsonLiteClientValidator.validateJsonFileExistence(this.jsonFile);
            JsonLiteClientValidator.validateClassMatch(this.type, object);

            List<T> objects = this.selectHandler.handleSelectAll();

            if (idKey != null && !idKey.isBlank()) {
                String newObjectId = JsonLiteClientOperationsHelper.getFieldValue(object, idKey);
                if (newObjectId == null || newObjectId.isBlank()) {
                    throw new JsonLiteClientOperationException(
                            ErrorMessages.EMPTY_OR_NULL_ID);
                }
                boolean isDuplicate = objects.stream()
                        .anyMatch(existingObject -> JsonLiteClientOperationsHelper.getFieldValue(existingObject, idKey).equals(newObjectId));
                if (isDuplicate) {
                    throw new JsonLiteClientOperationException(
                            String.format(ErrorMessages.DUPLICATE_ID, newObjectId, idKey));
                }
            }

            objects.add(object);
            JsonLiteClientOperationsHelper.saveToFile(objectMapper, objects, jsonFile);
            logger.info(InfoMessages.OBJECT_INSERTED, object);
        } catch (RuntimeException e) {
            throw new JsonLiteClientOperationException(ErrorMessages.FAILED_INSERT, e);
        }
    }

    @Override
    public <T> void handleInsertMultiple(List<T> objects) {
        for (T object : objects) {
            handleInsert(object);
        }
    }
}
