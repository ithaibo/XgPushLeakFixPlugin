package com.andy.plugin.model;

public class FieldBean {
    public String owner;
    public int access;
    public String name;
    public String desc;
    public String signature;
    public Object value;

    public FieldBean(String owner, int access, String name, String desc, String signature, Object value) {
        this.owner = owner;
        this.access = access;
        this.name = name;
        this.desc = desc;
        this.signature = signature;
        this.value = value;
    }

    public FieldBean() {
    }
}


