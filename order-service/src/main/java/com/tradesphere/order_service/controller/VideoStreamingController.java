package com.tradesphere.order_service.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/video")
public class VideoStreamingController {
    private static Path VIDEO_PATH = Paths.get("/home/srihith/myworkdir/TradeSphere/uploads");

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> streamVideo(@PathVariable("fileName") String file,
                                                @RequestHeader HttpHeaders headers) throws IOException {

        Path vedioPath = VIDEO_PATH.resolve(file);
        long fileSize = Files.size(vedioPath);

        Resource resource = new UrlResource(vedioPath.toUri());

        List<HttpRange> ranges = headers.getRange();

        if (ranges.isEmpty()) {
            return ResponseEntity.ok().contentType(MediaType.valueOf("video/mp4"))
                    .contentLength(fileSize)
                    .body(resource);
        }

        HttpRange range = ranges.get(0);

        long start = range.getRangeStart(fileSize);
        long end = range.getRangeEnd(fileSize);
        long chunkSize = end - start + 1;

        InputStream inputStream = Files.newInputStream(vedioPath);
        inputStream.skip(start);


        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaType.valueOf("video/mp4"))
                .header(HttpHeaders.CONTENT_RANGE,
                        "bytes " + start + "-" + end + "/" + fileSize)
                .contentLength(chunkSize)
                .body(new InputStreamResource(inputStream));
    }


}
