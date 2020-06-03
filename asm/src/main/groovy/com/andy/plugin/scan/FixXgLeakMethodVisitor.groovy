package com.andy.plugin.scan

import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

class FixXgLeakMethodVisitor extends AdviceAdapter {
    String className

    FixXgLeakMethodVisitor(int api, MethodVisitor mv, int access, String name, String desc, String className) {
        super(api, mv, access, name, desc)
        this.className = className
    }

    @Override
    protected void onMethodExit(int opcode) {
        mv.visitLabel(new Label())
        mv.visitVarInsn(Opcodes.ALOAD, 0)
        mv.visitVarInsn(Opcodes.ALOAD, 2)
        mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "Landroid/content/Context", "getApplicationContext", "()Landroid/content/Context;", false)
        mv.visitFieldInsn(Opcodes.PUTFIELD, className, "b", "Landroid/content/Context;")
        super.onMethodExit(opcode)
    }
}