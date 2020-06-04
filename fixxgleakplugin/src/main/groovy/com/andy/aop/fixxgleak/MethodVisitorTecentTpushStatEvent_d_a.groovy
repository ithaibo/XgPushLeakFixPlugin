package com.andy.aop.fixxgleak

import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

class MethodVisitorTecentTpushStatEvent_d_a extends AdviceAdapter {
    String className

    MethodVisitorTecentTpushStatEvent_d_a(int api, MethodVisitor mv, int access, String name, String desc, String className) {
        super(api, mv, access, name, desc)
        this.className = className
    }

    @Override
    protected void onMethodExit(int opcode) {
        Label l1 = new Label()
        mv.visitInsn(Opcodes.ACONST_NULL)
        mv.visitVarInsn(Opcodes.ALOAD, 1)
        mv.visitJumpInsn(Opcodes.IF_ACMPEQ, l1)
        mv.visitVarInsn(Opcodes.ALOAD, 0)
        mv.visitVarInsn(Opcodes.ALOAD, 1)
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "android/content/Context", "getApplicationContext", "()Landroid/content/Context;", false)
        mv.visitFieldInsn(Opcodes.PUTFIELD, className, "l", "Landroid/content/Context;")
        mv.visitLabel(l1)

        super.onMethodExit(opcode)
    }
}