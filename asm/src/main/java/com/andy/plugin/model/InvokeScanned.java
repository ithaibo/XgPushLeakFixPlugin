package com.andy.plugin.model;

public abstract class InvokeScanned {
    /**com.andy.scanplugin.Config#canDebug()
     * or com.andy.scanplugin.Config#canDebug(java.lang.String)
     * */
    public String invoked;

    //com.andy.scanplugin.Config.canDebug(java.lang.String, boolean)
    /**class and method who invoked {@link #invoked}*/
    public String invoker;

    /**line number invoke occur*/
    public int line;

    protected abstract String getTypeInvoked();
}
