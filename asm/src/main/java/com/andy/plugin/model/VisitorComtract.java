package com.andy.plugin.model;

import org.objectweb.asm.MethodVisitor;

public interface VisitorComtract {
    boolean isNeedToVisit();
    MethodVisitor getVisitor();
}
