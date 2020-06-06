package com.andy.asm.extention;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

public class AddTimeTraceMethodVIsitor extends AdviceAdapter {
    private String ownerClassName;

    /**
     * Creates a new {@link AdviceAdapter}.
     *
     * @param api    the ASM API version implemented by this visitor. Must be one
     *               of {@link Opcodes#ASM4} or {@link Opcodes#ASM5}.
     * @param mv     the method visitor to which this adapter delegates calls.
     * @param access the method's access flags (see {@link Opcodes}).
     * @param name   the method's name.
     * @param desc   the method's descriptor (see {@link Type Type}).
     */
    protected AddTimeTraceMethodVIsitor(int api, MethodVisitor mv, int access, String name, String desc, String ownerClassName) {
        super(api, mv, access, name, desc);
        this.ownerClassName = ownerClassName;
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        //todo add time track
    }

    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode);
        //todo add time track
        //todo add time cost calculation
    }
}
