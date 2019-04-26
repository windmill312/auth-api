package com.github.windmill312.auth.model;

public enum Subsystem {
    INTERNAL_SERVICE(1, 10),
    USER(2, 20),
    EXTERNAL_SERVICE(4, 40);

    private int id;
    private int code;

    Subsystem(int id, int code) {
        this.id = id;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "Subsystem{" +
                "id=" + id +
                '}';
    }
}
