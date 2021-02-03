package vn.prostylee.store.dto.filter;

import lombok.Data;

@Data
public class StoreCategoryFilter extends StoreFilter {

    private int numberOfCategory = 10;
}
