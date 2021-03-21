package com.github.sdorra.jaxrstie;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import java.util.Set;

public final class Resources {

  private Resources() {
  }

  public static void append(ProcessingEnvironment processingEnv, Resource resource, Element element) {
    Set<Methods.MethodWrapper> methods = Methods.findJaxRsMethods(processingEnv, element);

    for (Methods.MethodWrapper method : methods) {
      if (method.isEndpoint()) {
        resource.addEndpoint(Endpoint.of(method.getMethod(), resource));
      } else {
        resource.addSubResource(SubResource.from(processingEnv, method.getMethod(), resource));
      }
    }
  }
}
