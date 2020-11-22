package vn.prostylee.core.service;

import java.util.List;

/**
 * The base service for master data
 * @param <R> The response type
 */
public interface MasterDataService<R> {

    List<R> findAll();
}
