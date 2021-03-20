package com.github.sdorra.jaxrstie;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.Objects;

public class MethodParameter {

    private final String name;
    private final String type;

    public MethodParameter(String name, String type) {
        this.name = name;
        this.type = removeJavaLang(type);
    }

    private String removeJavaLang(String type) {
        if (type.startsWith("java.lang.")) {
            return type.substring("java.lang.".length());
        }
        return type;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MethodParameter that = (MethodParameter) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public String toString() {
        return type + " " + name;
    }

    public static MethodParameter of(VariableElement parameter) {
        String name = parameter.getSimpleName().toString();
        TypeMirror typeMirror = parameter.asType();
        String type;
        if (typeMirror instanceof DeclaredType) {
            type = ((DeclaredType)typeMirror).asElement().getSimpleName().toString();
        } else {
            type = typeMirror.toString();
        }
        return new MethodParameter(name, type);
    }
}
