package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.LoginPage;

public class LoginSteps {
    LoginPage loginPage;

    @Given("the user is on the login page")
        public void the_user_is_on_the_login_page () {
            loginPage = new LoginPage(Hooks.driver);
            loginPage.goTo();
        }

        @When("they log in with username {string} and password {string}")
        public void they_log_in_with_username_and_password(String username, String password) {
            loginPage.login(username, password);
        }

    }
