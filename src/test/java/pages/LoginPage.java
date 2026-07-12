package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {
    @FindBy(xpath = "//input[@placeholder='Username']")
    private WebElement usernameField;
    @FindBy(xpath = "//input[@placeholder='Password']")
    private WebElement passwordField;
    @FindBy(css = "button[type='submit']")
    private WebElement loginButton;

    @FindBy(className = "orangehrm-login-layout")
    private WebElement layoutForPage;
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    public void goTo() {
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
    }
    public void login(String username, String password) {
        waitForPage(layoutForPage);
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        waitAndClick(loginButton);
    }
}
