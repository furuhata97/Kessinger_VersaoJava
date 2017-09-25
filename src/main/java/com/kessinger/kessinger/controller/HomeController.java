package com.kessinger.kessinger.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/", "/index", "/index.html"})
public class HomeController {

    @GetMapping
    public String home() {
        return "index";
    }

    @GetMapping("/redirectTipoUsuario")
    public String users() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().toString();
        role = role.replace("[", "");
        role = role.replace("]", "");
        return "redirect:/user";
    }

}
