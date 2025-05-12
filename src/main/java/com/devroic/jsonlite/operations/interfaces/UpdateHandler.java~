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

package com.techbasecore.jsonlite.operations.interfaces;

import java.util.Map;
import java.util.function.Predicate;

public interface UpdateHandler {
    <T> boolean handleUpdateKey(String key, Object newValue);

    <T> boolean handleUpdateById(String id, T updatedObject);

    <T> boolean handleUpdateWhere(Predicate<T> condition, String key, Object newValue);

    <T> boolean handleUpdateWhere(Predicate<T> condition, Map<String, Object> updates);

}
