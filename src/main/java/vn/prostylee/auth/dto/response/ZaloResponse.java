package vn.prostylee.auth.dto.response;

import lombok.Data;
import vn.prostylee.auth.constant.Gender;

import java.util.Date;

@Data
public class ZaloResponse {

    private int id;

    private Date birthDay;

    private Gender gender;

    private String avatar;

    private String name;
}
