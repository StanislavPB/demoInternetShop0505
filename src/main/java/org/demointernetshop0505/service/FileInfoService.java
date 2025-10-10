package org.demointernetshop0505.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.demointernetshop0505.entity.FileInfo;
import org.demointernetshop0505.entity.User;
import org.demointernetshop0505.repository.FileInfoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
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
    private final AmazonS3 amazonS3;

    @Value("${s3.bucket}")
    private String bucket;

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



    // ------- создаем метод для загрузки полученного файла во внешнее хранилище ---

    @Transactional
    @SneakyThrows
    public String uploadDigitalOceanStorage(MultipartFile uploadFile) {

        // переименуем файл в формат UUID + "." + расширение исходного файла

        String newFileName = createFileName(uploadFile);

        //загрузка файла в хранилище Digital Ocean

        InputStream inputStream = uploadFile.getInputStream();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(uploadFile.getContentType());

        /*
            Варианты того, что может вернуть getContentType():

            Изображения:
            - image/jpeg
            - image/png
            - image/gif
            ...

            не изображения:
            text/plain - txt
            text/html - html

            Документы:
            application/pdf
            application/msword
            application/vnd.ms-excel
            application/vnd.ms-powerpoint

            Архивы:
            application/zip

            ...

            Аудио:
            audio/mp3
            audio/wav
            ...

            Видео:
            video/mp4
            video/x-matroska (MKV)


         */


        // создаем запрос на отправку файла

        String fileLink = "";

        if (uploadFile.getContentType().startsWith("image")) {
            fileLink = "image/" + newFileName;
        } else if (uploadFile.getContentType().startsWith("text")){
            fileLink = "data/" + newFileName;
        }

        PutObjectRequest request = new PutObjectRequest(
                bucket,
                fileLink,
                inputStream,
                metadata
        ).withCannedAcl(CannedAccessControlList.PublicRead);

        amazonS3.putObject(request);

        // файл отправлен, но нам необходимо информацию о том, где он зранится
        // записать в FileInfo и в User

        String digitalOceanLink = amazonS3.getUrl(bucket, fileLink).toString();

        User currentUser = userService.getCurrentUser();

        FileInfo fileInfo = FileInfo.builder()
                .link(digitalOceanLink)
                .user(currentUser)
                .build();

        repository.save(fileInfo);


        // при необходимости digitalOceanLink можно сохранить в user
        // в поле photo - как ссылку на текущую картинку (например аватарку)

        return "Файл " + digitalOceanLink + " успешно сохранен";

    }


    //--------- private методы для упрощения работы с файлом --------
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
