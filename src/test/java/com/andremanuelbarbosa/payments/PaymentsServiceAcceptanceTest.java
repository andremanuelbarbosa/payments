package com.andremanuelbarbosa.payments;

import com.google.common.collect.Lists;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static ru.yandex.qatools.embed.postgresql.distribution.Version.Main.V9_6;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/cucumber/features",
        glue = {"com.andremanuelbarbosa.payments.steps"},
        plugin = {"html:target/cucumber", "json:target/cucumber.json"},
        tags = "not @ignore",
        strict = true,
        monochrome = true,
        dryRun = false)
public class PaymentsServiceAcceptanceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentsServiceAcceptanceTest.class);

    private static final EmbeddedPostgres EMBEDDED_POSTGRES = new EmbeddedPostgres(V9_6);

    static {

        try {

            EMBEDDED_POSTGRES.start("localhost", 15432, "payments-service", "postgres", "postgres");

        } catch (IOException e) {

            throw new IllegalStateException("Unable to initialize Postgres", e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {

                LOGGER.info("Terminating the Payments Service Acceptance Tests...");

                EMBEDDED_POSTGRES.stop();

                LOGGER.info("The Payments Service Acceptance Tests have terminated.");
            }
        });
    }

    private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("conf.yml");

    @ClassRule
    public static final DropwizardAppRule<PaymentsServiceConfiguration> DROPWIZARD_APP_RULE =
            new DropwizardAppRule<>(PaymentsService.class, CONFIG_PATH,
                    ConfigOverride.config("server.applicationConnectors[0].port", "0"),
                    ConfigOverride.config("server.adminConnectors[0].port", "0"),
                    ConfigOverride.config("database.url", "jdbc:postgresql://localhost:15432/payments-service"));

    public static final HashMap<String, String> GLOBAL_PARAMS = new HashMap<>();

    public static DBI dbi;
    public static Handle handle;
    public static String baseUrl;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        dbi = (new DBIFactory()).build(DROPWIZARD_APP_RULE.getEnvironment(),
                DROPWIZARD_APP_RULE.getConfiguration().getDataSourceFactory(), PaymentsServiceAcceptanceTest.class.getSimpleName());

        handle = dbi.open();

        baseUrl = "http://localhost:" + DROPWIZARD_APP_RULE.getLocalPort() + "/api";

        DROPWIZARD_APP_RULE.getApplication().run("db", "drop-all", "--confirm-delete-everything", CONFIG_PATH);
        DROPWIZARD_APP_RULE.getApplication().run("db", "migrate", CONFIG_PATH);

        GLOBAL_PARAMS.put("baseUrl", baseUrl);
    }

    @AfterClass
    public static void tearDownAfterClass() {

        if (handle != null) {

            handle.close();
        }

        EMBEDDED_POSTGRES.stop();

        (new ReportBuilder(Lists.newArrayList("target/cucumber.json"),
                new Configuration(new File("target"), "Payments Service"))).generateReports();
    }
}
