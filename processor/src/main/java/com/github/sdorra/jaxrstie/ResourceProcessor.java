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

import com.google.auto.common.MoreElements;
import org.kohsuke.MetaInfServices;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import javax.ws.rs.Path;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@MetaInfServices(Processor.class)
@SupportedAnnotationTypes("com.github.sdorra.jaxrstie.JaxRsTie")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ResourceProcessor extends AbstractProcessor {

  @Override
  @SuppressWarnings("java:S3516") // for annotation processors it is ok to return always the same value
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    if (roundEnv.processingOver()) {
      return false;
    }

    for (TypeElement annotation : annotations) {
      for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
        process(roundEnv, element);
      }
    }

    return false;
  }

  @SuppressWarnings("UnstableApiUsage")
  private void process(RoundEnvironment roundEnv, Element linkElement) {
    JaxRsTie annotation = linkElement.getAnnotation(JaxRsTie.class);

    List<RootResource> rootResources = new ArrayList<>();
    for (Element element : roundEnv.getElementsAnnotatedWith(Path.class)) {
      if (isClass(element) && element.toString().matches(annotation.value())) {
        RootResource resource = RootResource.from(processingEnv, element);
        rootResources.add(resource);
      }
    }

    TypeElement linkType = MoreElements.asType(linkElement);

    String className = linkType.getSimpleName().toString() + "Links";
    String packageName = getPackageName(linkType);

    Model model = new Model(packageName, className, rootResources);
    try {
      write(model, linkElement);
    } catch (IOException ex) {
      throw new IllegalStateException("failed to create model", ex);
    }
  }

  private boolean isClass(Element element) {
    return element.getKind() == ElementKind.CLASS;
  }

  private String getPackageName(TypeElement classElement) {
    return ((PackageElement) classElement.getEnclosingElement()).getQualifiedName().toString();
  }

  private void write(Model model, Element element) throws IOException {
    Filer filer = processingEnv.getFiler();
    JavaFileObject jfo = filer.createSourceFile(model.getClassName(), element);
    SourceCodeGenerator generator = new SourceCodeGenerator();
    try (Writer writer = jfo.openWriter()) {
      generator.generate(writer, model);
    }
  }

}