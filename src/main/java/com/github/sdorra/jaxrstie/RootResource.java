package com.github.sdorra.jaxrstie;

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
