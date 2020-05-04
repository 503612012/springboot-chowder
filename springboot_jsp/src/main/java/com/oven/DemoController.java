package com.oven;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DemoController {

    @RequestMapping("/test")
    public ModelAndView text() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("msg", "Oven");
        return mv;
    }

}
