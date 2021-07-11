package vn.prostylee.core.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import vn.prostylee.core.exception.ValidatingException;

public enum TargetType {
    PRODUCT,
    POST,
    STORY,
    STORE,
    BRANCH,
    USER,
    COMMENT,
    UNKNOWN;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static TargetType convert(String type) throws ValidatingException {
        for(TargetType targetType : TargetType.values()){
            if(targetType.name().equalsIgnoreCase(type)){
                return targetType;
            }
        }
        throw new ValidatingException("The [" + type + "] has not supported!");
    }
}
