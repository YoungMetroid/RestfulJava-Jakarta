package com.restfully.shop.services;

import com.restfully.shop.domain.Customer;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractCustomerResource {

    private Map<Integer, Customer> customerDB=
            new ConcurrentHashMap<Integer,Customer>();
    private AtomicInteger idCounter = new AtomicInteger();

    @POST
    @Consumes({"application/xml","application/json"})
    public Response createCustomer(InputStream is)
    {
        System.out.println(is.toString());
        if(JsonToObject.isJson(is))
        {

        }
        Customer customer = readCustomer(is);
        customer.setId(idCounter.incrementAndGet());
        customerDB.put(customer.getId(),customer);
        System.out.println("Created customer " + customer.getId());;
        return Response.created(URI.create("/customers/" + customer.getId())).build();
    }

    @GET
    @Path("{id}")
    @Produces("application/xml")
    public StreamingOutput getCustomer(@jakarta.ws.rs.PathParam("id")int id)
    {
        final Customer customer = customerDB.get(id);
        if(customer == null)
        {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return new StreamingOutput()
        {
            public void write(OutputStream outputStream) throws IOException, WebApplicationException
            {
                outputCustomer(outputStream,customer);
            }
        };
    }

    @GET
    @Path("/test")
    public Response getHelloWorld()
    {
        String result = "<h1>RESTful Demo Application</h1>In real world application, a collection of users will be returned !!";
        return Response.status(200).entity(result).build();
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml","application/json"})
    public void updateCustomer(@jakarta.ws.rs.PathParam("id")int id, InputStream is)
    {

        Customer update = readCustomer(is);
        Customer current = customerDB.get(id);
        if(current == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);

        current.setFirstName(update.getFirstName());
        current.setLastName(update.getLastName());
        current.setStreet(update.getStreet());
        current.setStreet(update.getState());
        current.setZip(update.getZip());
        current.setCountry(update.getCountry());
    }

    abstract protected void outputCustomer(OutputStream os,
                                           Customer cust) throws IOException;

    abstract protected Customer readCustomer(InputStream is);

}
