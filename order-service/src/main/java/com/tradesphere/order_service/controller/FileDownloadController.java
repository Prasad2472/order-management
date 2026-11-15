package com.tradesphere.order_service.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/download")
public class FileDownloadController {
    private static final Path BASE_LOCATION = Paths.get("/home/srihith/myworkdir/TradeSphere/uploads");

    @GetMapping("/{fileName}")
    public ResponseEntity<?> downloadFile(@PathVariable("fileName") String file) throws MalformedURLException {

        Path path = BASE_LOCATION.resolve(file);
        Resource resource = new UrlResource(path.toUri());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file + "\"")
                .body(resource);
    }
}
