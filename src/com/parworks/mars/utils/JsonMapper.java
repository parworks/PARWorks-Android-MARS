package com.parworks.mars.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMapper {

	@SuppressWarnings("serial")
	private static ObjectMapper mapper = new ObjectMapper();
	
	public static ObjectMapper get() {
		if (mapper == null) {
			mapper = new ObjectMapper();
		}
		return mapper;
	}
}
