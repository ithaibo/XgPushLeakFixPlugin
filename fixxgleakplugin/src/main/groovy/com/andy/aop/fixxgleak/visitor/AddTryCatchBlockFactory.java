package com.andy.aop.fixxgleak.visitor;

import com.andy.asm.extention.trycat.AddTryCatchMethodVisitor;
import com.andy.asm.extention.TraceResultConsumer;
import com.andy.plugin.visitor.MethodVisitorFactory;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AddTryCatchBlockFactory implements MethodVisitorFactory {

    /**
     * exception handler in runtime.
     */
    private TraceResultConsumer exceptionHandler;

    public AddTryCatchBlockFactory(TraceResultConsumer exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public AddTryCatchBlockFactory() {
    }

    @Override
    public MethodVisitor create(String ownerClassName,
                                int access,
                                String name,
                                String desc,
                                String signature,
                                String[] exceptions,
                                MethodVisitor visitor) {
        AddTryCatchMethodVisitor addTryCatchMethodVisitor = new AddTryCatchMethodVisitor(
                Opcodes.ASM5,
                visitor,
                access,
                name,
                 desc,
                ownerClassName
        );

        if (null != exceptionHandler) {
            addTryCatchMethodVisitor.setExceptionHandlerBean(exceptionHandler);
        }

        return addTryCatchMethodVisitor;
    }

}
