package com.andy.aop.fixxgleak

import com.android.build.api.transform.Transform
import com.andy.aop.fixxgleak.visitor.AddTryCatchBlockFactory
import com.andy.plugin.model.FieldBean
import com.andy.plugin.model.MethodBean
import com.andy.plugin.model.Params
import com.andy.plugin.scan.MethodTracer
import com.andy.plugin.util.Log
import com.andy.plugin.visitor.FieldVisitorFactory
import com.andy.plugin.visitor.MethodVisitorFactory
import org.gradle.api.Project

@SuppressWarnings("unused")
class ASMTraceTransform extends TraceTransform {

    private Map<MethodBean, MethodVisitorFactory> methodVisitorMap
    private MethodVisitorFactory xgG46VisitorFactory = new XgG45MethodVisitorFactory()
    private MethodVisitorFactory xgEventDaVisitorFactory = new XgEventDaVisitorFactory()
    private MethodVisitorFactory addTryCatchFactory = new AddTryCatchBlockFactory()

    ASMTraceTransform(Project project) {
        super(project)
    }

    ASMTraceTransform(Project project, def variant, Transform origTransform) {
        super(project, variant, origTransform)
        //TODO move
        Params.methodToScan = new LinkedList<>()
        Params.methodToScan.add('android/os/HandlerThread#<init>(Ljava.lang.String;)V')
    }

    @Override
    protected Map<MethodBean, MethodVisitorFactory> obtainMethodVisitorMap() {
        if (null == methodVisitorMap) {
            methodVisitorMap = new HashMap<>()
            methodVisitorMap.put(prepareXgG4IntiMethodBean(), xgG46VisitorFactory)
            methodVisitorMap.put(prepareXgG5IntiMethodBean(), xgG46VisitorFactory)
            methodVisitorMap.put(prepareXgEventDaMethodBean(), xgEventDaVisitorFactory)

            methodVisitorMap.put(prepareLifecycleRegistryAddTryCatch(), addTryCatchFactory)
        }
        return methodVisitorMap
    }

    @Override
    protected Map<FieldBean, FieldVisitorFactory> obtainFieldVistorMap() {
        return null
    }

    @Override
    protected void trace(Map<File, File> scrInputMap, Map<File, File> jarInputMap) {
        Log.i("ASM-Transform", "trace")
        MethodTracer methodTracer = new MethodTracer()
        methodTracer.trace(scrInputMap, jarInputMap)
    }

    @Override
    String getName() {
        return "ASMTraceTransform"
    }

    static MethodBean prepareXgG4IntiMethodBean() {
        MethodBean g4 = new MethodBean()
        g4.owner = 'com/tencent/android/tpush/stat/g$4'
        g4.name = '<init>'
        g4.desc = '(Ljava/lang/String;Landroid/content/Context;J)V'
        return g4
    }

    static MethodBean prepareXgG5IntiMethodBean() {
        MethodBean g5 = new MethodBean()
        g5.owner = 'com/tencent/android/tpush/stat/g$5'
        g5.name = '<init>'
        g5.desc = '(Ljava/lang/String;Landroid/content/Context;JJJ)V'
        return g5
    }

    static MethodBean prepareXgEventDaMethodBean() {
        MethodBean mb = new MethodBean()
        mb.owner = 'com/tencent/android/tpush/stat/event/d'
        mb.name = 'a'
        mb.desc = '(Landroid/content/Context;IJ)V'
        return mb
    }

    static MethodBean prepareXgTpushWorkingThread() {
        MethodBean mb = new MethodBean()
        mb.owner = "android/os/HandlerThread"
        mb.name = "<init>"
        mb.desc = "(Ljava/lang/String;)V"
        return mb
    }


    static MethodBean prepareLifecycleRegistryAddTryCatch() {
        return createOne(
                'android/arch/lifecycle/LifecycleRegistry',
                'calculateTargetState',
                'Landroid/arch/lifecycle/LifecycleObserver;)Landroid/arch/lifecycle/Lifecycle/State'
        )
    }

    static MethodBean createOne(String ownerClass, String name, String desc) {
        MethodBean mb = new MethodBean()
        mb.owner = ownerClass
        mb.name = name
        mb.desc = desc
        return mb
    }
}
