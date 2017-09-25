package com.kessinger.kessinger.controller;


import com.kessinger.kessinger.model.Usuario;
import com.kessinger.kessinger.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PreAuthorize("hasAuthority('USUARIO')")
@RequestMapping("/user")
public class UsuarioController {


    @GetMapping
    public String usuario(Model model) {
        return "usuario/index";
    }

}
