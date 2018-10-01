package com.github.sdorra.jaxrstie;

import com.google.auto.common.MoreTypes;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import java.util.Set;

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

        Set<Methods.MethodWrapper> methods = Methods.findJaxRsMethods(processingEnv, returnElement);

        for (Methods.MethodWrapper method : methods) {
            if (method.isEndpoint()) {
                resource.addEndpoint(Endpoint.of(method.getMethod(), resource));
            } else {
                resource.addSubResource(SubResource.from(processingEnv, method.getMethod(), resource));
            }
        }

        return resource;
    }
}
