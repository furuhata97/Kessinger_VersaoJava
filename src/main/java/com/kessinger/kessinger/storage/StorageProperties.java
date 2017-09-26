package com.kessinger.kessinger.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

    private String location = "upload-dir";

    public String getLocation() {
        return location;
    }

    public StorageProperties(){

    }

    public void setLocation(String location) {
        this.location = location;
    }
}


