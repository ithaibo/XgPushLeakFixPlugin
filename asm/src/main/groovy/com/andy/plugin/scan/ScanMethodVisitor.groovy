package com.andy.plugin.scan


import com.andy.plugin.model.MethodInvokeBean
import com.andy.plugin.model.Params
import com.andy.plugin.util.Log
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor

class ScanMethodVisitor extends MethodVisitor {
    String className
    String methodName
    String methodDesc
    private int line

    ScanMethodVisitor(int api, MethodVisitor mv, String className, String methodName, String methodDesc) {
        super(api, mv)
        this.className = className
        this.methodName = methodName
        this.methodDesc = methodDesc
    }

    @Override
    void visitFieldInsn(int opcode, String owner, String name, String desc) {
        super.visitFieldInsn(opcode, owner, name, desc)
//        FieldInvokeBean fieldInvokeBean = new FieldInvokeBean()
//        fieldInvokeBean.Invoked = String.format("%1s#%2s", owner, name)
//        fieldInvokeBean.invoker = String.format("%1s#%2s", className, methodName)
//        Params.fieldScannedList.add(fieldInvokeBean)
//        Log.i("MethodScan", "visitFieldInsn, " + fieldInvokeBean.Invoked)
    }


    @Override
    void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        super.visitMethodInsn(opcode, owner, name, desc, itf)

        String methodInvoked = String.format("%1s#%2s%3s", owner.trim(), name.trim(), desc.trim())
        Log.i("MethodScan", "visitMethodInsn:%s", methodInvoked)

        // 匹配方法扫描列表
        if (null != Params.methodToScan) {
            for (String textMethod : Params.methodToScan) {
                if (textMethod == methodInvoked) {
                    MethodInvokeBean methodInvokeBean = new MethodInvokeBean()
                    methodInvokeBean.invoker = String.format("%1s#%2s%3s", className, methodName, methodDesc)
                    methodInvokeBean.invoked = methodInvoked
                    methodInvokeBean.line = line

                    Params.methodScannedList.add(methodInvokeBean)

                    Log.i("MethodScan",
                            "visitMethodInsn, %1s invoked %2s, line: %1d",
                            methodInvokeBean.invoker,
                            methodInvokeBean.invoked,
                            methodInvokeBean.line)
                }
            }
        }
    }


    @Override
    void visitLineNumber(int line, Label start) {
        this.line = line
        super.visitLineNumber(line, start)
    }
}