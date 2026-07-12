package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CandidatePage extends BasePage{

    @FindBy(xpath = "//span[text()='Dashboard']")
    private WebElement leftMenuButtons;

    @FindBy(xpath = "//p[text()='Invalid credentials']")
    private WebElement invalidMessage;

    public CandidatePage(WebDriver driver) {
        super(driver);
    }
    public String dashboardText() {
        waitForPage(leftMenuButtons);
        return waitAndGetText(leftMenuButtons);
    }

    public String InvalidError() {
        waitForPage(invalidMessage);
        return waitAndGetText(invalidMessage);
    }

    public String widgetText(String label) {
        WebElement widget = driver.findElement(By.xpath(String.format("//p[text()='%s']", label)));
        return waitAndGetText(widget);
    }
}
