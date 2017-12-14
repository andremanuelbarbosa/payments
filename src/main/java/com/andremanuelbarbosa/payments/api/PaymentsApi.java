package com.andremanuelbarbosa.payments.api;

import com.andremanuelbarbosa.payments.domain.Payment;
import com.andremanuelbarbosa.payments.manager.PaymentsManager;
import com.andremanuelbarbosa.payments.resources.Resources;
import com.google.inject.Inject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.UUID;

@Singleton
@Api("Payments")
@Path("/payments")
public class PaymentsApi extends AbstractApi {

    private final PaymentsManager paymentsManager;

    @Inject
    public PaymentsApi(PaymentsManager paymentsManager) {

        this.paymentsManager = paymentsManager;
    }

    @POST
    @ApiOperation("Create a Payment Resource")
    public Payment createPayment(@ApiParam("The Payment Resource") Payment payment) {

        // TODO
        return null;
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation("Delete a Payment Resource")
    public void deletePayment(@PathParam("id") @ApiParam("The ID of the Payment") UUID id) {

        // TODO
    }

    @GET
    @Path("/{id}")
    @ApiOperation("Fetch a Payment Resource")
    public Payment getPayment(@PathParam("id") @ApiParam("The ID of the Payment") UUID id) {

        return paymentsManager.getPayment(id);
    }

    @GET
    @ApiOperation("List a Collection of Payment Resources")
    public Resources<Payment> getPayments(@Context UriInfo uriInfo) {

        return new Resources<>(paymentsManager.getPayments(), new Resources.Links(uriInfo.getAbsolutePath().toString()));
    }

    @PUT
    @Path("/{id}")
    @ApiOperation("Update a Payment Resource")
    public Payment updatePayment(@PathParam("id") @ApiParam("The ID of the Payment") UUID id, @ApiParam("The Payment Resource") Payment payment) {

        // TODO
        return null;
    }
}
