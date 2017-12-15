package com.andremanuelbarbosa.payments;

import com.andremanuelbarbosa.payments.domain.Payment;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.assertj.core.util.Lists;
import org.joda.time.DateTime;
import org.junit.*;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import static ru.yandex.qatools.embed.postgresql.distribution.Version.Main.V9_6;

public abstract class PaymentsServiceIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentsServiceIntegrationTest.class);

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
    }

    public static void cleanUp() {

        handle.update("DELETE FROM payments_sender_charges");
        handle.update("DELETE FROM payments");
    }

    @Before
    public void setUp() {

        cleanUp();
    }

    @After
    public void tearDown() {

    }
}
