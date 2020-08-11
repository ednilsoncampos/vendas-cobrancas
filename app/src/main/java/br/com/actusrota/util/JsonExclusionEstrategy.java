package br.com.actusrota.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class JsonExclusionEstrategy implements ExclusionStrategy {
	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		return false;
	}

	@Override
	public boolean shouldSkipField(FieldAttributes fieldAttributes) {
		return fieldAttributes.getAnnotation(JsonExclude.class) != null;
	}
}
