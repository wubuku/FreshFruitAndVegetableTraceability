package org.dddml.ffvtraceability.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OAuth2TestController {

    @GetMapping("/oauth2-test")
    public String testPage() {
        return "oauth2-test";
    }

    @GetMapping("/oauth2-test-callback")
    public String callback() {
        return "oauth2-test-callback";
    }
}