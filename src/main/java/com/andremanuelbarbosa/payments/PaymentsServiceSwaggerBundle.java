package com.andremanuelbarbosa.payments;

import com.andremanuelbarbosa.payments.api.AbstractApi;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.*;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

import javax.ws.rs.Path;

public class PaymentsServiceSwaggerBundle extends SwaggerBundle<PaymentsServiceConfiguration> {

    private static final String SWAGGER_RESOURCES_PATH = "/swagger-static";
    private static final String SWAGGER_ASSETS_NAME = "swagger-assets";

    @Override
    public void run(PaymentsServiceConfiguration paymentsServiceConfiguration, Environment environment) {

        final SwaggerBundleConfiguration swaggerBundleConfiguration = getSwaggerBundleConfiguration(paymentsServiceConfiguration);

        final ConfigurationHelper configurationHelper = new ConfigurationHelper(paymentsServiceConfiguration, swaggerBundleConfiguration);

        new AssetsBundle(SWAGGER_RESOURCES_PATH, configurationHelper.getSwaggerUriPath(), null, SWAGGER_ASSETS_NAME).run(environment);

        environment.jersey().register(new SwaggerApi(configurationHelper.getUrlPattern()));

        configureSwagger(swaggerBundleConfiguration, configurationHelper.getUrlPattern());

        environment.jersey().register(ApiListingResource.class);
        environment.jersey().register(SwaggerSerializers.class);
    }

    @Override
    protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(PaymentsServiceConfiguration paymentsServiceConfiguration) {

        final SwaggerBundleConfiguration swaggerBundleConfiguration = new SwaggerBundleConfiguration();

        swaggerBundleConfiguration.setResourcePackage(AbstractApi.class.getPackage().getName());

        return swaggerBundleConfiguration;
    }

    private void configureSwagger(SwaggerBundleConfiguration swaggerBundleConfiguration, String urlPattern) {

        final BeanConfig beanConfig = new BeanConfig();

        beanConfig.setTitle(swaggerBundleConfiguration.getTitle());
        beanConfig.setVersion(swaggerBundleConfiguration.getVersion());
        beanConfig.setDescription(swaggerBundleConfiguration.getDescription());
        beanConfig.setContact(swaggerBundleConfiguration.getContact());
        beanConfig.setLicense(swaggerBundleConfiguration.getLicense());
        beanConfig.setLicenseUrl(swaggerBundleConfiguration.getLicenseUrl());
        beanConfig.setTermsOfServiceUrl(swaggerBundleConfiguration.getTermsOfServiceUrl());

        beanConfig.setBasePath(urlPattern);
        beanConfig.setResourcePackage(swaggerBundleConfiguration.getResourcePackage());
        beanConfig.setScan(true);
    }

    @Path("/")
    private final static class SwaggerApi extends SwaggerResource {

        private static final SwaggerViewConfiguration SWAGGER_VIEW_CONFIGURATION = new SwaggerViewConfiguration();
        private static final SwaggerOAuth2Configuration SWAGGER_OAUTH2_CONFIGURATION = new SwaggerOAuth2Configuration();

        public SwaggerApi(String urlPattern) {

            super(urlPattern, SWAGGER_VIEW_CONFIGURATION, SWAGGER_OAUTH2_CONFIGURATION);
        }
    }
}
