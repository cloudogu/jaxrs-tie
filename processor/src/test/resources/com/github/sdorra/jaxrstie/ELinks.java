package com.example;

import java.net.URI;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

public final class ELinks {

    private final UriInfo uriInfo;

    public ELinks(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    public SubResourceWithParamLinks subResourceWithParam() {
        return new SubResourceWithParamLinks(uriInfo.getBaseUriBuilder().path(SubResourceWithParam.class));
    }

    public static class SubResourceWithParamLinks {

        private final UriBuilder builder;

        private SubResourceWithParamLinks(UriBuilder builder) {
            this.builder = builder;
        }

        public GroupLinks group(String id) {
            return new GroupLinks(builder.path(SubResourceWithParam.class, "group"), id);
        }

        public static class GroupLinks {

            private final UriBuilder builder;
            private final String arg0;

            private GroupLinks(UriBuilder builder, String arg0) {
                this.builder = builder;
                this.arg0 = arg0;
            }

            public URI get(String id) {
                return builder.path(SubResourceWithParam.ChildResource.class, "get").build( arg0, id );
            }

        }


    }


}
