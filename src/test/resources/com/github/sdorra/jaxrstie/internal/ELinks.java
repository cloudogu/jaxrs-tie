/*
 * MIT License
 *
 * Copyright (c) 2021, Sebastian Sdorra
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.example;

import java.lang.String;
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

      public BuilderLink get(String id) {
        URI uri = builder.path(SubResourceWithParam.ChildResource.class, "get").build(arg0, id);
        return new BuilderLink(uri);
      }

    }

  }

  public static class BuilderLink {

    private final URI uri;

    private BuilderLink(URI uri) {
      this.uri = uri;
    }

    public URI asUri() {
      return uri;
    }

    public String asString() {
      return uri.toASCIIString();
    }
  }

}
