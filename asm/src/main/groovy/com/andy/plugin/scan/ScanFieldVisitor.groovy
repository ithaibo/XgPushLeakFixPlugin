package com.andy.plugin.scan

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Attribute
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.TypePath

class ScanFieldVisitor extends FieldVisitor{
    ScanFieldVisitor(int api) {
        super(api)
    }

    ScanFieldVisitor(int api, FieldVisitor fv) {
        super(api, fv)
    }

    @Override
    Object invokeMethod(String s, Object o) {
        return super.invokeMethod(s, o)
    }

    @Override
    Object getProperty(String s) {
        return super.getProperty(s)
    }

    @Override
    void setProperty(String s, Object o) {
        super.setProperty(s, o)
    }

    @Override
    MetaClass getMetaClass() {
        return super.getMetaClass()
    }

    @Override
    void setMetaClass(MetaClass metaClass) {
        super.setMetaClass(metaClass)
    }

    @Override
    AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return super.visitAnnotation(desc, visible)
    }

    @Override
    AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {

        return super.visitTypeAnnotation(typeRef, typePath, desc, visible)
    }

    @Override
    void visitAttribute(Attribute attr) {
        super.visitAttribute(attr)
    }

    @Override
    void visitEnd() {
        super.visitEnd()
    }
}