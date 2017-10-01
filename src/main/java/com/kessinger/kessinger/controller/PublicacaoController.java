package com.kessinger.kessinger.controller;

import com.kessinger.kessinger.model.Publicacao;
import com.kessinger.kessinger.model.Usuario;
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
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Controller
@PreAuthorize("hasAuthority('USUARIO')")
@RequestMapping("/publicacao")
public class PublicacaoController {

    private final UsuarioRepository usuarioRepository;
    private final StorageService storageService;
    private final PublicacaoRepository publicacaoRepository;

    public PublicacaoController(UsuarioRepository usuarioRepository, StorageService storageService, PublicacaoRepository publicacaoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.storageService = storageService;
        this.publicacaoRepository = publicacaoRepository;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @GetMapping("/novo")
    public String usuario(Model model) {
        model.addAttribute("publicacao", new Publicacao());
        return "publicacao/cadastro";
    }

    /*private void addFile(Model model, HttpServletRequest req) {
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
    }*/

}
