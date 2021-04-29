package ui

import cz.upce.music.MusicApplication
import cz.upce.music.dataFactory.Creator
import cz.upce.music.entity.Artist
import cz.upce.music.entity.User
import cz.upce.music.entity.UserRoleEnum
import cz.upce.music.repository.ArtistRepository
import cz.upce.music.repository.TrackRepository
import cz.upce.music.repository.UserRepository
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@SpringBootTest(classes = MusicApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Import(Creator.class)
class ArtistUITest {

    @Autowired
    private Creator creator;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private WebDriver driver;

    @BeforeAll
    static void setupWebdriverChromeDriver() {
        String chromeDriverPath = ArtistUITest.class.getResource("/chromedriver.exe").getFile();
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
    }

    @BeforeEach
    void setup() {
        driver = new ChromeDriver();
        userRepository.deleteAll();
        trackRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void performLogin(String username, String password) {
        driver.get("http://localhost:8080/login");
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.xpath("//button[@type='submit']")).click();
    }

    @Test
    void successfulAdminLoginTest() {
        creator.saveEntity(new User(username: "admin", password: passwordEncoder.encode("admin"), userRole: UserRoleEnum.Admin));
        performLogin("admin", "admin");

        Assert.assertEquals(driver.getCurrentUrl(), "http://localhost:8080/");
    }

    @Test
    void successfulUserLoginTest() {
        creator.saveEntity(new User(username: "user", password: passwordEncoder.encode("password"), userRole: UserRoleEnum.User));
        performLogin("user", "password");

        Assert.assertEquals(driver.getCurrentUrl(), "http://localhost:8080/");
    }

    @Test
    void unsuccessfulUserLoginTest() {
        creator.saveEntity(new User(username: "user", password: passwordEncoder.encode("password"), userRole: UserRoleEnum.User));
        performLogin("user", "wrong-password");

        Assert.assertEquals(driver.getCurrentUrl(), "http://localhost:8080/login?error");
    }

    @Test
    void addTrackToArtistTest() {
        Artist artist = new Artist(name: "Test artist");
        creator.saveEntity(artist);
        creator.saveEntity(new User(username: "admin", password: passwordEncoder.encode("admin"), userRole: UserRoleEnum.Admin));
        performLogin("admin", "admin");

        Assert.assertEquals(driver.getCurrentUrl(), "http://localhost:8080/");

        driver.get("http://localhost:8080/artist/" + artist.getId() + "/add-tracks");
        driver.findElement(By.id("name")).sendKeys("Test track");
        driver.findElement(By.id("track")).sendKeys("C:\\Users\\michc\\Music\\Anyway You Want It- Journey.mp3");
        driver.findElement(By.xpath("//input[@type='submit']")).click();

        Assert.assertEquals(1, driver.findElements(By.xpath("//h3[text()='Test track']")).size());
    }
}