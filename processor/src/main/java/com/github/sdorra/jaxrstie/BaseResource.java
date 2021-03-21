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

package com.github.sdorra.jaxrstie;

import java.util.ArrayList;
import java.util.List;

public class BaseResource {

  protected String name;
  protected MethodParameters parameters;
  protected BaseResource parent;

  public BaseResource(String name) {
    this.name = name;
    this.parameters = MethodParameters.createEmpty();
  }

  public String getName() {
    return name;
  }

  public MethodParameters getParameters() {
    return parameters;
  }

  public MethodParameters getAllParametersAnonymized() {
    List<MethodParameter> allParameters = new ArrayList<>();
    collectParameters(this, allParameters);
    return new MethodParameters(allParameters);
  }

  public MethodParameters getAllParameters() {
    List<MethodParameter> allParameters = new ArrayList<>();
    collectParameters(getParent(), allParameters);
    for (MethodParameter parameter : getParameters()) {
      allParameters.add(parameter);
    }
    return new MethodParameters(allParameters);
  }

  private void collectParameters(BaseResource resource, List<MethodParameter> parameters) {
    if (resource == null) {
      return;
    }
    BaseResource parentResource = resource.getParent();
    if (parentResource != null) {
      collectParameters(parentResource, parameters);
    }
    for (MethodParameter param : resource.getParameters()) {
      parameters.add(new MethodParameter("arg" + parameters.size(), param.getType()));
    }
  }

  public BaseResource getParent() {
    return parent;
  }

  public void setParameters(MethodParameters parameters) {
    this.parameters = parameters;
  }

  public void setParent(BaseResource parent) {
    this.parent = parent;
  }
}
