package com.restfully.shop.domain;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "customer")
@XmlAccessorType(XmlAccessType.FIELD)
public class Customer
{

    @XmlAttribute
    private int id;
    @XmlElement
    private String firstName;
    @XmlElement
    private String lastName;
    @XmlElement
    private String street;
    @XmlElement
    private String city;
    @XmlElement
    private String state;
    @XmlElement
    private String zip;
    @XmlElement
    private String country;



    public int getId(){return id;}
    public void setId(int id){this.id = id;}

    public String getFirstName(){return firstName;}
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName(){return lastName;}
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getStreet(){return street;}
    public void setStreet(String street) { this.street = street; }

    public String getCity(){return city;}
    public void setCity(String city) { this.city = city; }

    public String getState(){return state;}
    public void setState(String state){this.state = state;}

    public String getZip(){return zip;}
    public void setZip(String zip){this.zip = zip;}

    public String getCountry(){return country;}
    public void setCountry(String country){this.country = country;}
}
