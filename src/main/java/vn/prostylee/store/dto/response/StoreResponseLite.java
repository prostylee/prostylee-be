package vn.prostylee.store.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class StoreResponseLite implements Serializable {

    private Long id;

    private String name;

}
