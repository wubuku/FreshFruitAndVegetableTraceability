package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffRawItemDto;

public interface RawItemQueryService {

    /**
     * Retrieves a raw item by its product ID using cache.
     * This is the preferred method for most use cases as it provides better performance
     * through caching while maintaining reasonable data freshness.
     *
     * @param productId the unique identifier of the product
     * @return the raw item DTO
     */
    BffRawItemDto getRawItem(String productId);

    /**
     * Retrieves a raw item by its product ID directly from the source without using cache.
     * Use this method only when real-time data is strictly required, as it may have
     * higher latency and resource consumption compared to the cached version.
     *
     * @param productId the unique identifier of the product
     * @return the raw item DTO with the most up-to-date data
     */
    BffRawItemDto getRawItemWithoutCache(String productId);
}
