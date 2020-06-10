package com.andy.asm.extention.trycat;

import com.andy.asm.extention.TraceResultConsumer;
import com.andy.plugin.util.Log;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

import java.util.Objects;

public class AddTryCatchMethodVisitor extends AdviceAdapter {
    private String TAG = "AddTryCatchBlock";

    private Label start;
    private Label end;
    private String ownerClass;
    private String methodName;

    private TraceResultConsumer exceptionHandlerBean;

    /**
     * Creates a new {@link AdviceAdapter}.
     *
     * @param api    the ASM API version implemented by this visitor. Must be one
     *               of {@link Opcodes#ASM4} or {@link Opcodes#ASM5}.
     * @param mv     the method visitor to which this adapter delegates calls.
     * @param access the method's access flags (see {@link Opcodes}).
     * @param name   the method's name.
     * @param desc   the method's descriptor (see {@linkType Type}).
     */
    public AddTryCatchMethodVisitor(int api,
                                    MethodVisitor mv,
                                    int access,
                                    String name,
                                    String desc,
                                    String ownerClass) {
        super(api, mv, access, name, desc);
        this.ownerClass = ownerClass;
        this.methodName = name;
    }

    public AddTryCatchMethodVisitor(int api,
                                    MethodVisitor mv,
                                    int access,
                                    String name,
                                    String desc,
                                    String ownerClass,
                                    TraceResultConsumer exceptionHandlerBean) {
        this(api, mv, access, name, desc, ownerClass);
        this.exceptionHandlerBean = exceptionHandlerBean;
    }

    public void setExceptionHandlerBean(TraceResultConsumer exceptionHandlerBean) {
        this.exceptionHandlerBean = exceptionHandlerBean;
    }

    @Override
    protected void onMethodEnter() {
        Label l0 = new Label();
        start = new Label();
        end = new Label();
        mv.visitTryCatchBlock(l0, start, end, "java/lang/Exception");
        mv.visitLabel(l0);
        super.onMethodEnter();
    }

    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode);

        mv.visitLabel(start);
        Label l3 = new Label();
        mv.visitJumpInsn(GOTO, l3);
        mv.visitLabel(end);

        mv.visitVarInsn(ASTORE, 1);
        mv.visitVarInsn(ALOAD, 1);

        //handle exception occur
        if (null != exceptionHandlerBean) {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC,
                    exceptionHandlerBean.ownerClassname,
                    exceptionHandlerBean.methodName,
                    exceptionHandlerBean.methodDesc,
                    exceptionHandlerBean.interfaceMethod);
        } else {
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                    "java.lang.Exception",
                    "printStackTrace",
                    "()V",
                    false);
        }

        Label l4 = new Label();
        mv.visitLabel(l4);
        mv.visitLineNumber(14, l4);

        if (isVoidReturn()) {
            mv.visitInsn(Opcodes.RETURN);
        }
        else if (isReferenceReturn()) {
            mv.visitInsn(Opcodes.ACONST_NULL);
            mv.visitInsn(Opcodes.ARETURN);
        } else {
            mv.visitInsn(Opcodes.ICONST_0);
            mv.visitInsn(Opcodes.IRETURN);
        }


        mv.visitLabel(l3);

        Log.i(TAG, "add try catch block for class:%s, method:%s, desc:%s", ownerClass, methodName, methodDesc);
    }

    private boolean isReferenceReturn() {
        String[] descArray = methodDesc.split("\\)");
//        Log.i(TAG, "desc:%s", methodDesc);
        boolean isObj = false;
        if (descArray.length > 1 && null != descArray[1]) {
            isObj = descArray[1].startsWith("L") || descArray[1].startsWith("[");
        }
        return isObj;
    }

    private boolean isVoidReturn() {
        String[] descArray = methodDesc.split("\\)");
//        Log.i(TAG, "desc:%s", methodDesc);
        return (descArray.length > 1 && null != descArray[1] && Objects.equals("V", descArray[1]));
    }
}
