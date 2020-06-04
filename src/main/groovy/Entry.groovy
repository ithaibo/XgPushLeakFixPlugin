import com.andy.aop.fixxgleak.ASMTraceTransform
import com.andy.aop.fixxgleak.XgEventDaVisitorFactory
import com.andy.aop.fixxgleak.XgG45MethodVisitorFactory
import com.andy.plugin.model.MethodBean
import com.andy.plugin.model.Params
import com.andy.plugin.scan.AsmEntry
import com.andy.plugin.scan.MethodTracer
import com.andy.plugin.util.Log
import com.andy.plugin.visitor.MethodVisitorFactory
import com.google.gson.Gson

class Entry {
    static void main(String[] args) {
//        Map<MethodBean, MethodVisitorFactory> methodVisitorMap = new HashMap<>()

//        MethodVisitorFactory xgG46VisitorFactory = new XgG45MethodVisitorFactory()
//        MethodVisitorFactory xgEventDaVisitorFactory = new XgEventDaVisitorFactory()

//        methodVisitorMap.put(ASMTraceTransform.prepareXgG4IntiMethodBean(), xgG46VisitorFactory)
//        methodVisitorMap.put(ASMTraceTransform.prepareXgG5IntiMethodBean(), xgG46VisitorFactory)
//        methodVisitorMap.put(ASMTraceTransform.prepareXgEventDaMethodBean(), xgEventDaVisitorFactory)
//        Params.methodVisitorMap = methodVisitorMap
//
//        AsmEntry.scanClassFile(new File('./toscan/g$4.class'))
//        AsmEntry.scanClassFile(new File('./toscan/g$5.class'))
//        AsmEntry.scanClassFile(new File('./toscan/d.class'))

        Params.methodToScan = new LinkedList<>()
//        Params.methodToScan.add('android/os/HandlerThread#<init>(Ljava/lang/String;)V')

        //27
//        Params.methodToScan.add('com/tencent/android/tpush/common/c# a(Ljava/lang/Runnable;)Z')

        //10
//        Params.methodToScan.add('com/tencent/android/tpush/common/c# a(Ljava/lang/Runnable;J)Z')

        //4
        Params.methodToScan.add('com/tencent/android/tpush/common/c# a(Ljava/lang/Runnable;IJ)Z')

        Map<File, File> dirToScan = new HashMap<>()
        dirToScan.put(new File('./toscan/Xg_sdk_4.3.2_20190222_1535.jar'), new File('./after_scan/Xg_sdk_4.3.2_20190222_1535.jar'))

        new MethodTracer().trace(null, dirToScan)


        Log.i("ScanResult", "method scan result:%s", new Gson().toJson(Params.methodScannedList))
    }
}
