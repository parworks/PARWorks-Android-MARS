package com.parworks.mars.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parworks.androidlibrary.ar.ARException;
import com.parworks.androidlibrary.response.OverlayConfiguration;

public class SiteTags {
	
	List<String> mTags;
	
	public List<String> getTags() {
		return mTags;
	}
	public void setTags(List<String> tags) {
		mTags = tags;
	}
	
	public static String toJson(List<String> tags) {
		ObjectMapper objectMapper = new ObjectMapper();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			objectMapper.writeValue(outputStream, SiteTags.class);
		} catch (JsonGenerationException e) {
			throw new ARException(e.getMessage(),e);
		} catch (JsonMappingException e) {
			throw new ARException(e.getMessage(),e);
		} catch (IOException e) {
			throw new ARException(e.getMessage(),e);
		}
		return outputStream.toString();
	}
	public static SiteTags fromJson(String json) {
		ObjectMapper objectMapper = new ObjectMapper();
		SiteTags siteTags = null;
		try {
			siteTags = objectMapper.readValue(json, SiteTags.class);
		} catch (JsonParseException e) {
			throw new ARException(e.getMessage(),e);
		} catch (JsonMappingException e) {
			throw new ARException(e.getMessage(),e);
		} catch (IOException e) {
			throw new ARException(e.getMessage(),e);
		}
		return siteTags;
	}

}
