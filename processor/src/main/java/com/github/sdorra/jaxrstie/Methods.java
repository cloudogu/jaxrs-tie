package com.github.sdorra.jaxrstie;

import com.google.auto.common.MoreElements;
import com.google.auto.common.Visibility;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class Methods {

  private Methods() {
  }

  public static MethodParameters findPathParams(ExecutableElement method) {
    List<MethodParameter> parameters = method.getParameters()
      .stream()
      .filter(Methods::isPathParam)
      .map(MethodParameter::of)
      .collect(Collectors.toList());
    return new MethodParameters(parameters);
  }

  private static boolean isPathParam(VariableElement variableElement) {
    return variableElement.getAnnotation(PathParam.class) != null;
  }

  @SuppressWarnings("UnstableApiUsage")
  public static Set<MethodWrapper> findJaxRsMethods(ProcessingEnvironment processingEnv, Element element) {
    TypeElement typeElement = MoreElements.asType(element);
    return MoreElements.getLocalAndInheritedMethods(typeElement, processingEnv.getTypeUtils(), processingEnv.getElementUtils())
      .stream()
      .map(MethodWrapper::new)
      .filter(MethodWrapper::isPublic)
      .filter(MethodWrapper::isNotAbstract)
      .filter(MethodWrapper::isJaxRsMethod)
      .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  public static class MethodWrapper {

    private final ExecutableElement method;

    public MethodWrapper(ExecutableElement method) {
      this.method = method;
    }

    public ExecutableElement getMethod() {
      return method;
    }

    @SuppressWarnings("UnstableApiUsage")
    public boolean isSubResource() {
      return MoreElements.isAnnotationPresent(method, Path.class);
    }

    public boolean isPublic() {
      return Visibility.ofElement(method) == Visibility.PUBLIC;
    }

    @SuppressWarnings("UnstableApiUsage")
    public boolean isNotAbstract() {
      return !MoreElements.hasModifiers(Modifier.ABSTRACT).apply(method);
    }

    public boolean isEndpoint() {
      for (AnnotationMirror annotationMirror : method.getAnnotationMirrors()) {
        if (Annotations.isTypeOf(annotationMirror, HttpMethod.class)) {
          return true;
        }

        Element element = annotationMirror.getAnnotationType().asElement();
        if (element.getAnnotation(HttpMethod.class) != null) {
          return true;
        }
      }
      return false;
    }

    public boolean isJaxRsMethod() {
      return isSubResource() || isEndpoint();
    }
  }

}
