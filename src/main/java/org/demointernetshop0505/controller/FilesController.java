package org.demointernetshop0505.controller;

import lombok.RequiredArgsConstructor;
import org.demointernetshop0505.service.FileInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class FilesController {

    private final FileInfoService service;

    @PostMapping("/files")
    public ResponseEntity<String> upload(@RequestParam("uploadFile")MultipartFile file) throws IOException {
        return ResponseEntity.ok().body(service.upload(file));
    }
}
