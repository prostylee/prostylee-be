package vn.prostylee.core.provider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Component
public class ThymeleafTemplateProcessor {

    private final SpringTemplateEngine templateEngine;
    private final Context context;

    public ThymeleafTemplateProcessor(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        context = getContext();
    }

    /**
     * Replace placeholder in the template
     *
     * @param template The thymeleaf template with markers
     * @param data The object data for replacing the markers
     * @param <T> The data type of the given data value
     *
     * @return String after replaced data in template
     */
    public <T> String process(String template, T data) {
        return Optional.ofNullable(template)
                .map(txt -> replaceThymeleafMarker(txt, data))
                .orElse(null);
    }

    private Context getContext() {
        Locale locale = LocaleContextHolder.getLocale();
        return new Context(locale);
    }

    /**
     * Convert java object to map
     *
     * @param data The object data
     * @return the map with key value of the given object data
     */
    private <T> Map<String, Object> convertObjectToMap(T data) {
        if (data == null) {
            return new HashMap<>();
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(data, new TypeReference<>() {
        });
    }

    /**
     * Replace mail marker by thymeleaf
     *
     * @param thymeleafMarker The thymeleaf template with markers
     * @param data The object data for replacing the markers
     * @return The string after replacing values for the markers
     */
    private <T> String replaceThymeleafMarker(String thymeleafMarker, T data) {
        Map<String, Object> vars = convertObjectToMap(data);
        return replaceThymeleafMarker(thymeleafMarker, vars);
    }

    /**
     * Replace mail marker by thymeleaf
     *
     * @param thymeleafMarker The thymeleaf template with markers
     * @param vars The parameters for replacing the markers
     * @return The string after replacing values for the markers
     */
    private String replaceThymeleafMarker(String thymeleafMarker, Map<String, Object> vars) {
        context.setVariables(vars);
        return templateEngine.process(thymeleafMarker, context);
    }
}
