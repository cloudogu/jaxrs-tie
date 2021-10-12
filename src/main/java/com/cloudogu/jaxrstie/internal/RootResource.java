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

package com.cloudogu.jaxrstie.internal;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

public class RootResource extends Resource {

  public RootResource(String type, String name) {
    super(type, name);
  }

  @Override
  public String toString() {
    StringBuilder buffer = new StringBuilder(name).append("\n");
    appendEndpoints(buffer, " - ", this);
    appendSubResources(buffer, " - ", this);
    return buffer.toString();
  }

  private void appendEndpoints(StringBuilder buffer, String prefix, Resource resource) {
    for (Endpoint endpoint : resource.endpoints) {
      buffer.append(prefix).append(endpoint).append("\n");
    }
  }

  private void appendSubResources(StringBuilder buffer, String prefix, Resource resource) {
    for (SubResource subResource : resource.subResources) {
      buffer.append(prefix).append(subResource).append("\n");
      String newPrefix = "   ".concat(prefix);
      appendEndpoints(buffer, newPrefix, subResource);
      appendSubResources(buffer, newPrefix, subResource);
    }
  }

  public static RootResource from(ProcessingEnvironment processingEnv, Element element) {
    RootResource rootResource = new RootResource(element.toString(), Names.of(element));
    Resources.append(processingEnv, rootResource, element);
    return rootResource;
  }


}
