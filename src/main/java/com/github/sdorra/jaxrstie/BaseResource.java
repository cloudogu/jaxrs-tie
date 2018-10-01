package com.github.sdorra.jaxrstie;

import java.util.ArrayList;
import java.util.List;

public class BaseResource {

    protected String name;
    protected MethodParameters parameters;
    protected BaseResource parent;

    public BaseResource(String name) {
        this.name = name;
        this.parameters = MethodParameters.createEmpty();
    }

    public String getName() {
        return name;
    }

    public MethodParameters getParameters() {
        return parameters;
    }

    public MethodParameters getAllParametersAnonymized() {
        List<MethodParameter> parameters = new ArrayList<>();
        collectParameters(this, parameters);
        return new MethodParameters(parameters);
    }

    public MethodParameters getAllParameters() {
        List<MethodParameter> parameters = new ArrayList<>();
        collectParameters(getParent(), parameters);
        for (MethodParameter parameter : getParameters()) {
            parameters.add(parameter);
        }
        return new MethodParameters(parameters);
    }

    private final void collectParameters(BaseResource resource, List<MethodParameter> parameters) {
        if (resource == null) {
            return;
        }
        BaseResource parent = resource.getParent();
        if (parent != null) {
            collectParameters(parent, parameters);
        }
        for (MethodParameter param : resource.getParameters()) {
            parameters.add(new MethodParameter("arg" + parameters.size(), param.getType()));
        }
    }

    public BaseResource getParent() {
        return parent;
    }

    public void setParameters(MethodParameters parameters) {
        this.parameters = parameters;
    }

    public void setParent(BaseResource parent) {
        this.parent = parent;
    }
}
