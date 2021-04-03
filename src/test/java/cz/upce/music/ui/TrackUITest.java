package cz.upce.music.ui;

import cz.upce.music.dataFactory.Creator;
import cz.upce.music.repository.TrackRepository;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Import(Creator.class)
public class TrackUITest {

    private WebDriver driver;

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private Creator creator;

    @BeforeAll
    public static void setupWebdriverChromeDriver() {
        String chromeDriverPath = TrackUITest.class.getResource("/chromedriver.exe").getFile();

        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
    }

    @BeforeEach
    public void setup()
    {
        driver = new ChromeDriver();
        trackRepository.deleteAll();
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void addTrackTest() {
        addOneTrack();
    }

    @Test
    public void addTrackTest2() {
        addOneTrack();
    }

    private void addOneTrack() {
        driver.get("http://localhost:8080/track-form");

        driver.findElement(By.id("name")).sendKeys("test");
        driver.findElement(By.xpath("//input[@type='submit']")).click();

        Assert.assertEquals(1, driver.findElements(By.xpath("//h2[text()='Track list']")).size());

        Assert.assertEquals(1, driver.findElements(By.xpath("//h3[text()='test']")).size());
    }
}