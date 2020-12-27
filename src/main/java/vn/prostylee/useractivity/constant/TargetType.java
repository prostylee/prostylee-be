package vn.prostylee.useractivity.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import vn.prostylee.auth.constant.SocialProviderType;

@AllArgsConstructor
public enum TargetType {
    PRODUCT("product"),
    STORE("store");

    @Getter
    private String status;

    public String getType(){
        return this.status;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static TargetType fromType(String type){
        for(TargetType providerType : TargetType.values()){
            if(providerType.getType().equalsIgnoreCase(type)){
                return providerType;
            }
        }
        throw new IllegalArgumentException();
    }
}
