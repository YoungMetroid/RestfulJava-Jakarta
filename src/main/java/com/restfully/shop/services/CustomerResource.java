


package com.restfully.shop.services;


import com.restfully.shop.domain.Customer;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import java.io.*;

@Path("/customers")
public class CustomerResource extends AbstractCustomerResource{

    @Override
    protected void outputCustomer(OutputStream os, Customer customer) throws IOException {
        PrintStream writer = new PrintStream(os);
        writer.println("<customer id=\"" + customer.getId() + "\">");
        writer.println("<first-name>" + customer.getFirstName() + "</first-name>");
        writer.println("<last-name>" + customer.getLastName() + "</last-name>");
        writer.println("<street>" + customer.getStreet() + "</street>");
        writer.println("<city>" + customer.getCity() + "</city>");
        writer.println("<state>" + customer.getState() + "</state>");
        writer.println("<zip>" + customer.getZip() + "</zip>");
        writer.println("<country>" + customer.getCountry() + "</country>");
        writer.println("</customer>");
    }

    @Override
    protected Customer readCustomer(InputStream is) {
        try{
            JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Customer customer = (Customer) unmarshaller.unmarshal(is);
            System.out.println(customer);

            return customer;

        }
        catch (Exception exception)
        {
            throw new WebApplicationException(exception, Response.Status.BAD_REQUEST);
        }
    }
}


