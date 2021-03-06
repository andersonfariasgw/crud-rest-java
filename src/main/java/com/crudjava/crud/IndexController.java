package com.crudjava.crud;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController {
    @GetMapping(value = {"/", "/tela", "/cad-edit-tela"})
    public String index() {
        return "index.html";
    }
}
