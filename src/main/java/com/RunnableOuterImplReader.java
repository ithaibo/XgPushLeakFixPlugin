package com;

import com.andy.plugin.model.ClassBean;
import com.andy.plugin.util.Log;
import com.andy.plugin.visitor.ClassInfoReader;

import java.util.Objects;

public class RunnableOuterImplReader implements ClassInfoReader {
    private String TAG = "RunnableOuterImpl";

    @Override
    public void read(ClassBean classBean) {
        if (null == classBean) return;

        if (null == classBean.interfaces || classBean.interfaces.length <= 0) {
            return;
        }
        final String runnable = "java/lang/Runnable";
        boolean impl = false;
        for (String interfaceName : classBean.interfaces) {
            if (Objects.equals(interfaceName, runnable)) {
                impl = true;
                break;
            }
        }
        if (!impl) {
            return;
        }
        if (!classBean.name.contains("\\$")) {
            Log.i(TAG, "find one implements Runnable:%s", classBean.name);
        }
    }
}
