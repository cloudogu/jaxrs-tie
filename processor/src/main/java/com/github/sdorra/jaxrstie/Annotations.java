package com.github.sdorra.jaxrstie;

import javax.lang.model.element.AnnotationMirror;
import java.lang.annotation.Annotation;

public final class Annotations {

  private Annotations() {
  }

  public static boolean isTypeOf(AnnotationMirror mirror, Class<? extends Annotation> annotation) {
    return mirror.getAnnotationType().toString().equals(annotation.toString());
  }

}
