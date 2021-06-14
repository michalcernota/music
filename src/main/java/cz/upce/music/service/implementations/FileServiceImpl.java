package cz.upce.music.service.implementations;

import cz.upce.music.service.interfaces.FileService;
import org.apache.tomcat.util.http.fileupload.util.mime.MimeUtility;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadFile(MultipartFile file, FileType fileType) throws IOException {
        String destination = "";

        if (fileType == FileType.IMAGE) {
            if (file != null) {
                destination = "images/" + new Date().getTime() + getFileExtension(file);
                file.transferTo(Paths.get(destination));
            } else {
                Files.copy(Paths.get("images/default/artist.png"), Paths.get(destination));
            }
        }
        else if (fileType == FileType.TRACK) {
            destination = "media/" + new Date().getTime() + getFileExtension(file);
            file.transferTo(Paths.get(destination));
        }

        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(destination)
                .toUriString();
    }

    @Override
    public boolean deleteFile(String filePath, FileType fileType) {
        String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
        String relativeFilePath = "";

        if (fileType == FileType.IMAGE) {
            relativeFilePath = "images/" + fileName;
        }
        else if (fileType == FileType.TRACK) {
            relativeFilePath = "media/" + fileName;
        }

        File file = new File(relativeFilePath);
        return file.delete();
    }

    private String getFileExtension(MultipartFile file) throws IOException {
        Optional<String> optionalExtension = getExtensionByStringHandling(file.getOriginalFilename());
        if (!optionalExtension.isPresent()) {
            throw new IOException("Unknown file extension.");
        }

        return optionalExtension.get();
    }

    private Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".")));
    }
}
