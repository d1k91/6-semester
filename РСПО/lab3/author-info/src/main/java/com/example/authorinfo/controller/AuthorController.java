package com.example.authorinfo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthorController {
    @GetMapping("/")
    public String authorInfo(Model model) {
        model.addAttribute("name", "Глинский Вадим Юрьевич");
        model.addAttribute("bio", "Студент группы ИП-212");
        return "author";
    }
}
