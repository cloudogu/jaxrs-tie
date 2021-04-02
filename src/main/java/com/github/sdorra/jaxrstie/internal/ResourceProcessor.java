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

package com.github.sdorra.jaxrstie.internal;

import com.github.sdorra.jaxrstie.GenerateLinks;
import com.google.auto.common.MoreElements;
import com.google.common.base.Strings;
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
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("com.github.sdorra.jaxrstie.GenerateLinks")
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

  private void process(RoundEnvironment roundEnv, Element linkElement) {
    GenerateLinks annotation = linkElement.getAnnotation(GenerateLinks.class);

    List<RootResource> rootResources = collectResources(roundEnv, annotation);

    TypeElement linkType = MoreElements.asType(linkElement);

    String className = className(annotation, linkType);
    String packageName = packageName(annotation, linkType);

    Model model = new Model(packageName, className, rootResources);
    try {
      write(model, linkElement);
    } catch (IOException ex) {
      throw new IllegalStateException("failed to create model", ex);
    }
  }

  private List<RootResource> collectResources(RoundEnvironment roundEnv, GenerateLinks annotation) {
    List<RootResource> rootResources = new ArrayList<>();
    for (Element element : roundEnv.getElementsAnnotatedWith(Path.class)) {
      if (shouldProcess(annotation, element)) {
        RootResource resource = RootResource.from(processingEnv, element);
        rootResources.add(resource);
      }
    }
    return rootResources;
  }

  private String className(GenerateLinks annotation, TypeElement linkType) {
    if (Strings.isNullOrEmpty(annotation.className())) {
      return linkType.getSimpleName().toString() + "Links";
    }
    return annotation.className();
  }

  private String packageName(GenerateLinks annotation, TypeElement classElement) {
    if (Strings.isNullOrEmpty(annotation.packageName())) {
      return ((PackageElement) classElement.getEnclosingElement()).getQualifiedName().toString();
    }
    return annotation.packageName();
  }

  private boolean shouldProcess(GenerateLinks annotation, Element element) {
    return isClass(element) && isIncluded(annotation, element.toString());
  }

  private boolean isIncluded(GenerateLinks annotation, String resourceClass) {
    return resourceClass.matches(annotation.includes()) && ! isExcluded(annotation, resourceClass);
  }

  private boolean isExcluded(GenerateLinks annotation, String resourceClass) {
    return ! Strings.isNullOrEmpty(annotation.excludes()) && resourceClass.matches(annotation.excludes());
  }

  private boolean isClass(Element element) {
    return element.getKind() == ElementKind.CLASS;
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
