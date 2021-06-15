package mockmvc

import cz.upce.music.MusicApplication
import cz.upce.music.entity.Artist
import cz.upce.music.service.implementations.ArtistServiceImpl
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

import java.security.Principal

import static org.hamcrest.Matchers.is

@SpringBootTest(classes = MusicApplication.class)
@AutoConfigureMockMvc
class MockMvcArtistsTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArtistServiceImpl artistService;

    @Test
    @WithAnonymousUser
    void getAllArtistsTest() {
        List<Artist> artistList = new ArrayList();
        artistList.add(new Artist(name: "one", nationality: "CZE", pathToImage: "c://imagepath.png"));
        artistList.add(new Artist(name: "two", nationality: "USA", pathToImage: "c://imagepath.png"));

        Mockito.when(artistService.getAll()).thenReturn(artistList);

        mockMvc.perform(MockMvcRequestBuilders.get("/artists"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("\$[0].name", is("one")))
                .andExpect(MockMvcResultMatchers.jsonPath("\$[0].nationality", is("CZE")))
                .andExpect(MockMvcResultMatchers.jsonPath("\$[1].name", is("two")))
                .andExpect(MockMvcResultMatchers.jsonPath("\$[1].nationality", is("USA")));
    }

    @Test
    @WithAnonymousUser
    void createArtist() {
        Principal principal = new Principal() {
            @Override
            String getName() {
                return "TEST_PRINCIPAL";
            }
        }

        mockMvc.perform(MockMvcRequestBuilders.post("/artists")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .principal(principal)
                .param("name", "one")
                .param("nationality", "CZE")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
