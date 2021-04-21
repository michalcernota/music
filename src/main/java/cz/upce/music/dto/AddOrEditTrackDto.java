package cz.upce.music.dto;

import org.springframework.web.multipart.MultipartFile;

public class AddOrEditTrackDto {

    private Long id;

    private String name;

    private MultipartFile track;

    private Long artistId;

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

    public MultipartFile getTrack() {
        return track;
    }

    public void setTrack(MultipartFile track) {
        this.track = track;
    }

    public Long getArtistId() {
        return artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }
}
