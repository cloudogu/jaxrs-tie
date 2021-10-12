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
