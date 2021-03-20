package com.github.sdorra.jaxrstie;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.google.auto.common.MoreElements;
import org.kohsuke.MetaInfServices;

import javax.annotation.processing.*;
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

    private static final String TEMPLATE = "com/github/sdorra/jaxrstie/template.mustache";
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return false;
        }

        for ( TypeElement annotation : annotations ) {
            for ( Element element : roundEnv.getElementsAnnotatedWith(annotation) ) {
                process(roundEnv, element);
            }
        }


        return false;
    }

    private void process(RoundEnvironment roundEnv, Element linkElement) {
        JaxRsTie annotation = linkElement.getAnnotation(JaxRsTie.class);

        List<RootResource> rootResources = new ArrayList<>();
        for ( Element element : roundEnv.getElementsAnnotatedWith(Path.class) ) {
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
        Mustache mustache = new DefaultMustacheFactory().compile(TEMPLATE);

        try (Writer writer = jfo.openWriter()) {
            mustache.execute(writer, model).flush();
        }
    }
}
