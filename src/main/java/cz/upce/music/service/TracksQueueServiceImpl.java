package cz.upce.music.service;

import cz.upce.music.entity.Played;
import cz.upce.music.entity.Track;
import cz.upce.music.repository.PlayedRepository;
import cz.upce.music.repository.TrackRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@SessionScope
public class TracksQueueServiceImpl implements TracksQueueService {

    private List<Track> queue;

    private final TrackRepository trackRepository;
    private final PlayedRepository playedRepository;

    public TracksQueueServiceImpl(TrackRepository trackRepository, PlayedRepository playedRepository) {
        this.trackRepository = trackRepository;
        this.playedRepository = playedRepository;
        queue = new ArrayList<>();
    }

    @Override
    public void add(Long id) {
        Track track = trackRepository.findById(id).orElseThrow(NoSuchElementException::new);
        queue.add(track);
    }

    @Override
    public void remove(Long id) {
        Track track = trackRepository.findById(id).orElseThrow(NoSuchElementException::new);
        int index = -1;
        for (int i = 0; i < queue.size(); i++) {
            if (queue.get(i).getId() == id) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            queue.remove(index);
        }
    }

    @Override
    public List<Track> getQueue() {
        return queue;
    }

    @Override
    public void checkout(Long id) {
        if (queue.size() > 0) {
            int index = -1;
            for (int i = 0; i < queue.size(); i++) {
                if (queue.get(i).getId() == id) {
                    index = i;
                    break;
                }
            }

            if (index != -1) {
                Track track = queue.get(index);

                Played played = new Played();
                played.setTimestamp(LocalDateTime.now());
                played.setWholeTrackPlayed(true);
                played.setTrack(track);
                playedRepository.save(played);

                queue.remove(index);
            }
        }
    }
}
