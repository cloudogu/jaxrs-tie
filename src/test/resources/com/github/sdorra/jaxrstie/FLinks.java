package com.example;

import java.net.URI;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

public final class FLinks {

    private final UriInfo uriInfo;

    public FLinks(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    public ResourceWithValidationLinks resourceWithValidation() {
        return new ResourceWithValidationLinks(uriInfo.getBaseUriBuilder().path(ResourceWithValidation.class));
    }

    public static class ResourceWithValidationLinks {

        private final UriBuilder builder;

        private ResourceWithValidationLinks(UriBuilder builder) {
            this.builder = builder;
        }

        public URI create(String id) {
            return builder.path(ResourceWithValidation.class, "create").build( id );
        }

    }


}
