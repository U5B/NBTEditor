/*
 * Copyright (C) 2013-2018 Gonçalo Baltazar <me@goncalomb.com>
 *
 * This file is part of NBTEditor.
 *
 * NBTEditor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NBTEditor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NBTEditor.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.goncalomb.bukkit.mylib.reflect;

import java.util.logging.Logger;

public class NBTBase {

	private static NBTBaseAdapter adapter = null;

	public static void prepareReflection(Class<?> serverClass, Logger logger) throws Exception {
		String packageName = serverClass.getPackage().getName();
		String version = packageName.substring(packageName.lastIndexOf('.') + 1);

		Class<?> clazz = Class.forName("com.goncalomb.bukkit.mylib.reflect.NBTBaseAdapter_" + version);
		adapter = (NBTBaseAdapter) clazz.getConstructor().newInstance();
		logger.info("Loaded NBTBase adapter for " + version);
	}

	final Object _handle; // The wrapped Minecraft NBTBase instance.

	// Wraps any Minecraft tags in MyLib tags.
	// Primitives and strings are wrapped with NBTBase.
	protected static final NBTBase wrap(Object object) {
		ensureAdapter(adapter);
		return adapter.wrap(object);
	}

	// Helper method for NBTTagCompoundWrapper.merge().
	// Clones any internal Minecraft tags.
	protected static final Object clone(Object nbtBaseObject) {
		ensureAdapter(adapter);
		return adapter.clone(nbtBaseObject);
	}

	static byte getTypeId(Object handle) {
		ensureAdapter(adapter);
		return adapter.getTypeId(handle);
	}

	protected NBTBase(Object handle) {
		_handle = handle;
	}

	public NBTBase clone() {
		return wrap(clone(_handle));
	}

	@Override
	public String toString() {
		return _handle.toString();
	}

	private static void ensureAdapter(Object adapter) throws RuntimeException {
		if (adapter == null) {
			throw new RuntimeException("Version adapter is not loaded");
		}
	}
}
