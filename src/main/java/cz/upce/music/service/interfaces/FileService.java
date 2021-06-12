package cz.upce.music.service.interfaces;

import cz.upce.music.service.implementations.FileType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String uploadFile(MultipartFile file, FileType fileType) throws IOException;
    boolean deleteFile(String filePath, FileType fileType);
}
