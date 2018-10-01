package com.example;

import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class ELinks {

    private final UriInfo uriInfo;

    public ELinks(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }


    public SubResourceWithParamLinks subResourceWithParam() {
        return new SubResourceWithParamLinks(uriInfo.getBaseUriBuilder().path(com.example.SubResourceWithParam.class));
    }

    public static class SubResourceWithParamLinks {

        private static final Class<?> clazz = com.example.SubResourceWithParam.class;

        private final UriBuilder builder;

        private SubResourceWithParamLinks(UriBuilder builder) {
            this.builder = builder;
        }

        public GroupLinks group(String id) {
            return new GroupLinks(builder.path(clazz, "group"), id);
        }

        public static class GroupLinks {

            private static final Class<?> clazz = com.example.SubResourceWithParam.ChildResource.class;

            private final UriBuilder builder;
            private final String arg0;

            private GroupLinks(UriBuilder builder, String arg0) {
                this.builder = builder;
                this.arg0 = arg0;
            }

            public URI get(String id) {
                return builder.path(com.example.SubResourceWithParam.ChildResource.class, "get").build( arg0, id );
            }

        }


    }


}
