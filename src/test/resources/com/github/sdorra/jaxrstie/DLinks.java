package com.example;

import java.net.URI;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

public final class DLinks {

    private final UriInfo uriInfo;

    public DLinks(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }


    public SubLinks sub() {
        return new SubLinks(uriInfo.getBaseUriBuilder().path(SubResource.class));
    }

    public static class SubLinks {

        private final UriBuilder builder;

        private SubLinks(UriBuilder builder) {
            this.builder = builder;
        }


        public FindLinks find() {
            return new FindLinks(builder.path(SubResource.class, "find"));
        }

        public static class FindLinks {

            private final UriBuilder builder;

            private FindLinks(UriBuilder builder) {
                this.builder = builder;
            }

            public URI get(String id) {
                return builder.path(SubResource.ChildResource.class, "get").build( id );
            }

        }


    }


}
