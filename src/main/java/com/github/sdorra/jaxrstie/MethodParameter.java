package com.github.sdorra.jaxrstie;

import javax.lang.model.element.VariableElement;
import java.util.Objects;

public class MethodParameter {

    private final String name;
    private final String type;

    public MethodParameter(String name, String type) {
        this.name = name;
        this.type = removeJavaLang(type);
    }

    private final String removeJavaLang(String type) {
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
        String type = parameter.asType().toString();
        return new MethodParameter(name, type);
    }
}
