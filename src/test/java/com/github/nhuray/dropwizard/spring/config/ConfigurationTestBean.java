package com.github.nhuray.dropwizard.spring.config;

public class ConfigurationTestBean {

    private String connectorType;
    private String rootPath;
    private int maxThreads;

    public String getConnectorType() {
        return connectorType;
    }

    public void setConnectorType(String connectorType) {
        this.connectorType = connectorType;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public int getMaxThreads() { return maxThreads;  }

    public void setMaxThreads(final int maxThreads) { this.maxThreads = maxThreads; }
}