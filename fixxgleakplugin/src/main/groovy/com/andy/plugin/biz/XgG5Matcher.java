package com.andy.plugin.biz;

import com.andy.plugin.model.MethodBean;
import org.objectweb.asm.Opcodes;

import java.util.Objects;

public class XgG5Matcher implements Compact.Matcher<String> {
    private MethodBean target = new MethodBean();
    {
        target.owner = "com/tencent/android/tpush/stat/g$5";
        target.name = "<init>";
        target.desc = "(Ljava/lang/String;Landroid/content/Context;JJJ)V";
    }

    @Override
    public boolean match(String... input) {
        return Objects.equals(target.owner, input[0]) &&
                Objects.equals(target.name, input[1]) &&
                Objects.equals(target.desc, input[2]);
    }
}
