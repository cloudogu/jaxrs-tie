package com.example;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("sub")
public class SubResourceWithParam {

    @Path("{id}")
    public ChildResource group(@PathParam("id") String id) {
        return new ChildResource();
    }

    public static class ChildResource {

        @GET
        @Path("{id}")
        public String get(@PathParam("id") String id) {
            return "";
        }

    }

}
