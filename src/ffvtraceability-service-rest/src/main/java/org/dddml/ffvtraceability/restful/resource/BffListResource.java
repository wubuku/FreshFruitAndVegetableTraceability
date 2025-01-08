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
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping(path = "BffList", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class BffListResource {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BffRawItemApplicationService bffRawItemApplicationService;

    @GetMapping("Items")
    public List<? extends BffRawItemDto> getRawItems(
            @RequestParam(value = "active", required = false) String active) {
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
