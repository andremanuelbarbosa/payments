package com.andremanuelbarbosa.payments.resources;

public abstract class Resource {

    private final String type;

    protected Resource(String type) {

        this.type = type;
    }

    public String getType() {

        return type;
    }
}
