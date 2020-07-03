package com.nammaexpo.utils;

import com.nammaexpo.expection.ExpoException;
import com.nammaexpo.models.enums.MessageCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Service
public class FileUtils {

    private static final int BYTE_RANGE = 1024;

    private Path imagePath;

    private Path videoPath;

    @Autowired
    public FileUtils(
            @Value(value = "${imagePath}") String imagePath,
            @Value(value = "${videoPath}") String videoPath)
            throws IOException {

        this.imagePath = Paths.get(imagePath);
        this.videoPath = Paths.get(videoPath);

        createDirectories();
    }

    private void createDirectories() throws IOException {

        if (!imagePath.toFile().exists()) {
            Files.createDirectory(imagePath);
        }

        if (!videoPath.toFile().exists()) {
            Files.createDirectory(videoPath);
        }
    }

    private String generateFileId() {
        return UUID.randomUUID().toString();
    }

    public String storeImageFile(MultipartFile file) throws IOException {
        String fileId = generateFileId();

        Path targetLocation = this.imagePath.resolve(fileId);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return fileId;
    }

    public Resource fetchImageFile(String fileId) {
        try {
            Path filePath = this.imagePath.resolve(fileId).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                throw ExpoException.error(MessageCode.FILE_NOT_FOUND);
            }

            return resource;
        } catch (MalformedURLException exception) {
            log.error("Error : ", exception);
            throw ExpoException.error(MessageCode.FILE_NOT_FOUND);
        }
    }

    public String storeVideoFile(MultipartFile file) throws IOException {
        String fileId = generateFileId();

        Path targetLocation = this.videoPath.resolve(fileId);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return fileId;
    }

    public byte[] fetchVideoFile(String fileId, long start, long end) {
        Path path = this.videoPath.resolve(fileId).normalize();

        try (InputStream inputStream = (Files.newInputStream(path));
             ByteArrayOutputStream bufferedOutputStream = new ByteArrayOutputStream()) {
            byte[] data = new byte[BYTE_RANGE];
            int nRead;
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                bufferedOutputStream.write(data, 0, nRead);
            }
            bufferedOutputStream.flush();
            byte[] result = new byte[(int) (end - start) + 1];
            System.arraycopy(bufferedOutputStream.toByteArray(), (int) start, result, 0, result.length);
            return result;

        } catch (IOException e) {
            log.error("Error : ", e);
            throw ExpoException.error(MessageCode.INTERNAL_SERVER_ERROR);
        }
    }

    public Resource fetchVideoForDownload(String fileId) {
        try {
            Path filePath = this.videoPath.resolve(fileId).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                throw ExpoException.error(MessageCode.FILE_NOT_FOUND);
            }

            return resource;
        } catch (MalformedURLException exception) {
            log.error("Error : ", exception);
            throw ExpoException.error(MessageCode.FILE_NOT_FOUND);
        }
    }
}
