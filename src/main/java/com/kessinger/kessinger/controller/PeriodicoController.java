package com.kessinger.kessinger.controller;

import com.kessinger.kessinger.model.Periodico;
import com.kessinger.kessinger.model.Publicacao;
import com.kessinger.kessinger.model.Usuario;
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
        Usuario usuario = obtemUsuarioAtual(model, req);
        return "periodico/index";
    }

    @GetMapping("/meus")
    public String listaMeusPeriodicos(Model model, HttpServletRequest req) {
        Usuario usuario = obtemUsuarioAtual(model, req);

        List<Periodico> listaPeriodico =  periodicoRepository.findByUser(usuario);
        model.addAttribute("periodicos", listaPeriodico);
        String path = req.getRequestURL().toString();
        path = path.replace(req.getRequestURI(), "") + "/kessinger";
        model.addAttribute("caminho", path);

        model.addAttribute("podeExcluir", true);

        return "periodico/index";
    }

    @GetMapping("/publicacao/{id}")
    public String listaPublicacoesDosPeriodicos(Model model, HttpServletRequest req, @PathVariable Integer id) {
        Periodico periodico = periodicoRepository.findOne(id);
        List<Publicacao> publicacoes = periodico.getPublicacoes();
        model.addAttribute("publicacoes", publicacoes);
        Usuario usuario = obtemUsuarioAtual(model, req);
        return "publicacao/index";
    }

    @PostMapping("/delete/{id}")
    @Cascade(CascadeType.DELETE)
    public String deletaPeriodico(Model model, HttpServletRequest req, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Periodico periodico = periodicoRepository.findOne(id);
        System.out.println(id);
        if (periodico.getPublicacoes().isEmpty()) {
            Usuario usuario = obtemUsuarioAtual(model, req);
            usuario.getPeriodico().remove(periodico);
            usuarioRepository.save(usuario);
            periodico.setUsuario(null);
            periodicoRepository.delete(id);
            redirectAttributes.addFlashAttribute("removido",
                    true);
            return "redirect:/periodico/meus";
        }
        else {
            redirectAttributes.addFlashAttribute("impossivel", true);
            return "redirect:/periodico/meus";
        }
    }

    @GetMapping("/novo")
    public String novoPeriodico(Model model, HttpServletRequest req) {
        addFile(model, req);
        return "periodico/cadastro";
    }

    @GetMapping("{id}")
    public String editaPeriodico(Model model, HttpServletRequest req, @PathVariable Integer id) {
        Usuario user = obtemUsuarioAtual(model, req);
        Periodico periodico = periodicoRepository.findOne(id);
        model.addAttribute("periodico", periodico);
        return "periodico/editar";
    }

    @PostMapping("{id}")
    public String PersisteEdicaoPeriodico(Model model, HttpServletRequest req, Periodico periodico, @PathVariable Integer id, RedirectAttributes redirectAttributes){
        Periodico periodicoAux = periodicoRepository.findOne(id);
        periodico.setUpload(periodicoAux.getUpload());
        periodico.setUsuario(periodicoAux.getUsuario());
        periodico.setPublicacoes(periodicoAux.getPublicacoes());
        periodico.setId(periodicoAux.getId());
        periodicoRepository.save(periodico);
        return "redirect:/periodico/meus";
    }

    private void addFile(Model model, HttpServletRequest req) {
        Usuario usuario = obtemUsuarioAtual(model, req);
        model.addAttribute("usuarioID", usuario.getId());
        Periodico periodico = new Periodico();
        model.addAttribute("periodico", periodico);

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
