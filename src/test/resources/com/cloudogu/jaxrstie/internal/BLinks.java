/*
 * MIT License
 *
 * Copyright (c) 2021, Cloudogu GmbH
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

import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import java.lang.String;
import java.net.URI;

public final class BLinks {

  private final URI uri;

  public BLinks(UriInfo uriInfo) {
    this.uri = uriInfo.getBaseUri();
  }

  public BLinks(URI uri) {
    this.uri = uri;
  }

  public SimpleLinks simple() {
    return new SimpleLinks(UriBuilder.fromUri(uri).path(SimpleResource.class));
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
