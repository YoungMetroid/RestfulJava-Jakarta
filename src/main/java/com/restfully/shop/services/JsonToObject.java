package com.restfully.shop.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class JsonToObject {


    public static boolean isJson(InputStream inputStream)
    {
        try
        {
            String jsonString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            String trimmedJsonString = jsonString.trim();
            if(trimmedJsonString.startsWith("{"))
            {
                return true;
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return false;
    }
    public static Object convertJsonToObject(InputStream inputStream, Class<?> objectClass, String schemaPath)
            throws IOException, ClassNotFoundException
    {
        String jsonString = new String(inputStream.readAllBytes(),StandardCharsets.UTF_8);
        InputStream newStream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909);

        File schemaFile = new File(schemaPath);
        InputStream schemaStream = new FileInputStream(schemaFile);

        JsonNode jsonNode = objectMapper.readTree(newStream);
        JsonSchema schema = schemaFactory.getSchema(schemaStream);
        Set<ValidationMessage> validationResult = schema.validate(jsonNode);

        if (validationResult.isEmpty())
        {
            Class<?> cls = Class.forName(objectClass.getName());
            Object obj = objectMapper.readValue(jsonString,cls);
            System.out.println(obj.toString());
            return obj;
        }

        return null;
    }

}
