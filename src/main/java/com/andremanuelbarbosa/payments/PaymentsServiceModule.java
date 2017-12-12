package com.andremanuelbarbosa.payments;

import com.andremanuelbarbosa.payments.dao.Dao;
import com.google.inject.Binder;
import com.google.inject.Module;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaymentsServiceModule implements Module {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentsServiceModule.class);

    private static final SubTypesScanner SUB_TYPES_SCANNER_INCLUDE_OBJECT_CLASS = new SubTypesScanner(false);

    private final DBI dbi;

    public PaymentsServiceModule(Environment environment, PaymentsServiceConfiguration paymentsServiceConfiguration) {

        dbi = (new DBIFactory()).build(environment, paymentsServiceConfiguration.getDataSourceFactory(), PaymentsService.class.getSimpleName());
    }

    @Override
    public void configure(Binder binder) {

        (new Reflections(Dao.class.getPackage().getName(), SUB_TYPES_SCANNER_INCLUDE_OBJECT_CLASS)).getSubTypesOf(Dao.class).stream().forEach(daoClass -> {

            if (daoClass.getInterfaces().length > 0 && daoClass.getInterfaces()[0] == Dao.class) {

                try {

                    final Class<Dao> daoBindClass = (Class<Dao>) Class.forName(daoClass.getName().replace(".dao.", ".dao.jdbi.") + "Jdbi");

                    binder.bind((Class<Dao>) daoClass).toInstance(dbi.onDemand(daoBindClass));

                    LOGGER.debug("The DAO Class [{}] has been bind to [{}].", daoClass.getName(), daoBindClass.getName());

                } catch (ClassNotFoundException e) {

                    LOGGER.error("Unable to load/bind the DAO Class [{}]: {}.", daoClass.getName(), e.getMessage());

                    throw new IllegalStateException(e);
                }
            }
        });
    }
}
