package vn.prostylee.auth.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import vn.prostylee.auth.constant.Gender;
import vn.prostylee.auth.dto.response.ZaloResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ZaloConverter{
    private static final String MALE_RESPONSE_KEY = "male";
    private static final String FEMALE_RESPONSE_KEY = "female";
    private static final String FORMAT_DD_MM_YYYY = "dd/MM/yyyy";
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DD_MM_YYYY);

    public static class Deserialize extends JsonDeserializer<String> {

        @Override
        public String deserialize(JsonParser jp, DeserializationContext context) throws IOException {
            ObjectCodec oc = jp.getCodec();
            JsonNode node = oc.readTree(jp);
            return Optional.ofNullable(node.get("data")).map(jsonNode -> jsonNode.get("url").asText()).orElse(null);
        }
    }

    public static Character convertGender(ZaloResponse response){
        if(MALE_RESPONSE_KEY.equalsIgnoreCase(response.getGender())){
            return Gender.MALE.getValue();
        }else if(FEMALE_RESPONSE_KEY.equalsIgnoreCase(response.getGender())){
            return Gender.FEMALE.getValue();
        }else{
            return Gender.OTHER.getValue();
        }
    }
    public static int convertDay(String birthday) {
        return getLocalDate(birthday).getDayOfMonth();
    }

    public static int convertMonth(String birthday) {
        return getLocalDate(birthday).getMonthValue();
    }

    public static int convertYear(String birthday) {
        return getLocalDate(birthday).getYear();
    }

    private static LocalDate getLocalDate(String birthday) {
        return LocalDate.parse(birthday, formatter);
    }
}
