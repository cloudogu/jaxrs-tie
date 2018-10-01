package com.github.sdorra.jaxrstie;

import java.util.List;

public class Model {

    private String packageName;
    private String simpleClassName;
    private List<RootResource> rootResources;

    public Model(String packageName, String simpleClassName, List<RootResource> rootResources) {
        this.packageName = packageName;
        this.simpleClassName = simpleClassName;
        this.rootResources = rootResources;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getSimpleClassName() {
        return simpleClassName;
    }

    public String getClassName() {
        return packageName.concat(".").concat(simpleClassName);
    }

    public List<RootResource> getRootResources() {
        return rootResources;
    }
}
