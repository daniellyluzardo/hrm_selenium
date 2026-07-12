package tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import stepdefinitions.Hooks;

public class BasePage extends Hooks {
    @Test (dataProvider = "loginCredentials")
    public void loginAttempt (String username, String password){
        LoginPage loginPage = new LoginPage(driver);
        loginPage.goTo();
        loginPage.login(username,password);
    }
    @DataProvider(name = "loginCredentials")
    public Object[][] loginCredentials() {
        return new Object[][] {
                {"Admin", "admin123"},
//                {"wronguser", "wrongpass", "Invalid credentials"},
//                {"tomsmith", "wrongpass", "Invalid credentials"}
        };
        }
}