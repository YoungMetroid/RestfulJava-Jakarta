package com.restfully.shop.api;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class DirectoryPaths
{
    private static DirectoryPaths directoryPathInstance = null;

    private String rootPath;
    public final String CUSTOMERJSON= "jsonSchemas/customer.json";
    public final String CUSTOMERXSD= "xmlSchemas/customer.xsd";


    private DirectoryPaths() throws UnsupportedEncodingException {
        String encodedPath="";
        try
        {
            encodedPath = DirectoryPaths.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            rootPath = URLDecoder.decode(encodedPath, StandardCharsets.UTF_8);

        }
        catch (Throwable ex)
        {
            throw new UnsupportedEncodingException("Unable to decode the following encoded path:" + encodedPath + "\n" + ex.getLocalizedMessage());
        }
    }

    public static DirectoryPaths getInstance() throws UnsupportedEncodingException
    {
        if(directoryPathInstance == null)
        {
            directoryPathInstance = new DirectoryPaths();
        }
        return directoryPathInstance;
    }

    public String getRootPath()
    {
        return this.rootPath;
    }
    public String getSchemasPath()
    {
        return this.rootPath +  "/schemas/" ;
    }
}
