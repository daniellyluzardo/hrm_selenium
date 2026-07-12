package stepdefinitions;

import io.cucumber.java.en.Then;
import org.testng.Assert;
import pages.CandidatePage;

public class CandidatesSteps {
    CandidatePage candidates;
    @Then("they should see a Dashboard {string} panel")
    public void they_should_see_a_dashboard(String expectedText) {
            candidates = new CandidatePage(Hooks.driver);
            Assert.assertTrue(candidates.dashboardText().contains(expectedText));
    }

    @Then("they should see a message containing {string}")
    public void they_should_see_an_error(String expectedText) {
        candidates = new CandidatePage(Hooks.driver);
        Assert.assertTrue(candidates.InvalidError().contains(expectedText));
    }

    @Then("they should see widget {string}")
    public void they_should_see_widget(String label) {
        candidates = new CandidatePage(Hooks.driver);
        Assert.assertTrue(candidates.widgetText(label).contains(label));
    }
}
