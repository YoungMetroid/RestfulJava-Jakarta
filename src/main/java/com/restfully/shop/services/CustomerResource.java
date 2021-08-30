


package com.restfully.shop.services;


import com.restfully.shop.domain.Customer;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.mortbay.jetty.HttpParser;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
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
/*
    @Override
    protected Customer readCustomer(InputStream is)
    {
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
*/
    @Override
    protected Customer readCustomer(InputStream is)
    {

        try
        {
            byte []inputArray = is.readAllBytes();
            if(JsonToObject.isJson(inputArray))
            {
                Customer customer =  (Customer)JsonToObject.convertJsonToObject(inputArray,Customer.class, directoryPaths.getSchemasPath()+directoryPaths.CUSTOMERJSON);
                if(customer == null)
                {
                    throw new WebApplicationException("Unable to convert the InputStream to a Customer object, returned object is null",Response.Status.BAD_REQUEST);
                }
                return customer;
            }


            JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema customerSchema =  schemaFactory.newSchema(new File(directoryPaths.getSchemasPath()+directoryPaths.CUSTOMERXSD));
            unmarshaller.setSchema(customerSchema);

            InputStream targetStream = new ByteArrayInputStream(inputArray);
            return (Customer) unmarshaller.unmarshal(targetStream);

        }
        catch (IOException exception)
        {
            System.out.println("Json Error");
            throw new WebApplicationException(exception, Response.Status.BAD_REQUEST);
        }
        catch (JAXBException | SAXException exception)
        {
            String message = "";
            if(exception.getClass() == JAXBException.class)
            {
                message = "Exception of type JAXB";
            }
            else
            {
                message = "Exception of type SAX";
            }
            throw new WebApplicationException(message,exception, Response.Status.BAD_REQUEST);
        }

    }
}


