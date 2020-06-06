package com.andy.plugin.visitor;

import org.objectweb.asm.MethodVisitor;

public interface MethodVisitorFactory {
    MethodVisitor create(String ownerClassName,
                         int access,
                         String name,
                         String desc,
                         String signature,
                         String[] exceptions,
                         MethodVisitor visitor);
}
