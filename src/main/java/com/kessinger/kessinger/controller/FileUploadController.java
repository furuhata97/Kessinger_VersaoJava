package com.kessinger.kessinger.controller;



import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Collectors;

import com.kessinger.kessinger.model.Usuario;
import com.kessinger.kessinger.repository.UsuarioRepository;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kessinger.kessinger.storage.StorageFileNotFoundException;
import com.kessinger.kessinger.storage.StorageService;

import javax.servlet.http.HttpServletResponse;

@Controller
public class FileUploadController {

    private final StorageService storageService;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public FileUploadController(StorageService storageService, UsuarioRepository usuarioRepository) {
        this.storageService = storageService;
        this.usuarioRepository = usuarioRepository;
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
        usuario.setFoto(file.getOriginalFilename());
        usuarioRepository.save(usuario);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/user/perfil";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
