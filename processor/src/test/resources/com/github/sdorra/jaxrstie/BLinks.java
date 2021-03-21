package com.example;

import java.net.URI;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

public final class BLinks {

    private final UriInfo uriInfo;

    public BLinks(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    public SimpleLinks simple() {
        return new SimpleLinks(uriInfo.getBaseUriBuilder().path(SimpleResource.class));
    }

    public static class SimpleLinks {
        private final UriBuilder builder;

        private SimpleLinks(UriBuilder builder) {
            this.builder = builder;
        }

        public URI get() {
            return builder.path(SimpleResource.class, "get").build(  );
        }

        public URI delete(String id) {
            return builder.path(SimpleResource.class, "delete").build( id );
        }

    }


}
