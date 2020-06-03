package com.andy.plugin.model;

import java.util.List;

public class ClassBean {
    public int version;
    public int access;
    public String name;
    public String signature;
    public String superName;
    public String[] interfaces;

    public List<FieldBean> fieldList;
    public List<MethodBean> methodList;
}
