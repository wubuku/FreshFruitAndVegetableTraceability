package org.dddml.ffvtraceability.restful.resource;

import org.dddml.ffvtraceability.domain.BffRawItemDto;
import org.dddml.ffvtraceability.domain.service.BffRawItemApplicationService;
import org.dddml.ffvtraceability.domain.service.BffRawItemServiceCommands;
import org.dddml.ffvtraceability.specialization.DomainErrorUtils;
import org.dddml.ffvtraceability.specialization.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping(path = "BffLists", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class BffListResource {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BffRawItemApplicationService bffRawItemApplicationService;

    //
    // NOTE: 这里资源的路径命名应该和“分页列表”查询接口的命名保持一直，仅去掉“Bff”前缀。
    //   因为在类名上已经包含了“Bff”前缀，所以这里不需要再重复。
    //
    @GetMapping("RawItems") // 因为对应的分页查询接口的路径名是 "BffRawItems"
    public List<? extends BffRawItemDto> getRawItems(
            @RequestParam(value = "active", required = false) String active
    ) {
        BffRawItemServiceCommands.GetRawItems getRawItems = new BffRawItemServiceCommands.GetRawItems();
        getRawItems.setPage(0);
        getRawItems.setSize(Integer.MAX_VALUE);
        getRawItems.setActive(active);
        try {
            getRawItems.setRequesterId(SecurityContextUtil.getRequesterId());
            Page<BffRawItemDto> rawItemDtoPage = bffRawItemApplicationService.when(getRawItems);
            return rawItemDtoPage.getContent();
        } catch (Exception ex) {
            logger.info(ex.getMessage(), ex);
            throw DomainErrorUtils.convertException(ex);
        }
    }

}
