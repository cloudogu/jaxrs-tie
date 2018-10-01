package com.example;

import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class BLinks {

    private final UriInfo uriInfo;

    public BLinks(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    public SimpleLinks simple() {
        return new SimpleLinks(uriInfo.getBaseUriBuilder().path(com.example.SimpleResource.class));
    }

    public static class SimpleLinks {
        private static final Class<?> clazz = com.example.SimpleResource.class;

        private final UriBuilder builder;

        private SimpleLinks(UriBuilder builder) {
            this.builder = builder;
        }

        public URI get() {
            return builder.path(com.example.SimpleResource.class, "get").build(  );
        }

        public URI delete(String id) {
            return builder.path(com.example.SimpleResource.class, "delete").build( id );
        }

    }


}
