package org.demointernetshop0505.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.demointernetshop0505.entity.FileInfo;
import org.demointernetshop0505.repository.FileInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileInfoService {

    private final String LOCAL_STORAGE_PATH = "src/main/resources/static/upload";
    private final FileInfoRepository repository;
    private final UserService userService;

    @Transactional
    public String uploadLocalStorage(MultipartFile uploadFile) throws IOException {

        Path fileStorageLocation = Paths.get(LOCAL_STORAGE_PATH);

        String newFileName = createFileName(uploadFile);

        // создаем targetLocation который будет содержать полный путь до места хранения и имя файла

        Path targetLocation = fileStorageLocation.resolve(newFileName);

        // копируем данные из файла upload, который хранится во временном хранилище
        // в папку и под именем, которое мы создали

        Files.copy(uploadFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        String link = targetLocation.toString();



        FileInfo fileInfo = FileInfo.builder()
                .link(link)
                .user(userService.getCurrentUser())
                .build();

        repository.save(fileInfo);


        return "Файл " + link + "успешно сохранен";
    }

    private String createFileName(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        // получаем исходное имя файла

        String extension = "";

        if (originalFileName != null) {
            int indexExtension = originalFileName.lastIndexOf(".") + 1;
            //получсаекм индекс начала ирасширения полученного файла (следующий
            // символ после точки)
            extension = originalFileName.substring(indexExtension);
        } else {
            throw  new NullPointerException("Null original file name");
        }

        // генерируем случайное имя для файла с помощью UUID

        String uuidFileName = UUID.randomUUID().toString();
        String newFileName = uuidFileName + "." + extension;

        return newFileName;
    }
}
