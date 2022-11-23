package com.example.finalproject.models;

public class District {
    String id;
    String provinceId;
    String name;
    String prefix;

    public District() {
    }

    public District(String id, String provinceId, String name, String prefix) {
        this.id = id;
        this.provinceId = provinceId;
        this.name = name;
        this.prefix = prefix;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
