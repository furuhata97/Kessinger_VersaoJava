package com.kessinger.kessinger.controller;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import com.kessinger.kessinger.model.Periodico;
import com.kessinger.kessinger.model.Publicacao;
import com.kessinger.kessinger.model.Usuario;
import com.kessinger.kessinger.repository.PeriodicoRepository;
import com.kessinger.kessinger.repository.PublicacaoRepository;
import com.kessinger.kessinger.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kessinger.kessinger.storage.StorageFileNotFoundException;
import com.kessinger.kessinger.storage.StorageService;

import javax.servlet.http.HttpServletResponse;

@Controller
public class FileUploadController {

    private final StorageService storageService;
    private final UsuarioRepository usuarioRepository;
    private final PublicacaoRepository publicacaoRepository;
    private final PeriodicoRepository periodicoRepository;

    @Autowired
    public FileUploadController(StorageService storageService, UsuarioRepository usuarioRepository, PublicacaoRepository publicacaoRepository, PeriodicoRepository periodicoRepository) {
        this.storageService = storageService;
        this.usuarioRepository = usuarioRepository;
        this.publicacaoRepository = publicacaoRepository;
        this.periodicoRepository = periodicoRepository;
    }


    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/user/perfil")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes, HttpServletResponse resp) throws IOException {

        storageService.store(file);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Optional<Usuario> usuariOpt = usuarioRepository.findByUsername(name);
        Usuario usuario = new Usuario();
        if(usuariOpt.isPresent()) {
            usuario = usuariOpt.get();
        }
        final File folder = new File("webapps/kessinger/images");

            File[] listOfFiles = folder.listFiles();
            int tamanho = folder.listFiles().length;
            String nomeArquivo = "perfil" + (tamanho - 1) + ".jpg";


            for (File fil : listOfFiles) {
                if (fil.getName().equals(file.getOriginalFilename())) {
                    if (usuario.getFoto() != null){
                        for (File f:listOfFiles){
                            if (f.getName().equals(usuario.getFoto())){
                                f.delete();
                            }
                        }
                        fil.renameTo(new File("webapps/kessinger/images/" + usuario.getFoto()));
                        usuario.setFoto(usuario.getFoto());
                    }else{
                        fil.renameTo(new File("webapps/kessinger/images/" + nomeArquivo));
                        usuario.setFoto(nomeArquivo);
                    }
                }
            }

            usuarioRepository.save(usuario);

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/user/perfil";
    }

    @PostMapping("/publicacao/novo")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes, HttpServletResponse resp, Publicacao publicacao, Integer usuarioID) throws IOException {

        storageService.store(file);
        publicacao.setUpload(file.getOriginalFilename());
        Usuario usuario = usuarioRepository.findOne(usuarioID);
        publicacao.setUser(usuario);
        publicacaoRepository.save(publicacao);
        Periodico periodico = periodicoRepository.findOne(publicacao.getPeriodico().getId());
        periodico.addPublicacao(publicacao);
        periodicoRepository.save(periodico);
        usuario.addPublicacao(publicacao);
        usuarioRepository.save(usuario);
        redirectAttributes.addFlashAttribute("message2",
                true);
        return "redirect:/user";
    }

    @PostMapping("/periodico/novo")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes, HttpServletResponse resp, Periodico periodico, Integer usuarioID) throws IOException {

        storageService.store(file);
        periodico.setUpload(file.getOriginalFilename());
        Usuario usuario = usuarioRepository.findOne(usuarioID);
        periodico.setUsuario(usuario);
        periodicoRepository.save(periodico);
        usuario.addPeriodico(periodico);
        usuarioRepository.save(usuario);
        redirectAttributes.addFlashAttribute("message2",
                true);
        return "redirect:/user";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}