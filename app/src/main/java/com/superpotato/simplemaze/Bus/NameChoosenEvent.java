package com.superpotato.simplemaze.Bus;

/**
 * Created by Erwan on 24/05/2015.
 */
public class NameChoosenEvent {

    private String name;

    public NameChoosenEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
