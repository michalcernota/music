package cz.upce.music.service.implementations;

import cz.upce.music.service.interfaces.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadFile(MultipartFile file, FileType fileType) throws IOException {
        String destination = "";
        if (fileType == FileType.IMAGE) {
            if (file != null) {
                destination = "images/" + new Date().getTime() + ".png";
                file.transferTo(Paths.get(destination));
            } else {
                Files.copy(Paths.get("images/default/artist.png"), Paths.get(destination));
            }
        }
        else if (fileType == FileType.TRACK) {
            destination = "tracks/" + new Date().getTime() + ".mp3";
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
            relativeFilePath = "tracks/" + fileName;
        }

        File file = new File(relativeFilePath);
        return file.delete();
    }
}
