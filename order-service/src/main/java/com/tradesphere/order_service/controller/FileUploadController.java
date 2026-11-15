package com.tradesphere.order_service.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/upload")
public class FileUploadController {
    private static final String BASE_LOCATION = "/home/srihith/myworkdir/TradeSphere/uploads";

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {

        Path path = Paths.get(BASE_LOCATION);
        Path target = path.resolve(file.getOriginalFilename());

        try (InputStream inputStream = file.getInputStream();
             OutputStream outputStream = Files.newOutputStream(target)) {
            inputStream.transferTo(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok("Uploaded File: "+file.getOriginalFilename());
    }
}
