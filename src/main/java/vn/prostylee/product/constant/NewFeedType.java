package vn.prostylee.product.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NewFeedType {

    STORE("store"),
    USER("user");

    private final String type;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static NewFeedType fromType(String type){
        for(NewFeedType newFeedType : NewFeedType.values()){
            if(newFeedType.getType().equalsIgnoreCase(type)){
                return newFeedType;
            }
        }
        return STORE;
    }
}
