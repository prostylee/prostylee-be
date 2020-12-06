package vn.prostylee.auth.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum SocialProviderType {
    FIREBASE("firebase"),
    ZALO("zalo");

    private String type;

    public String getType(){
        return this.type;
    }

    @Override public String toString() {
        return type;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SocialProviderType fromType(String type){
        for(SocialProviderType providerType : SocialProviderType.values()){
            if(providerType.getType().equals(type)){
                return providerType;
            }
        }
        throw new IllegalArgumentException();
    }
}