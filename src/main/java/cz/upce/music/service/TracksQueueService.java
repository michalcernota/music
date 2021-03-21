package cz.upce.music.service;

import cz.upce.music.entity.Track;

import java.util.List;

public interface TracksQueueService {

    void add(Long id);

    void remove(Long id);

    List<Track> getQueue();

    void checkout(Long id);
}
