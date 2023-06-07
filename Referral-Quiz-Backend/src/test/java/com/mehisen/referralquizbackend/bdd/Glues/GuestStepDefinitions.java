package com.mehisen.referralquizbackend.bdd.Glues;

import io.cucumber.java.en.When;
import com.mehisen.referralquizbackend.bdd.SpringIntegrationTest;

public class GuestStepDefinitions extends SpringIntegrationTest {
    @When("^the client calls endpoint (.+)$")
    public void the_client_calls_endpoint(String url) throws Throwable {
        executeGet(url);
    }

}
