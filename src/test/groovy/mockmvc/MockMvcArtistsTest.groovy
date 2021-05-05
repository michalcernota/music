package mockmvc

import cz.upce.music.MusicApplication
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest(classes = MusicApplication.class)
@AutoConfigureMockMvc
class MockMvcArtistsTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithAnonymousUser
    void statusOkTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/artists"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("artists"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("artists"));
    }
}
