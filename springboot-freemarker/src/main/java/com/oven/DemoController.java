package com.oven;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DemoController {

    @RequestMapping("/test")
    public String index(Model model) {
        model.addAttribute("msg", "Oven");
        return "index";
    }

}
