package com.andremanuelbarbosa.payments.resources;

import java.util.List;

public class Resources<E extends Resource> {

    private final List<E> data;
    private final Links links;

    public Resources(List<E> data, Links links) {

        this.data = data;
        this.links = links;
    }

    public List<E> getData() {

        return data;
    }

    public Links getLinks() {

        return links;
    }

    public static class Links {

        private final String self;

        public Links(String self) {

            this.self = self;
        }

        public String getSelf() {

            return self;
        }
    }
}
