package com.andy.plugin.model;


public class MethodBean {
    public String owner;
    public int access;
    public String name;
    public String desc;
    public String signature;
    public String[] exceptions;

    public MethodBean(int access, String name, String desc, String signature, String[] exceptions) {
        this.access = access;
        this.name = name;
        this.desc = desc;
        this.signature = signature;
        this.exceptions = exceptions;
    }

    public MethodBean() {
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}