package cz.upce.music;

import cz.upce.music.entity.Track;
import cz.upce.music.repository.TrackRepository;
import cz.upce.music.service.TracksQueueService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class TracksQueueTest {

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private TracksQueueService tracksQueueService;

    @Test
    void addOneToQueue() {
        Track track = new Track();
        track.setName("Never There");
        trackRepository.save(track);
        List<Track> all = trackRepository.findAll();

        Long trackId = all.get((int)trackRepository.count() - 1).getId();

        tracksQueueService.add(trackId);

        // Počet skladeb ve frontě je 1
        Assertions.assertThat(tracksQueueService.getQueue().size()).isEqualTo(1);

        // Obsahuje právě vloženou skladbu
        Assertions.assertThat(tracksQueueService.getQueue().get(0).getName()).isEqualTo("Never There");

        tracksQueueService.add(trackId);
        Assertions.assertThat(tracksQueueService.getQueue().size()).isEqualTo(2);

        tracksQueueService.add(trackId);
        Assertions.assertThat(tracksQueueService.getQueue().size()).isEqualTo(3);

        tracksQueueService.remove(trackId);
        Assertions.assertThat(tracksQueueService.getQueue().size()).isEqualTo(2);
    }
}
