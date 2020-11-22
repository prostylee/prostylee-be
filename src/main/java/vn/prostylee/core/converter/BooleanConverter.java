package vn.prostylee.core.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * The helper class for custom Jackson convert boolean to numeric Json and vice
 * versa
 */
public class BooleanConverter {

	private static final int TRUE_VALUE = 1;
	private static final int FALSE_VALUE = 2;

	private BooleanConverter() {
		super();
	}

	public static class Serialize extends JsonSerializer<Boolean> {

		@Override
		public void serialize(Boolean value, JsonGenerator jgen, SerializerProvider serializers) throws IOException {
			Integer number = toInteger(value);
			if (number == null) {
				jgen.writeNull();
			} else {
				jgen.writeString("" + number);
			}
		}
	}

	public static class Deserialize extends JsonDeserializer<Boolean> {
		public Boolean deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
			String numberAsString = jp.getText();
			if (StringUtils.isBlank(numberAsString)) {
				return null;
			}
			if (StringUtils.equalsIgnoreCase(numberAsString, "true")
					|| StringUtils.equalsIgnoreCase(numberAsString, "false")) {
				return BooleanUtils.toBooleanObject(numberAsString);
			}
			return toBooleanObject(Integer.valueOf(numberAsString));
		}
	}

	private static Boolean toBooleanObject(final Integer value) {
		if (value == null) {
			return null;
		}
		return value == TRUE_VALUE ? Boolean.TRUE : Boolean.FALSE;
	}

	private static Integer toInteger(final Boolean boolValue) {
		if (boolValue == null) {
			return null;
		}
		return boolValue ? TRUE_VALUE : FALSE_VALUE;
	}
}