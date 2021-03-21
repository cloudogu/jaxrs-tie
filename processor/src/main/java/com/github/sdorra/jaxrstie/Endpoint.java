package com.github.sdorra.jaxrstie;

import javax.lang.model.element.ExecutableElement;
import java.util.Objects;

public class Endpoint extends BaseResource {

  public Endpoint(String name, MethodParameters parameters, BaseResource parent) {
    super(name);
    this.parameters = parameters;
    this.parent = parent;
  }

  @Override
  public String toString() {
    return name + "(" + parameters + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Endpoint endpoint = (Endpoint) o;
    return Objects.equals(name, endpoint.name) &&
      Objects.equals(parameters, endpoint.parameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, parameters);
  }

  public static Endpoint of(ExecutableElement method, BaseResource parent) {
    String name = method.getSimpleName().toString();
    return new Endpoint(name, Methods.findPathParams(method), parent);
  }

}
