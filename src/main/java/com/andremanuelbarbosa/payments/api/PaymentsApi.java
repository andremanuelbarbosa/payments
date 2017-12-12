package com.andremanuelbarbosa.payments.api;

import com.andremanuelbarbosa.payments.domain.Payment;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@Singleton
@Api("Payments")
@Path("/payments")
public class PaymentsApi extends AbstractApi {

    @GET
    @Path("/{id}")
    @ApiOperation("Fetch a Payment Resource")
    public Payment getPayment(@PathParam("id") String id) {

        // TODO
        return null;
    }

    @GET
    @ApiOperation("List a Collection of Payment Resources")
    public List<Payment> getPayments() {

        // TODO
        return null;
    }
}
