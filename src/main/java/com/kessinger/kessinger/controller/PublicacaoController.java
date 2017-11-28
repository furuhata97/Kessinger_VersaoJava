package com.kessinger.kessinger.controller;

import com.kessinger.kessinger.model.Periodico;
import com.kessinger.kessinger.model.Publicacao;
import com.kessinger.kessinger.model.Usuario;
import com.kessinger.kessinger.model.enums.Area;
import com.kessinger.kessinger.repository.PeriodicoRepository;
import com.kessinger.kessinger.repository.PublicacaoRepository;
import com.kessinger.kessinger.repository.UsuarioRepository;
import com.kessinger.kessinger.storage.StorageService;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@PreAuthorize("hasAuthority('USUARIO')")
@RequestMapping("/publicacoes")
public class PublicacaoController {

    private final UsuarioRepository usuarioRepository;
    private final StorageService storageService;
    private final PublicacaoRepository publicacaoRepository;
    private final PeriodicoRepository periodicoRepository;

    public PublicacaoController(UsuarioRepository usuarioRepository, StorageService storageService, PublicacaoRepository publicacaoRepository, PeriodicoRepository periodicoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.storageService = storageService;
        this.publicacaoRepository = publicacaoRepository;
        this.periodicoRepository = periodicoRepository;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @GetMapping
    public String listaPublicacao(Model model, HttpServletRequest req) {
        List<Publicacao> listaPublicacao =  publicacaoRepository.findAll();
        model.addAttribute("publicacoes", listaPublicacao);
        String path = req.getRequestURL().toString();
        path = path.replace(req.getRequestURI(), "") + "/kessinger";
        model.addAttribute("caminho", path);

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
        return "publicacao/index";
    }

    @GetMapping("/meus")
    public String listaMinhasPublicacoes(Model model, HttpServletRequest req) {
        Usuario usuario = obtemUsuarioAtual(model, req);

        List<Publicacao> listaPublicacao =  publicacaoRepository.findByUser(usuario);
        model.addAttribute("publicacoes", listaPublicacao);
        String path = req.getRequestURL().toString();
        path = path.replace(req.getRequestURI(), "") + "/kessinger";
        model.addAttribute("caminho", path);

        model.addAttribute("podeExcluir", true);

        return "publicacao/index";
    }


    @PostMapping("/delete/{id}")
    @Cascade(CascadeType.DELETE)
    public String deletaPublicacao(Model model, HttpServletRequest req, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Publicacao publicacao = publicacaoRepository.findOne(id);
        System.out.println(id);
            Usuario usuario = obtemUsuarioAtual(model, req);
            usuario.getPublicacoes().remove(publicacao);
            usuarioRepository.save(usuario);
            Periodico periodico = periodicoRepository.findOne(publicacao.getPeriodico().getId());
            periodico.getPublicacoes().remove(publicacao);
            periodicoRepository.save(periodico);
            publicacao.setUser(null);
            publicacao.setPeriodico(null);
            publicacaoRepository.delete(id);
            redirectAttributes.addFlashAttribute("removido",
                    true);
            return "redirect:/publicacoes/meus";

    }


    @GetMapping("/novo")
    public String novaPublicacao(Model model,  HttpServletRequest req) {
        addFile(model, req);
        List<Periodico> listaPeriodico =  periodicoRepository.findAll();
        model.addAttribute("periodicos", listaPeriodico);
        return "publicacao/cadastro";
    }


    private void addFile(Model model, HttpServletRequest req) {
        Usuario usuario = obtemUsuarioAtual(model, req);
        model.addAttribute("usuarioID", usuario.getId());

        Publicacao publicacao = new Publicacao();
        model.addAttribute("publicacao", publicacao);

        if(usuario.getFoto() != null)
            model.addAttribute("files", storageService.load(usuario.getFoto()).getFileName());
    }

    private Usuario obtemUsuarioAtual(Model model, HttpServletRequest req){
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

        return usuario;
    }

}
