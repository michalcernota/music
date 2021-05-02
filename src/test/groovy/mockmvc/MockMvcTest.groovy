package mockmvc

import cz.upce.music.MusicApplication
import cz.upce.music.controller.ArtistController
import cz.upce.music.repository.ArtistRepository
import cz.upce.music.repository.TrackOfPlaylistRepository
import cz.upce.music.repository.TrackRepository
import cz.upce.music.service.FileService
import cz.upce.music.service.UserService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@SpringBootTest(classes = MusicApplication.class)
@AutoConfigureMockMvc
class MockMvcTest {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private TrackOfPlaylistRepository trackOfPlaylistRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(
                new ArtistController(
                        artistRepository,
                        trackRepository,
                        trackOfPlaylistRepository,
                        fileService,
                        userService
                ))
                .build();
    }

    @Test
    @WithAnonymousUser
    void statusOkTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/artists"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("artists"));
    }
}
