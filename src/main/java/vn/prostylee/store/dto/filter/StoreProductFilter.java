package vn.prostylee.store.dto.filter;

import lombok.Data;

@Data
public class StoreProductFilter extends StoreFilter {

    private int numberOfProducts = 10;
}
