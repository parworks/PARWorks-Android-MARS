/*******************************************************************************
 * Copyright 2013 PAR Works, Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.parworks.mars.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parworks.androidlibrary.ar.ARException;

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
		SiteTags siteTags = new SiteTags();
		siteTags.setTags(tags);
		try {
			objectMapper.writeValue(outputStream, siteTags);
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
