package org.demointernetshop0505.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileInfoService {



    public String upload(MultipartFile file) throws IOException {



        return "Файл успешно сохранен";
    }
}
