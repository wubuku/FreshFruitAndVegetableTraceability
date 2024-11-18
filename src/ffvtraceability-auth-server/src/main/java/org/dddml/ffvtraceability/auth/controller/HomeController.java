package org.dddml.ffvtraceability.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String home() {
        return "redirect:/oauth2-test";  // 重定向到测试页面
    }

}
