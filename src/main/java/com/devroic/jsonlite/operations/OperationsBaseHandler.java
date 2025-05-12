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

package com.devroic.jsonlite.operations;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public abstract class OperationsBaseHandler {

    protected File jsonFile;
    protected ObjectMapper objectMapper;
    protected Class<?> type;
    protected String idKey;

    protected OperationsBaseHandler(File jsonFile, Class<?> type, String idKey) {
        this.objectMapper = new ObjectMapper();
        this.jsonFile = jsonFile;
        this.type = type;
        this.idKey = idKey;
    }
}
