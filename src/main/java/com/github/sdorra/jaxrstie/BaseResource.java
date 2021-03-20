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
        List<MethodParameter> allParameters = new ArrayList<>();
        collectParameters(this, allParameters);
        return new MethodParameters(allParameters);
    }

    public MethodParameters getAllParameters() {
        List<MethodParameter> allParameters = new ArrayList<>();
        collectParameters(getParent(), allParameters);
        for (MethodParameter parameter : getParameters()) {
            allParameters.add(parameter);
        }
        return new MethodParameters(allParameters);
    }

    private void collectParameters(BaseResource resource, List<MethodParameter> parameters) {
        if (resource == null) {
            return;
        }
        BaseResource parentResource = resource.getParent();
        if (parentResource != null) {
            collectParameters(parentResource, parameters);
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
