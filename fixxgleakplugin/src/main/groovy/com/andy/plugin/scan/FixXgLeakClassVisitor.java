package com.andy.plugin.scan;

import com.andy.plugin.biz.ConstructorVisitorTecentTpushStatg45;
import com.andy.plugin.biz.XgEventDaMatcher;
import com.andy.plugin.biz.XgG4Matcher;
import com.andy.plugin.biz.XgG5Matcher;
import com.andy.plugin.model.MethodBean;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import com.andy.plugin.model.ClassBean;
import com.andy.plugin.util.Log;

public class FixXgLeakClassVisitor extends ClassVisitor {
    private final static String TAG = "FixXgLeakClassVisitor";
    private ClassBean classBean;

    XgG4Matcher xgG4Matcher = new XgG4Matcher();
    XgG5Matcher xgG5Matcher = new XgG5Matcher();
    XgEventDaMatcher xgEventDaMatcher = new XgEventDaMatcher();

    public FixXgLeakClassVisitor(int api, ClassVisitor cv) {
        super(api, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        classBean = new ClassBean();
        classBean.version = version;
        classBean.access = access;
        classBean.name = name;
        classBean.signature = signature;
        classBean.superName = superName;
        classBean.interfaces = interfaces;
        super.visit(version, access, name, signature, superName, interfaces);
    }


    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);

        String methodToScan = String.format("%s#%s%s", classBean.name, name, desc);
        if (xgG4Matcher.match(classBean.name, name, desc) || xgG5Matcher.match(classBean.name, name, desc)) {
            Log.i(TAG, "find one to scan:%s", methodToScan);
            return new ConstructorVisitorTecentTpushStatg45(Opcodes.ASM5, methodVisitor, access, name, desc, classBean.name);
        }

        if (xgEventDaMatcher.match(classBean.name, name, desc)) {
            Log.i(TAG, "find one to scan:%s", methodToScan);
            return new MethodVisitorTecentTpushStatEvent_d_a(Opcodes.ASM5, methodVisitor, access, name, desc, classBean.name);
        }
        return methodVisitor;
    }

}