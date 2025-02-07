package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.BffRawItemDto;

public interface RawItemQueryService {
    BffRawItemDto findRawItem(String productId);
}
