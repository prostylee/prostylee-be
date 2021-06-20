package vn.prostylee.core.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import vn.prostylee.core.utils.JsonUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.List;

@Converter
public class JsonStringToListLongConverter implements AttributeConverter<List<Long>, String> {

    @Override
    public String convertToDatabaseColumn(List<Long> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return JsonUtils.toJson(list);
    }

    @Override
    public List<Long> convertToEntityAttribute(String json) {
        if (StringUtils.isBlank(json)) {
            return new ArrayList<>();
        }
        return JsonUtils.fromJson(json, new TypeReference<ArrayList<Long>>() {});
    }

}