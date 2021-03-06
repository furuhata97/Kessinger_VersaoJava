package com.kessinger.kessinger.controller;

import com.kessinger.kessinger.repository.UsuarioRepository;
import com.kessinger.kessinger.model.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/register")
public class RegistroController {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    private final UsuarioRepository ur;

    @Autowired
    public RegistroController(UsuarioRepository ur) {
        this.ur = ur;
    }

    @GetMapping
    public String register_get(Model m) {
        m.addAttribute("usuario", new Usuario());
        return "register";
    }

    @PostMapping("/username")
    public @ResponseBody
    Boolean exist(String username) {
        Optional<Usuario> userOptional = ur.findByUsername(username);
        return !userOptional.isPresent();
    }

    @Bean
    public PasswordEncoder pe() {
        return new BCryptPasswordEncoder();
    }

    @PostMapping
    public String register_post(@Valid Usuario usuario, BindingResult br, String confirm, RedirectAttributes ra, Model m) {
        if(!usuario.getPassword().equals(confirm)) {
            m.addAttribute("usuario", usuario);
            ra.addFlashAttribute("erro", "As senhas devem ser iguais");
            return "register";
        }
        usuario.setPassword(pe().encode(usuario.getPassword()));
        ur.save(usuario);
        ra.addFlashAttribute("sucesso", true);
        return "redirect:/login";
    }
}
