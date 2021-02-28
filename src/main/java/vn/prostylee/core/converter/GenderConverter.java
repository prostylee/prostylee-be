package vn.prostylee.core.converter;

import vn.prostylee.auth.constant.Gender;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public final class GenderConverter {

    private static final List<String> MALE_LIST_DEFINITION = Arrays.asList("male", "1", "boy", "men", "m");
    private static final List<String> FEMALE_LIST_DEFINITION = Arrays.asList("female", "0", "girl", "women", "f");

    private GenderConverter() {
    }

    public static Character convertGender(String gender) {
        if (MALE_LIST_DEFINITION.stream().anyMatch(isMatched(gender))) {
            return Gender.MALE.getValue();
        } else if (FEMALE_LIST_DEFINITION.stream().anyMatch(isMatched(gender))) {
            return Gender.FEMALE.getValue();
        }
        return Gender.OTHER.getValue();
    }

    private static Predicate<String> isMatched(String gender) {
        return el -> el.equalsIgnoreCase(gender);
    }
}
