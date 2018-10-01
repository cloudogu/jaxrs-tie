package com.github.sdorra.jaxrstie;

import java.util.LinkedHashSet;
import java.util.Set;

public abstract class Resource extends BaseResource {

    protected String type;
    protected Set<SubResource> subResources;
    protected Set<Endpoint> endpoints;

    protected Resource(String type, String name) {
        super(name);
        this.type = type;
        this.parameters = MethodParameters.createEmpty();
        this.subResources = new LinkedHashSet<>();
        this.endpoints = new LinkedHashSet<>();
    }

    public void addEndpoint(Endpoint endpoint) {
        this.endpoints.add(endpoint);
    }

    public void addSubResource(SubResource resource) {
        this.subResources.add(resource);
    }

    public void setParameters(MethodParameters parameters) {
        this.parameters = parameters;
    }

    public String getMethodName() {
        return Names.methodName(name);
    }

    public String getClassName() {
        return Names.className(name);
    }


    public Set<Endpoint> getEndpoints() {
        return endpoints;
    }

    public Set<SubResource> getSubResources() {
        return subResources;
    }

}
