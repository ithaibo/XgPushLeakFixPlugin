package com.andy.plugin.visitor;

import org.objectweb.asm.FieldVisitor;

public interface FieldVisitorFactory {
    FieldVisitor create(String ownerClassName,
                        int access,
                        String name,
                        String desc,
                        String signature,
                        Object value,
                        FieldVisitor fieldVisitor);
}
