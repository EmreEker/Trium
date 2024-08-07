package com.trium.boot;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.trium.core.Response;

public class ResponseMessageConverter implements HttpMessageConverter<Response> {

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
	    return clazz.isAssignableFrom(Response.class);
	}

	@Override
	public void write(Response response, MediaType mediaType, HttpOutputMessage httpOutputMessage) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
		String json = objectMapper.writeValueAsString(response);
		JsonNode jsonNode = objectMapper.readTree(json);
		((ObjectNode) jsonNode).put("elementSize", response.getElementSize());
		String updatedJson = objectMapper.writeValueAsString(jsonNode);
		httpOutputMessage.getBody().write(updatedJson.getBytes());
	}

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
	    return false;
	}

	@Override
	public List<MediaType> getSupportedMediaTypes() {
	    return Arrays.asList(MediaType.APPLICATION_JSON);
	}

	@Override
	public Response read(Class<? extends Response> clazz, HttpInputMessage inputMessage)
	        throws IOException, HttpMessageNotReadableException {
	    return null;
	}
}