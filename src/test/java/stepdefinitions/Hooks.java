package stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class Hooks {
    public static WebDriver driver;

//  Opens a fresh browser in `@BeforeMethod`** — before every single `@Test`. This is the safer default: each test is fully independent, since every test relaunches Chrome
    @Before
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }
    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
