package vn.prostylee.statistics.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatisticsType {
    STORE,
    USER

    /*@JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static StatisticsType fromType(String type){
        for(StatisticsType profileType : StatisticsType.values()){
            if(profileType.getType().equalsIgnoreCase(type)){
                return profileType;
            }
        }
        return STORE;
    }*/
}
