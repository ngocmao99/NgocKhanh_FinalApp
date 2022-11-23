package com.example.finalproject.models;

import java.util.List;

public class Province {
    String code;
    String name;
    String prefix;
    String zipCode;

    public Province() {
    }

    public Province(String code, String name, String prefix, String zipCode) {
        this.code = code;
        this.name = name;
        this.prefix = prefix;
        this.zipCode = zipCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
