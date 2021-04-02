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

    public BuilderLink get() {
      URI uri = builder.path(SimpleResource.class, "get").build();
      return new BuilderLink(uri);
    }

    public BuilderLink delete(String id) {
      URI uri = builder.path(SimpleResource.class, "delete").build(id);
      return new BuilderLink(uri);
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