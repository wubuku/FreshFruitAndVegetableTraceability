package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffRawItemDto;

public interface RawItemQueryService {

    BffRawItemDto findRawItemUseCache(String productId);

    BffRawItemDto findRawItem(String productId);
}
