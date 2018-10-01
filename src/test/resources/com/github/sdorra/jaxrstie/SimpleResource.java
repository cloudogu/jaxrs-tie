package com.example;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("simple")
public class SimpleResource {

    @GET
    public String get() {
        return "";
    }

    @DELETE
    @Path("{id}")
    public String delete(@PathParam("id") String id) {
        return "";
    }

}
