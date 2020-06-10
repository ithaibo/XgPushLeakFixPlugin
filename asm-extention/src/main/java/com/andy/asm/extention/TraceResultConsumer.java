package com.andy.asm.extention;


import org.objectweb.asm.MethodVisitor;

/**
 * Note: method must be static, and can access.
 * Value of fields are used to generate code with ASM. know more to see
 * {@link MethodVisitor#visitMethodInsn(int, java.lang.String, java.lang.String, java.lang.String, boolean)}
 */
public class TraceResultConsumer {
    /**such as: java/lang/Exception*/
    public String ownerClassname;
    /**method name*/
    public String methodName;
    /**such as: (Ljava/lang/Exception;)V*/
    public String methodDesc;
    /**if the method's owner class is an interface.*/
    public boolean interfaceMethod;

    /**if access of method is public static or not*/
    public boolean accessPublicStatic;
}
