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

public final class FLinks {

  private final URI uri;

  public FLinks(UriInfo uriInfo) {
    this.uri = uriInfo.getBaseUri();
  }

  public FLinks(URI uri) {
    this.uri = uri;
  }

  public ResourceWithValidationLinks resourceWithValidation() {
    return new ResourceWithValidationLinks(UriBuilder.fromUri(uri).path(ResourceWithValidation.class));
  }

  public static class ResourceWithValidationLinks {

    private final UriBuilder builder;

    private ResourceWithValidationLinks(UriBuilder builder) {
      this.builder = builder;
    }

    public BuilderLink create(String id) {
      URI uri = builder.path(ResourceWithValidation.class, "create").build(id);
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
