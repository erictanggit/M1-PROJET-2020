package io.github.erictanggit.json;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import io.github.erictanggit.logger.Logger;

public class JsonParsing {
	@SuppressWarnings("unchecked")
	public static <T> T[] readFileJson(String fileName, Class<T> type) {
		Gson gson = new Gson();
		T[] json = null;
		try {
			json = (T[]) gson.fromJson(new FileReader(fileName), type);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			Logger.error(JsonParsing.class, "Error occured " + e.toString());
		}
		Logger.info(JsonParsing.class, "JSON parsed :");
		for (int i = 0; i < json.length; i++) {
			Logger.info(JsonParsing.class, json[i]);
		}
		return json;
	}

}
