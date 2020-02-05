package com.crudjava.crud;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/aaaaa")
public class cadEditTela {
    @GetMapping(value = {"/novo"})
    public String index() {
        return "index.html";
    }
}