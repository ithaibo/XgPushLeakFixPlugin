import com.andy.plugin.model.MethodBean
import com.andy.plugin.model.Params
import com.andy.plugin.scan.MethodTracer
import com.andy.plugin.util.Log
import com.andy.plugin.visitor.MethodVisitorFactory
import com.google.gson.Gson
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class Entry {
    static void main(String[] args) {
        Map<MethodBean, MethodVisitorFactory> methodVisitorMap = new HashMap<>()
//        MethodVisitorFactory xgG46VisitorFactory = new XgG45MethodVisitorFactory()
//        MethodVisitorFactory xgEventDaVisitorFactory = new XgEventDaVisitorFactory()
//        methodVisitorMap.put(ASMTraceTransform.prepareXgG4IntiMethodBean(), xgG46VisitorFactory)
//        methodVisitorMap.put(ASMTraceTransform.prepareXgG5IntiMethodBean(), xgG46VisitorFactory)
//        methodVisitorMap.put(ASMTraceTransform.prepareXgEventDaMethodBean(), xgEventDaVisitorFactory)

        MethodBean addTryCatch = new MethodBean()
        addTryCatch.owner = "com/ParseInteger"
        addTryCatch.name = "parse"
        addTryCatch.desc = "(Ljava/lang/String;)Ljava/lang/Integer;"

        MethodBean emptyReturn = new MethodBean()
        emptyReturn.owner = "com/ParseInteger"
        emptyReturn.name = "log"
        emptyReturn.desc = "(Ljava/lang/String;)V"

        MethodBean emptyReturn1 = new MethodBean()
        emptyReturn1.owner = "com/ParseInteger"
        emptyReturn1.name = "convert"
        emptyReturn1.desc = "(I)I"

        MethodBean booleanCase = new MethodBean()
        booleanCase.owner = "com/ParseInteger"
        booleanCase.name = "convert"
        booleanCase.desc = "(F)Z"

        MethodBean charCase = new MethodBean()
        charCase.owner = "com/ParseInteger"
        charCase.name = "convert"
        charCase.desc = "(Ljava/lang/String;)C"

        MethodBean methodBean = new MethodBean()
        methodBean.owner = 'android/arch/lifecycle/LifecycleRegistry'
        methodBean.name = 'calculateTargetState'
        methodBean.desc = '(Landroid/arch/lifecycle/LifecycleObserver;)Landroid/arch/lifecycle/Lifecycle$State;'


        MethodBean popMethod = new MethodBean()
        popMethod.owner = 'android/arch/lifecycle/LifecycleRegistry'
        popMethod.name = 'popParentState'
        popMethod.desc = '()V'

        //android/arch/lifecycle/LifecycleRegistry#calculateTargetState(Landroid/arch/lifecycle/LifecycleObserver;)Landroid/arch/lifecycle/Lifecycle$State;

        MethodVisitorFactory factory = new MethodVisitorFactory() {
            @Override
            MethodVisitor create(String ownerClassName,
                                 int access,
                                 String name,
                                 String desc,
                                 String signature,
                                 String[] exceptions,
                                 MethodVisitor visitor) {
                return new AddTryCatchMethodVisitor(Opcodes.ASM5, visitor, access, name, desc, ownerClassName)
            }
        }

//        methodVisitorMap.put(addTryCatch, factory)
//        methodVisitorMap.put(emptyReturn, factory)
//        methodVisitorMap.put(emptyReturn1, factory)
//        methodVisitorMap.put(booleanCase, factory)
//        methodVisitorMap.put(charCase, factory)
        methodVisitorMap.put(methodBean, factory)
        methodVisitorMap.put(popMethod, factory)
        Params.methodVisitorMap = methodVisitorMap
//        AsmEntry.scanClassFile(new File('./toscan/g$4.class'))
//        AsmEntry.scanClassFile(new File('./toscan/g$5.class'))
//        AsmEntry.scanClassFile(new File('./toscan/d.class'))

//        Params.methodToScan = new LinkedList<>()
//        Params.methodToScan.add('android/os/HandlerThread#<init>(Ljava/lang/String;)V')

        //27
//        Params.methodToScan.add('com/tencent/android/tpush/common/c# a(Ljava/lang/Runnable;)Z')

        //10
//        Params.methodToScan.add('com/tencent/android/tpush/common/c# a(Ljava/lang/Runnable;J)Z')

        //4
//        Params.methodToScan.add('com/tencent/android/tpush/common/c# a(Ljava/lang/Runnable;IJ)Z')

        Map<File, File> dirToScan = new HashMap<>()
//        dirToScan.put(new File('./toscan/Xg_sdk_4.3.2_20190222_1535.jar'), new File('./after_scan/Xg_sdk_4.3.2_20190222_1535.jar'))
        dirToScan.put(new File('./toscan/lifecycle_runtime.jar'), new File('./after_scan/lifecycle_runtime.jar'))

        Map<File, File> srcToScan = new HashMap<>()
        srcToScan.put(new File('toscan'), new File('after_scan'))

        new MethodTracer().trace(/*srcToScan*/ null, dirToScan)

        Log.i("ScanResult",
                "method scan result:%s",
                new Gson().toJson(Params.methodScannedList))
    }
}
