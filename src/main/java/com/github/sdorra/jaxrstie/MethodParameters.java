package com.github.sdorra.jaxrstie;

import com.google.common.base.Joiner;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class MethodParameters implements Iterable<MethodParameter> {

    private List<MethodParameter> parameters;

    public MethodParameters(List<MethodParameter> parameters) {
        this.parameters = parameters;
    }

    public ParameterNames getNames() {
        return new ParameterNames(parameters.stream().map(MethodParameter::getName).collect(Collectors.toList()));
    }

    public boolean isEmpty() {
        return parameters.isEmpty();
    }

    public static MethodParameters createEmpty() {
        return new MethodParameters(Collections.emptyList());
    }

    @Override
    public Iterator<MethodParameter> iterator() {
        return parameters.iterator();
    }

    @Override
    public String toString() {
        return Joiner.on(", ").join(parameters);
    }

    public static class ParameterNames implements Iterable<String> {

        private List<String> names;

        public ParameterNames(List<String> names) {
            this.names = names;
        }

        @Override
        public Iterator<String> iterator() {
            return names.iterator();
        }

        @Override
        public String toString() {
            return Joiner.on(", ").join(names);
        }
    }
}
