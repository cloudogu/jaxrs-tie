package com.example;

import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class DLinks {

    private final UriInfo uriInfo;

    public DLinks(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }


    public SubLinks sub() {
        return new SubLinks(uriInfo.getBaseUriBuilder().path(com.example.SubResource.class));
    }

    public static class SubLinks {

        private static final Class<?> clazz = com.example.SubResource.class;

        private final UriBuilder builder;

        private SubLinks(UriBuilder builder) {
            this.builder = builder;
        }


        public FindLinks find() {
            return new FindLinks(builder.path(clazz, "find"));
        }

        public static class FindLinks {

            private static final Class<?> clazz = com.example.SubResource.ChildResource.class;

            private final UriBuilder builder;

            private FindLinks(UriBuilder builder) {
                this.builder = builder;
            }

            public URI get(String id) {
                return builder.path(com.example.SubResource.ChildResource.class, "get").build( id );
            }

        }


    }


}
