package com.andy.aop.fixxgleak;

import com.andy.plugin.visitor.MethodVisitorFactory;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class XgG45MethodVisitorFactory implements MethodVisitorFactory {

    @Override
    public MethodVisitor create(String ownerClassName,
                                int access,
                                String name,
                                String desc,
                                String signature,
                                String[] exceptions,
                                MethodVisitor visitor) {
        return new ConstructorVisitorTecentTpushStatg45(ownerClassName,
                Opcodes.ASM5,
                visitor,
                access,
                name,
                desc);
    }
}
