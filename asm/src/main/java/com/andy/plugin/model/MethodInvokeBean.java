package com.andy.plugin.model;

public class MethodInvokeBean extends InvokeScanned {
    @Override
    protected String getTypeInvoked() {
        return "method";
    }
}
