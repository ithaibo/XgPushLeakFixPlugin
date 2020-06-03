package com.andy.plugin.biz;

import com.andy.plugin.model.MethodBean;

import java.util.Objects;

public class XgEventDaMatcher implements Compact.Matcher<String> {
    private MethodBean target = new MethodBean();
    {
        target.owner = "com/tencent/android/tpush/stat/event/d";
        target.name = "a";
        target.desc = "(Landroid/content/Context;IJ)V";
    }

    @Override
    public boolean match(String... input) {
        return Objects.equals(target.owner, input[0]) &&
                Objects.equals(target.name, input[1]) &&
                Objects.equals(target.desc, input[2]);
    }
}
