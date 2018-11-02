package com.example.danilius.phoneapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

    @Path("/jsonServices")
    public class RestService {
    List <PhoneBook> listPhoneBook;
        @GET
        @Path("/getphonebooks/{gson}")
        @Produces(MediaType.APPLICATION_JSON)
        public List<PhoneBook> GetPhoneBooks(@PathParam("gson") String gson) {
            StringBuilder str = new StringBuilder(gson);
            str.delete(0, 11);
            gson = str.toString();
            Type type = new TypeToken<List<PhoneBook>>() {}.getType();
            listPhoneBook = new Gson().fromJson(gson, type);
            return listPhoneBook;
        }

        @POST
        @Path("/postphonebooks")
        @Consumes(MediaType.APPLICATION_JSON)
        public Response PostPhoneBooks(List <PhoneBook> listPhoneBook) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(listPhoneBook);
            return Response.status(200).entity(json).build();
        }
    }