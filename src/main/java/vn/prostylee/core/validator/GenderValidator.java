package vn.prostylee.core.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.stream.Stream;

public class GenderValidator implements ConstraintValidator<Gender, Character> {

    public void initialize(Gender gender) {
        // used only if your annotation has attributes
    }

    @Override
    public boolean isValid(Character gender, ConstraintValidatorContext constraintContext) {
        // Bean Validation specification recommends to consider null values as
        // being valid. If null is not a valid value for an element, it should
        // be annotated with @NotNull explicitly.
        if (gender == null) {
            return true;
        }
        return Stream.of(vn.prostylee.auth.constant.Gender.values()).anyMatch(e -> e.getValue().equals(gender));
    }
}