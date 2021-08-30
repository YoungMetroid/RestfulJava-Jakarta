package com.restfully.shop.services;


import com.restfully.shop.api.DirectoryPaths;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/services")
public class ShoppingApplication extends Application {


    private Set<Object> singletons = new HashSet<Object>();
    private Set<Class<?>> empty = new HashSet<Class<?>>();

    public ShoppingApplication()
    {
        try
        {
            DirectoryPaths directoryPaths = DirectoryPaths.getInstance();
        }
        catch (UnsupportedEncodingException ex)
        {
            ex.printStackTrace();
        }
        singletons.add(new CustomerResource());
    }

    @Override
    public Set<Class<?>> getClasses() {
        return empty;
    }

    @Override
    public Set<Object> getSingletons()
    {
        return singletons;
    }
}
