package com.somanyfeeds.application;

import java.io.InputStream;

public class ResourceLoader {
    public InputStream load(String name) {
        return this.getClass().getResourceAsStream(name);
    }
}
