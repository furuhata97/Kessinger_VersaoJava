package com.kessinger.kessinger.controller;

import com.kessinger.kessinger.model.Periodico;
import com.kessinger.kessinger.model.Publicacao;
import com.kessinger.kessinger.model.Usuario;
import com.kessinger.kessinger.repository.PeriodicoRepository;
import com.kessinger.kessinger.repository.PublicacaoRepository;
import com.kessinger.kessinger.repository.UsuarioRepository;
import com.kessinger.kessinger.storage.StorageService;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@PreAuthorize("hasAuthority('USUARIO')")
@RequestMapping("/periodico")
public class PeriodicoController {

    private final UsuarioRepository usuarioRepository;
    private final StorageService storageService;
    private final PeriodicoRepository periodicoRepository;

    public PeriodicoController(UsuarioRepository usuarioRepository, StorageService storageService, PeriodicoRepository periodicoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.storageService = storageService;
        this.periodicoRepository = periodicoRepository;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @GetMapping
    public String listaPeriodico(Model model, HttpServletRequest req) {
        List<Periodico> listaPeriodico =  periodicoRepository.findAll();
        model.addAttribute("periodicos", listaPeriodico);
        String path = req.getRequestURL().toString();
        path = path.replace(req.getRequestURI(), "") + "/kessinger";
        model.addAttribute("caminho", path);
        System.out.println(listaPeriodico.get(0).getPublicacoes().isEmpty());

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
        return "periodico/index";
    }

    @GetMapping("/novo")
    public String novoPeriodico(Model model, HttpServletRequest req) {
        addFile(model, req);
        return "periodico/cadastro";
    }


    private void addFile(Model model, HttpServletRequest req) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Optional<Usuario> usuariOpt = usuarioRepository.findByUsername(name);
        Usuario usuario = new Usuario();
        if(usuariOpt.isPresent()) {
            usuario = usuariOpt.get();
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("usuarioID", usuario.getId());
        Periodico periodico = new Periodico();
        model.addAttribute("periodico", periodico);

        if(usuario.getFoto() != null)
            model.addAttribute("files", storageService.load(usuario.getFoto()).getFileName());

        String path = req.getRequestURL().toString();
        path = path.replace(req.getRequestURI(), "") + "/kessinger";

        model.addAttribute("caminho", path);
    }
}
