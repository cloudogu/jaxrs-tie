package com.example;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.validation.constraints.Pattern;

@Path("validation")
public class ResourceWithValidation {

  @POST
  @Path("{id}")
  public String create(@Pattern(regexp = "\\w{1,10}") @PathParam("id") String id) {
    return "";
  }

}
