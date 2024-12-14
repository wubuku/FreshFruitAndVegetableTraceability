package org.dddml.ffvtraceability.domain.util;

import org.dddml.ffvtraceability.specialization.Page;

import java.util.function.Function;
import java.util.stream.Collectors;

public class PageUtils {
    private PageUtils() {
    }

    public static <T, R> Page.PageImpl<R> toPage(org.springframework.data.domain.Page<T> page, Function<? super T, ? extends R> mapper) {
        Page.PageImpl<R> p = new Page.PageImpl<>(
                page.getContent().stream().map(mapper).collect(Collectors.toUnmodifiableList()),
                page.getTotalElements()
        );
        p.setSize(page.getSize());
        p.setNumber(page.getNumber());
        return p;
    }
}
