package com.github.sdorra.jaxrstie;

import com.google.auto.common.MoreTypes;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

public class SubResource extends Resource {

    public SubResource(String type, String name) {
        super(type, name);
    }

    @Override
    public String toString() {
        return name + "(" + parameters + ")";
    }

    public static SubResource from(ProcessingEnvironment processingEnv, ExecutableElement element, Resource parent) {
        TypeMirror returnType = element.getReturnType();
        Element returnElement = MoreTypes.asElement(returnType);

        String name = Names.of(element);

        SubResource resource = new SubResource(returnElement.toString(), name);
        resource.setParent(parent);

        MethodParameters parameters = Methods.findPathParams(element);
        resource.setParameters(parameters);

        Resources.append(processingEnv, resource, returnElement);

        return resource;
    }
}
