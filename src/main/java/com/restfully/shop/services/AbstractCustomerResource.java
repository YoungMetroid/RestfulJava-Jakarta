package com.restfully.shop.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restfully.shop.api.DirectoryPaths;
import com.restfully.shop.domain.Customer;

import io.vavr.Tuple2;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;


import java.io.*;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractCustomerResource {

    private Map<Integer, Customer> customerDB=
            new ConcurrentHashMap<Integer,Customer>();
    private AtomicInteger idCounter = new AtomicInteger();

    protected  DirectoryPaths directoryPaths;
    public AbstractCustomerResource()
    {
        try
        {
            directoryPaths = DirectoryPaths.getInstance();
        }
        catch (UnsupportedEncodingException ex)
        {
            ex.printStackTrace();
        }
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response createCustomer(InputStream is)
    {
        System.out.println(is.toString());
        Tuple2<Customer, Boolean> tuple2 = readCustomer(is);
        Customer customer = (Customer) tuple2._1();
        boolean isJson = tuple2._2();

        customer.setId(idCounter.incrementAndGet());
        customerDB.put(customer.getId(),customer);
        ObjectMapper Obj = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString =  Obj.writeValueAsString(customer);
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
        System.out.println("Created customer " + customer.getId());

        if(isJson)
        return Response.created(URI.create("customers/" + customer.getId())).
                entity(jsonString).
                type(MediaType.APPLICATION_JSON).build();

        return Response.created(URI.create("customers/" + customer.getId())).
                entity(jsonString).
                type(MediaType.APPLICATION_XML).build();


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

    @GET
    @Path("/GetResourcePath")
    public Response getResourcePath()
    {
        return Response.status(200).entity(directoryPaths.getRootPath()).build();
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml","application/json"})
    public void updateCustomer(@jakarta.ws.rs.PathParam("id")int id, InputStream is)
    {

        Tuple2<Customer,Boolean> tuple2 = readCustomer(is);
        Customer update = tuple2._1();
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

    abstract protected Tuple2<Customer,Boolean> readCustomer(InputStream is);

}
