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


    public static boolean isJson(byte[] inputArray)
    {
        try
        {
            String jsonString = new String(inputArray, StandardCharsets.UTF_8);
            String trimmedJsonString = jsonString.trim();
            if(trimmedJsonString.startsWith("{"))
            {
                System.out.println("Is json");
                return true;
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return false;
    }
    public static Object convertJsonToObject(byte[] inputArray, Class<?> objectClass, String schemaPath)   {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909);
            File schemaFile = new File(schemaPath);
            InputStream schemaStream = new FileInputStream(schemaFile);
            JsonNode jsonNode = objectMapper.readTree(inputArray);
            JsonSchema schema = schemaFactory.getSchema(schemaStream);
            if(jsonNode == null)
            {
                System.out.println("JsonNode is null");
            }
            if(schema == null)
            {
                System.out.println("Schema is null");
            }
            Set<ValidationMessage> validationResult = schema.validate(jsonNode);

            if (validationResult.isEmpty())
            {
                Object obj = objectMapper.readValue(inputArray, objectClass);
                System.out.println(obj.toString());
                return obj;
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        catch (Exception exception)
        {
            System.out.println("Unknown exception: " + exception.getMessage());
            exception.printStackTrace();
        }

        System.out.println("ValidationResult is not empty");
        return null;
    }

}
