package com.restfully.shop.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restfully.shop.api.DirectoryPaths;
import com.restfully.shop.domain.Customer;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;

import javax.servlet.ServletContext;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
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
        Customer customer = readCustomer(is);
        System.out.println("Got Customer");
        System.out.println("Setting id");
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
        System.out.println("Created customer " + customer.getId());;
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
