package cz.upce.music.dto;

import org.springframework.web.multipart.MultipartFile;

public class AddOrEditArtistDto {

    private Long id;

    private String name;

    private String nationality;

    private MultipartFile image;

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

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
