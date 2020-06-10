package com.andy.asm.extention.timecost;

import com.andy.asm.extention.TraceResultConsumer;
import com.andy.plugin.util.Log;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;


public class AddTimeTraceMethodVisitor extends AdviceAdapter {
    private String ownerClassName;
    private String methodName;
    private int timeStart;
    private int timeEnd;
    public static final String consumerDesc = "(Lcom/andy/asm/runtime/MethodTimeCostResult;)V";

    /**note: desc must be: {@link #consumerDesc}*/
    private TraceResultConsumer consumer;
    private String TAG = "AddTimeTrace";

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
    public AddTimeTraceMethodVisitor(int api,
                                     MethodVisitor mv,
                                     int access,
                                     String name,
                                     String desc,
                                     String ownerClassName,
                                     TraceResultConsumer timeConsumer) {
        super(api, mv, access, name, desc);
        this.ownerClassName = ownerClassName;
        this.methodName = name;
        this.consumer = timeConsumer;
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        //save start time clock
        mv.visitMethodInsn(Opcodes.INVOKESTATIC,
                "java/lang/System",
                "currentTimeMillis",
                "()J",
                false);
        timeStart = newLocal(Type.LONG_TYPE);
        mv.visitVarInsn(Opcodes.LSTORE, timeStart);
    }

    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode);
        //save end time clock
        invokeProcessTimeCost();
//        printTimeCostLog();
    }

    private void invokeProcessTimeCost() {
        mv.visitMethodInsn(Opcodes.INVOKESTATIC,
                "java/lang/System",
                "currentTimeMillis",
                "()J",
                false);
        timeEnd = newLocal(Type.LONG_TYPE);
        mv.visitVarInsn(Opcodes.LSTORE, timeEnd);



        //check
        if (null == consumer) return;
        if (!consumerDesc.equals(consumer.methodDesc)) return;
        if (!consumer.accessPublicStatic) return;
        if (null == consumer.ownerClassname) return;
        if (null == consumer.methodName) return;

        String resultOwner = "com/andy/asm/runtime/MethodTimeCostResult";
        mv.visitTypeInsn(Opcodes.NEW, resultOwner); //TODO 这里为什么要提前
        mv.visitInsn(Opcodes.DUP);                  //TODO 这里为什么要提前

        //owner
        mv.visitLdcInsn(ownerClassName);
        //name
        mv.visitLdcInsn(methodName);
        //desc
        mv.visitLdcInsn(methodDesc);
        //time start
        mv.visitVarInsn(Opcodes.LLOAD, timeStart);
        //time end
        mv.visitVarInsn(Opcodes.LLOAD, timeEnd);

        mv.visitMethodInsn(Opcodes.INVOKESPECIAL,
                resultOwner,
                "<init>",
                "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JJ)V",
                false);


        mv.visitMethodInsn(Opcodes.INVOKESTATIC,
                consumer.ownerClassname,
                consumer.methodName,
                consumer.methodDesc,
                consumer.interfaceMethod);

        Log.i(TAG,
                "time cost trace inject success, class:%s, method:%s, desc:%s",
                ownerClassName,
                methodName,
                methodDesc);
    }

    private void printTimeCostLog() {
        mv.visitMethodInsn(Opcodes.INVOKESTATIC,
                "java/lang/System",
                "currentTimeMillis",
                "()J",
                false);
        //do sub
        mv.visitVarInsn(Opcodes.LLOAD, timeStart);
        mv.visitInsn(Opcodes.LSUB);
        //save cost
        mv.visitVarInsn(Opcodes.LSTORE, timeStart);
        String owner = "java/lang/StringBuilder";

        int varSb = newLocal(Type.getType(owner));

        mv.visitTypeInsn(Opcodes.NEW, owner);
        mv.visitInsn(Opcodes.DUP);

        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, owner, "<init>", "()V", false);
        mv.visitVarInsn(Opcodes.ASTORE, varSb);

        mv.visitVarInsn(Opcodes.ALOAD, varSb);
        mv.visitLdcInsn(ownerClassName+"."+methodName+", time cost:");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                owner,
                "append",
                "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
                false);
        mv.visitInsn(Opcodes.POP);

        mv.visitVarInsn(Opcodes.ALOAD, varSb);
        mv.visitVarInsn(Opcodes.LLOAD, timeStart);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                owner,
                "append",
                "(J)Ljava/lang/StringBuilder;",
                false);
        mv.visitInsn(Opcodes.POP);

        mv.visitLdcInsn("TimeCost");
        mv.visitVarInsn(Opcodes.ALOAD, varSb);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                owner,
                "toString",
                "()Ljava/lang/String;",
                false);

        mv.visitMethodInsn(Opcodes.INVOKESTATIC,
                "android/util/Log",
                "d",
                "(Ljava/lang/String;Ljava/lang/String;)I",
                false);
        mv.visitInsn(Opcodes.POP);
    }

}
