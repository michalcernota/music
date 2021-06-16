package ui

import cz.upce.music.MusicApplication
import cz.upce.music.dataFactory.Creator
import cz.upce.music.entity.Users
import cz.upce.music.entity.UserRoleEnum
import cz.upce.music.repository.ArtistRepository
import cz.upce.music.repository.TrackRepository
import cz.upce.music.repository.UsersRepository
import org.assertj.core.api.Assertions
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.transaction.annotation.Transactional

@SpringBootTest(classes = MusicApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(Creator.class)
@Transactional
class UITests {

    private String address = "https://music-share-nnpia.herokuapp.com/";

    @Autowired
    private Creator creator;

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private WebDriver driver;

    private WebDriverWait wait;

    @BeforeAll
    static void setupWebdriverChromeDriver() {
        String chromeDriverPath = UITests.class.getResource("/chromedriver.exe").getFile();
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        String circleCiChromeDriverPath = "/usr/local/bin/chromedriver";
        if (new File(circleCiChromeDriverPath).exists()) {
            System.setProperty("webdriver.chrome.driver", circleCiChromeDriverPath);
        }
    }

    @BeforeEach
    void setup() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setHeadless(true);

        driver = new ChromeDriver(chromeOptions);
        wait = new WebDriverWait(driver, 60);
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void performLogin(String username, String password) {
        driver.get(address);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/login']"))).click()
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='formPassword']")))
        driver.findElement(By.xpath("//input[@id='formUsername']")).sendKeys(username);
        driver.findElement(By.xpath("//input[@id='formPassword']")).sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
        driver.findElement(By.xpath("//button[@type='submit']")).click();
    }

    @Test
    void successfulAdminLoginTest() {
        performLogin("admin", "adminadmin");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/logout']")));
        Assert.assertTrue(driver.findElement(By.xpath("//a[@href='/logout']")) != null);
    }

    @Test
    void unsuccessfulUserLoginTest() {
        creator.saveEntity(new Users(username: "tester126", password: passwordEncoder.encode("tester126"), userRole: UserRoleEnum.ROLE_USER));
        performLogin("tester126", "wrong-password");
        Assert.assertTrue(driver.findElements(By.xpath("//a[@href='/logout']")).size() == 0);
    }

    @Test
    void signupTest() {
        String username = "testuser126";
        String password = "testuser126";
        String email = "testuser126@email.com";
        long oldCount = userRepository.count();

        driver.get(address);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/signup']"))).click()
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='formConfirmPassword']")))
        driver.findElement(By.xpath("//input[@id='formUsername']")).sendKeys(username);
        driver.findElement(By.xpath("//input[@id='formEmail']")).sendKeys(email);
        driver.findElement(By.xpath("//input[@id='formPassword']")).sendKeys(password);
        driver.findElement(By.xpath("//input[@id='formConfirmPassword']")).sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']"))).click();

        Assertions.assertThat(oldCount < userRepository.count());
    }
}