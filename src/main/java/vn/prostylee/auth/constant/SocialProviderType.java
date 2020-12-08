package vn.prostylee.auth.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SocialProviderType {
    FIREBASE("firebase"),
    ZALO("zalo");

    private String type;

    public String getType(){
        return this.type;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SocialProviderType fromType(String type){
        for(SocialProviderType providerType : SocialProviderType.values()){
            if(providerType.getType().equalsIgnoreCase(type)){
                return providerType;
            }
        }
        throw new IllegalArgumentException();
    }
}