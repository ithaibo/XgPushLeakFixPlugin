package com.andy.plugin.scan;

import com.andy.plugin.model.ClassBean;
import com.andy.plugin.model.FieldBean;
import com.andy.plugin.model.MethodBean;
import com.andy.plugin.model.Params;
import com.andy.plugin.util.Log;
import com.andy.plugin.visitor.ClassInfoReader;
import com.andy.plugin.visitor.FieldVisitorFactory;
import com.andy.plugin.visitor.MethodVisitorFactory;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class CommonClassVisitor extends ClassVisitor {
    private final static String TAG = "ClassVisitor";
    private ClassBean classBean;

    public CommonClassVisitor(int api, ClassVisitor cv) {
        super(api, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
//        Log.i(TAG, "visit, class:%s", name);
        classBean = new ClassBean();
        classBean.version = version;
        classBean.access = access;
        classBean.name = name;
        classBean.signature = signature;
        classBean.superName = superName;
        classBean.interfaces = interfaces;
        super.visit(version, access, name, signature, superName, interfaces);

        if (null == Params.classInfoReaders) return;
        for (ClassInfoReader reader : Params.classInfoReaders) {
            reader.read(classBean);
        }
    }


    @Override
    public MethodVisitor visitMethod(int access,
                                     String name,
                                     String desc,
                                     String signature,
                                     String[] exceptions) {
        MethodVisitor rawVisitor = super.visitMethod(access, name, desc, signature, exceptions);

        //scan method invoked
        MethodVisitor superVisitor = new ScanMethodVisitor(Opcodes.ASM5, rawVisitor, classBean.name, name, desc);

        if (null == Params.methodVisitorMap || Params.methodVisitorMap.isEmpty()) {
            Log.i(TAG, "visitMethod, no method to visit");
            return superVisitor;
        }

//        Log.i(TAG, "visitMethod, name:%s, desc:%s", name, desc);
        for (MethodBean methodBean : Params.methodVisitorMap.keySet()) {
            if (classBean.name.equals(methodBean.owner) &&
                    name.equals(methodBean.name) &&
                    desc.equals(methodBean.desc)) {
                MethodVisitorFactory methodVisitorFactory = Params.methodVisitorMap.get(methodBean);
                if (null == methodVisitorFactory) {
                    continue;
                }
                MethodVisitor methodVisitor = methodVisitorFactory.create(classBean.name,
                        access,
                        name,
                        desc,
                        signature,
                        exceptions,
                        superVisitor);
                if (null == methodVisitor) {
                    continue;
                }
                Log.i(TAG, "find one method to handle, class:%s, name:%s, desc:%s",
                        classBean.name,
                        name,
                        desc);
                return methodVisitor;
            }
        }

        return superVisitor;
    }


    @Override
    public FieldVisitor visitField(int access,
                                   String name,
                                   String desc,
                                   String signature,
                                   Object value) {
        FieldVisitor superVisitor = super.visitField(access, name, desc, signature, value);

        if (null == Params.fieldVisitorMap || Params.fieldVisitorMap.isEmpty())
        return superVisitor;

        for (FieldBean fieldBean : Params.fieldVisitorMap.keySet()) {
            if (classBean.name.equals(fieldBean.owner) &&
                    name.equals(fieldBean.name) &&
                    desc.equals(fieldBean.desc)) {
                FieldVisitorFactory fieldVisitorFactory = Params.fieldVisitorMap.get(fieldBean);
                if (null == fieldVisitorFactory) {
                    continue;
                }
                FieldVisitor fieldVisitor = fieldVisitorFactory.create(classBean.name,
                        access,
                        name,
                        desc,
                        signature,
                        value,
                        superVisitor);
                if (null == fieldVisitor) {
                    continue;
                }
                Log.i(TAG, "find one field to handle, class:%s, name:%s, desc:%s",
                        classBean.name,
                        name,
                        desc);
                return fieldVisitor;
            }
        }

        return superVisitor;
    }
}