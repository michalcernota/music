package cz.upce.music;

import cz.upce.music.dataFactory.Creator;
import cz.upce.music.dto.PlaylistDto;
import cz.upce.music.entity.Users;
import cz.upce.music.service.interfaces.PlaylistService;
import cz.upce.music.service.interfaces.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Import(Creator.class)
@Transactional
public class ServiceTests {

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    @WithMockUser
    void createUserAndPlaylistTest() {
        Users user = new Users();
        user.setUsername("my test user");
        user.setPassword("password");
        userService.saveUser(user);

        Assertions.assertThat(userService.findUserByUsername("my test user").getPassword().equals(passwordEncoder.encode("password")));

        PlaylistDto playlistDto = new PlaylistDto();
        playlistDto.setOwnerName("my test user");
        playlistDto.setName("my test playlist");
        playlistDto = playlistService.create(playlistDto);

        PlaylistDto detail = playlistService.getPlaylistDetail(playlistDto.getId());
        Assertions.assertThat(detail != null);
        Assertions.assertThat(detail.getOwnerName().equals("my test user"));
    }

}
