package com.kessinger.kessinger.controller;


import com.kessinger.kessinger.model.Periodico;
import com.kessinger.kessinger.model.Publicacao;
import com.kessinger.kessinger.model.Usuario;
import com.kessinger.kessinger.repository.PeriodicoRepository;
import com.kessinger.kessinger.repository.UsuarioRepository;
import com.kessinger.kessinger.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@PreAuthorize("hasAuthority('USUARIO')")
@RequestMapping("/user")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PeriodicoRepository periodicoRepository;
    private final StorageService storageService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @Autowired
    public UsuarioController(UsuarioRepository usuarioRepository, PeriodicoRepository periodicoRepository, StorageService storageService) {
        this.usuarioRepository = usuarioRepository;
        this.periodicoRepository = periodicoRepository;
        this.storageService = storageService;
    }

    @GetMapping
    public String usuario(Model model, HttpServletRequest req) {
        addPicture(model, req);
        Publicacao publicacao = new Publicacao();
        model.addAttribute("publicacao", publicacao);
        List<Periodico> listaPeriodico =  periodicoRepository.findAll();
        model.addAttribute("periodicos", listaPeriodico);
        return "usuario/index";
    }

    @GetMapping("/perfil")
    public String listar(Model model, HttpServletRequest req) {
        addPicture(model, req);
        return "usuario/perfil";
    }

    @GetMapping("/{id}")
    public String update(Model model, @PathVariable Integer id) {
        model.addAttribute("usuario", usuarioRepository.findOne(id));
        model.addAttribute("visualizar", false);
        model.addAttribute("update", true);
        return "register";
    }

    @PostMapping("/{id}")
    public String updateUsuario(Usuario usuario, RedirectAttributes ra) {
        usuarioRepository.save(usuario);
        ra.addFlashAttribute("sucesso", "Paciente " + usuario.getNome() + " atualizado com sucesso!");
        return "redirect:/user/perfil";
    }

    private void addPicture(Model model, HttpServletRequest req) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Optional<Usuario> usuariOpt = usuarioRepository.findByUsername(name);
        Usuario usuario = new Usuario();
        if(usuariOpt.isPresent()) {
            usuario = usuariOpt.get();
        }
        model.addAttribute("usuario", usuario);

        if(usuario.getFoto() != null)
            model.addAttribute("files", storageService.load(usuario.getFoto()).getFileName());

        String path = req.getRequestURL().toString();
        path = path.replace(req.getRequestURI(), "") + "/kessinger";

        model.addAttribute("caminho", path);
    }

}
