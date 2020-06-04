package com.andy.aop.fixxgleak

import com.android.build.api.transform.Transform
import com.andy.plugin.model.FieldBean
import com.andy.plugin.model.MethodBean
import com.andy.plugin.scan.MethodTracer
import com.andy.plugin.visitor.FieldVisitorFactory
import com.andy.plugin.visitor.MethodVisitorFactory
import org.gradle.api.Project
import org.gradle.api.Task

class ASMTraceTransform extends TraceTransform {

    private Map<MethodBean, MethodVisitorFactory> methodVisitorMap
    private MethodVisitorFactory xgG46VisitorFactory = new XgG45MethodVisitorFactory()
    private MethodVisitorFactory xgEventDaVisitorFactory = new XgEventDaVisitorFactory()

    ASMTraceTransform(Project project, def variant, Transform origTransform) {
        super(project, variant, origTransform)
    }

    @Override
    protected Map<MethodBean, MethodVisitorFactory> obtainMethodVisitorMap() {
        if (null == methodVisitorMap) {
            methodVisitorMap = new HashMap<>()
            methodVisitorMap.put(prepareXgG4IntiMethodBean(), xgG46VisitorFactory)
            methodVisitorMap.put(prepareXgG5IntiMethodBean(), xgG46VisitorFactory)
            methodVisitorMap.put(prepareXgEventDaMethodBean(), xgEventDaVisitorFactory)

        }
        return methodVisitorMap
    }

    @Override
    protected Map<FieldBean, FieldVisitorFactory> obtainFieldVistorMap() {
        return null
    }

    @Override
    protected void trace(Map<File, File> scrInputMap, Map<File, File> jarInputMap) {
        MethodTracer methodTracer = new MethodTracer()
        methodTracer.trace(scrInputMap, jarInputMap)
    }

    @Override
    String getName() {
        return "ASMTraceTransform"
    }

    private static MethodBean prepareXgG4IntiMethodBean() {
        MethodBean g4 = new MethodBean()
        g4.owner = 'com/tencent/android/tpush/stat/g$4'
        g4.name = '<init>'
        g4.desc = '(Ljava/lang/String;Landroid/content/Context;J)V'
        return g4
    }

    private static MethodBean prepareXgG5IntiMethodBean() {
        MethodBean g5 = new MethodBean()
        g5.owner = 'com/tencent/android/tpush/stat/g$5'
        g5.name = '<init>'
        g5.desc = '(Ljava/lang/String;Landroid/content/Context;JJJ)V'
        return g5
    }

    private static MethodBean prepareXgEventDaMethodBean() {
        MethodBean mb = new MethodBean()
        mb.owner = 'com/tencent/android/tpush/stat/event/d'
        mb.name = 'a'
        mb.desc = '(Landroid/content/Context;IJ)V'
        return mb
    }
}
