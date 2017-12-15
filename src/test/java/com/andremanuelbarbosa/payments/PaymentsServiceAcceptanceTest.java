package com.andremanuelbarbosa.payments;

import com.google.common.collect.Lists;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import org.junit.AfterClass;
import org.junit.runner.RunWith;

import java.io.File;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/cucumber/features",
        glue = {"com.andremanuelbarbosa.payments.steps"},
        plugin = {"html:target/cucumber", "json:target/cucumber.json"},
        tags = "not @ignore",
        strict = true,
        monochrome = true,
        dryRun = false)
public class PaymentsServiceAcceptanceTest extends PaymentsServiceIntegrationTest {

    @AfterClass
    public static void tearDownAfterClass() {

        PaymentsServiceIntegrationTest.tearDownAfterClass();

        (new ReportBuilder(Lists.newArrayList("target/cucumber.json"),
                new Configuration(new File("target"), "Payments Service"))).generateReports();
    }
}
