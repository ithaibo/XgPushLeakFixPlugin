package com.andy.aop.fixxgleak.visitor;

import com.andy.asm.extention.AddTryCatchMethodVisitor;
import com.andy.plugin.visitor.MethodVisitorFactory;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AddTryCatchBlockFactory implements MethodVisitorFactory {

    @Override
    public MethodVisitor create(String ownerClassName,
                                int access,
                                String name,
                                String desc,
                                String signature,
                                String[] exceptions,
                                MethodVisitor visitor) {
        return new AddTryCatchMethodVisitor(
                Opcodes.ASM5,
                visitor,
                access,
                name,
                 desc,
                ownerClassName
        );
    }

}
