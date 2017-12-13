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
import java.util.*;

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
        List<Periodico> listaPeriodico =  periodicoRepository.findAll();
        model.addAttribute("publicacoes", listaPublicacao);
        model.addAttribute("periodicos", listaPeriodico);
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

    @GetMapping("/resultado")
    public String resultadoPublicacao(@RequestParam(value = "search", required = false) String q, Model model, HttpServletRequest req) {
        List<Publicacao> listaPublicacao =  publicacaoRepository.findAllByNome(q);
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

    @GetMapping("/buscaAvancada")
    public String resultadoAvPublicacao(Publicacao p, Model model, HttpServletRequest req) {
        System.out.println(p.getNome() + " " + p.getAutor());
        String AreaString;
        if (p.getArea() == null){
            AreaString = "";
        }else {
            AreaString = p.getArea().toString();
        }

        String CategoriaString;
        if (p.getCategoria() == null){
            CategoriaString = "";
        }else {
            CategoriaString = p.getCategoria().toString();
        }


        System.out.println("Ano = "+p.getAno());

        List<Publicacao> listaPublicacao;

        if (p.getAno() == null && p.getPeriodico() == null){
            listaPublicacao = publicacaoRepository.findAllUnlessDateAndPeriodico(p.getNome(), p.getAutor(), AreaString, CategoriaString);
        }else {
            if(p.getAno() == null){
                listaPublicacao =  publicacaoRepository.findAllUnlessDate(p.getNome(), p.getAutor(), AreaString, CategoriaString, p.getPeriodico());
            }else{
                if (p.getPeriodico() == null){
                    listaPublicacao =  publicacaoRepository.findAllUnlessPeriodico(p.getNome(), p.getAutor(), AreaString, CategoriaString, p.getAno());
                }else{
                    listaPublicacao = publicacaoRepository.findAllByFiltro(p.getNome(), p.getAutor(), AreaString, CategoriaString, p.getAno(), p.getPeriodico());
                }
            }
        }

        System.out.println("Ano = "+p.getAno());
        System.out.println("Exemplo :: "+listaPublicacao);
        model.addAttribute("publicacoes", listaPublicacao);
        List<Periodico> listaPeriodico =  periodicoRepository.findAll();
        model.addAttribute("periodicos", listaPeriodico);
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
        List<Periodico> listaPeriodico =  periodicoRepository.findAll();
        model.addAttribute("periodicos", listaPeriodico);
        String path = req.getRequestURL().toString();
        path = path.replace(req.getRequestURI(), "") + "/kessinger";
        model.addAttribute("caminho", path);

        model.addAttribute("podeExcluir", true);

        return "publicacao/index";
    }

    @GetMapping("{id}")
    public String editaPublicacao(Model model, HttpServletRequest req, @PathVariable Integer id) {
        Usuario user = obtemUsuarioAtual(model, req);
        Publicacao publicacao = publicacaoRepository.findOne(id);
        model.addAttribute("publicacao", publicacao);
        List<Periodico> listaPeriodico =  periodicoRepository.findAll();
        model.addAttribute("periodicos", listaPeriodico);
        return "publicacao/editar";
    }

    @PostMapping("{id}")
    public String PersisteEdicaoPublicacao(Model model, HttpServletRequest req, Publicacao publicacao, @PathVariable Integer id, RedirectAttributes redirectAttributes){
        Publicacao publicacaoAux = publicacaoRepository.findOne(id);
        publicacao.setUpload(publicacaoAux.getUpload());
        publicacao.setUser(publicacaoAux.getUser());
        publicacao.setPeriodico(publicacaoAux.getPeriodico());
        publicacao.setId(publicacaoAux.getId());
        publicacaoRepository.save(publicacao);
        return "redirect:/publicacoes/meus";
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
