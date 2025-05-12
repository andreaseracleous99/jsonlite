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

public class WarningMessages {

    private WarningMessages() {
    }

    //Operations Messages
    public static final String OBJECT_NOT_FOUND_BY_ID = "Object with id {} not found.";
    public static final String OBJECT_NOT_FOUND_BY_KEY = "Objects where {} = '{}' not found.";
    public static final String OBJECT_NOT_FOUND_BY_CONDITION = "No objects found that matched the condition.";
}
