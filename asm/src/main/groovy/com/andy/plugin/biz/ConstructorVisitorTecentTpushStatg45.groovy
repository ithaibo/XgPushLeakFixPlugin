package com.andy.plugin.biz

import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

class ConstructorVisitorTecentTpushStatg45 extends AdviceAdapter {
    String className

    ConstructorVisitorTecentTpushStatg45(int api, MethodVisitor mv, int access, String name, String desc, String className) {
        super(api, mv, access, name, desc)
        this.className = className
    }

    @Override
    protected void onMethodExit(int opcode) {
        Label l1 = new Label()
        mv.visitInsn(Opcodes.ACONST_NULL)
        mv.visitVarInsn(Opcodes.ALOAD, 2)
        mv.visitJumpInsn(Opcodes.IF_ACMPEQ, l1)
        mv.visitVarInsn(Opcodes.ALOAD, 0)
        mv.visitVarInsn(Opcodes.ALOAD, 2)
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "android/content/Context", "getApplicationContext", "()Landroid/content/Context;", false)
        mv.visitFieldInsn(Opcodes.PUTFIELD, className, "b", "Landroid/content/Context;")
        mv.visitLabel(l1)

        super.onMethodExit(opcode)
    }
}