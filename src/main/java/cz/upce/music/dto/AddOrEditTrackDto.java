package cz.upce.music.dto;

import org.springframework.web.multipart.MultipartFile;

public class AddOrEditTrackDto {

    private Long id;

    private String name;

    private MultipartFile data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MultipartFile getData() {
        return data;
    }

    public void setData(MultipartFile data) {
        this.data = data;
    }
}
