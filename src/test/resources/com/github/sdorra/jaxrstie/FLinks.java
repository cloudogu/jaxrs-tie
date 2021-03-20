package com.example;

import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class FLinks {

    private final UriInfo uriInfo;

    public FLinks(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    public ResourceWithValidationLinks resourceWithValidation() {
        return new ResourceWithValidationLinks(uriInfo.getBaseUriBuilder().path(com.example.ResourceWithValidation.class));
    }

    public static class ResourceWithValidationLinks {
        private static final Class<?> clazz = com.example.ResourceWithValidation.class;

        private final UriBuilder builder;

        private ResourceWithValidationLinks(UriBuilder builder) {
            this.builder = builder;
        }

        public URI create(String id) {
            return builder.path(com.example.ResourceWithValidation.class, "create").build( id );
        }

    }


}
