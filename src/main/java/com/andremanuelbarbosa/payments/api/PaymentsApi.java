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
import javax.ws.rs.core.Response;
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

    @DELETE
    @Path("/{id}")
    @ApiOperation("Delete a Payment Resource")
    public Response deletePayment(@PathParam("id") @ApiParam("The ID of the Payment") UUID id) {

        if (paymentsManager.getPayment(id) == null) {

            return Response.status(Response.Status.NOT_FOUND).build();
        }

        paymentsManager.deletePayment(id);

        return Response.noContent().build();
    }

    @GET
    @Path("/{id}")
    @ApiOperation("Fetch a Payment Resource")
    public Response getPayment(@PathParam("id") @ApiParam("The ID of the Payment") UUID id) {

        if (paymentsManager.getPayment(id) == null) {

            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(paymentsManager.getPayment(id)).build();
    }

    @GET
    @ApiOperation("List a Collection of Payment Resources")
    public Resources<Payment> getPayments(@Context UriInfo uriInfo) {

        return new Resources<>(paymentsManager.getPayments(), new Resources.Links(uriInfo.getAbsolutePath().toString()));
    }

    @POST
    @ApiOperation("Create a Payment Resource")
    public Response insertPayment(@ApiParam("The Payment Resource") Payment payment) {

        if (paymentsManager.getPayment(payment.getId()) != null) {

            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.status(Response.Status.CREATED).entity(paymentsManager.insertPayment(payment)).build();
    }

    @PUT
    @Path("/{id}")
    @ApiOperation("Update a Payment Resource")
    public Response updatePayment(@PathParam("id") @ApiParam("The ID of the Payment") UUID id, @ApiParam("The Payment Resource") Payment payment) {

        if (paymentsManager.getPayment(id) == null) {

            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!id.equals(payment.getId())) {

            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.ok(paymentsManager.updatePayment(payment)).build();
    }
}
