package com.andremanuelbarbosa.payments;

import com.andremanuelbarbosa.payments.api.AbstractApi;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaymentsService extends Application<PaymentsServiceConfiguration> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentsService.class);

    private final PaymentsServiceSwaggerBundle paymentsServiceSwaggerBundle = new PaymentsServiceSwaggerBundle();

    private void addShutdownHook() {

        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {

                LOGGER.info("Terminating the Payments Service...");

                LOGGER.info("The Payments Service has terminated.");
            }
        });
    }

    @Override
    public void initialize(Bootstrap<PaymentsServiceConfiguration> bootstrap) {

        bootstrap.addBundle(new MigrationsBundle<PaymentsServiceConfiguration>() {

            @Override
            public DataSourceFactory getDataSourceFactory(PaymentsServiceConfiguration paymentsServiceConfiguration) {

                return paymentsServiceConfiguration.getDataSourceFactory();
            }
        });

        bootstrap.addBundle(paymentsServiceSwaggerBundle);

        bootstrap.getObjectMapper().registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
    }

    @Override
    public void run(PaymentsServiceConfiguration paymentsServiceConfiguration, Environment environment) throws Exception {

        addShutdownHook();

        final Injector injector = Guice.createInjector(new PaymentsServiceModule(environment, paymentsServiceConfiguration));

        (new Reflections(AbstractApi.class.getPackage().getName())).getSubTypesOf(AbstractApi.class).stream().forEach(apiClass -> {

            environment.jersey().register(injector.getInstance(apiClass));
        });

        environment.getObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    public static void main(String[] args) throws Exception {

        LOGGER.info("Starting the Payments Service...");

        new PaymentsService().run(args);

        LOGGER.info("The Payments Service has started.");
    }
}
