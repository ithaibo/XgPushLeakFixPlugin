import com.andy.asm.extention.timecost.AddTimeTraceMethodVisitor;
import com.andy.asm.extention.trycat.AddTryCatchMethodVisitor;
import com.andy.asm.extention.TraceResultConsumer;
import com.andy.plugin.model.ClassBean;
import com.andy.plugin.model.MethodBean;
import com.andy.plugin.model.Params;
import com.andy.plugin.scan.MethodTracer;
import com.andy.plugin.util.Log;
import com.andy.plugin.visitor.ClassInfoReader;
import com.andy.plugin.visitor.MethodVisitorFactory;
import com.google.gson.Gson;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

public class Sample {

    public static void main(String[] args) {
        Map<MethodBean, MethodVisitorFactory> methodVisitorMap = new HashMap<>();

        MethodBean addTryCatch = new MethodBean();
        addTryCatch.owner = "com/ParseInteger";
        addTryCatch.name = "parse";
        addTryCatch.desc = "(Ljava/lang/String;)Ljava/lang/Integer;";

        MethodBean emptyReturn = new MethodBean();
        emptyReturn.owner = "com/ParseInteger";
        emptyReturn.name = "log";
        emptyReturn.desc = "(Ljava/lang/String;)V";

        MethodBean emptyReturn1 = new MethodBean();
        emptyReturn1.owner = "com/ParseInteger";
        emptyReturn1.name = "convert";
        emptyReturn1.desc = "(I)I";

        MethodBean booleanCase = new MethodBean();
        booleanCase.owner = "com/ParseInteger";
        booleanCase.name = "convert";
        booleanCase.desc = "(F)Z";

        MethodBean charCase = new MethodBean();
        charCase.owner = "com/ParseInteger";
        charCase.name = "convert";
        charCase.desc = "(Ljava/lang/String;)C";

        MethodBean methodBean = new MethodBean();
        methodBean.owner = "android/arch/lifecycle/LifecycleRegistry";
        methodBean.name = "calculateTargetState";
        methodBean.desc = "(Landroid/arch/lifecycle/LifecycleObserver;)Landroid/arch/lifecycle/Lifecycle$State;";


        MethodBean popMethod = new MethodBean();
        popMethod.owner = "android/arch/lifecycle/LifecycleRegistry";
        popMethod.name = "popParentState";
        popMethod.desc = "()V";

        MethodVisitorFactory factory = (ownerClassName,
                                        access,
                                        name,
                                        desc,
                                        signature,
                                        exceptions,
                                        visitor) -> {
            TraceResultConsumer exceptionHandlerBean = new TraceResultConsumer();
            exceptionHandlerBean.ownerClassname = "PrintException";
            exceptionHandlerBean.methodName = "printStackTrace";
            exceptionHandlerBean.methodDesc = "(Ljava/lang/Exception;)V";
            exceptionHandlerBean.interfaceMethod = false;
            AddTryCatchMethodVisitor tryCatchMethodVisitor = new AddTryCatchMethodVisitor(Opcodes.ASM5,
                    visitor,
                    access,
                    name,
                    desc,
                    ownerClassName,
                    exceptionHandlerBean);


            TraceResultConsumer timeCostConsumer = new TraceResultConsumer();
            timeCostConsumer.ownerClassname = "TimeCostPrinter";
            timeCostConsumer.methodName = "printTimeCost";
            timeCostConsumer.methodDesc = "(Lcom/andy/asm/runtime/MethodTimeCostResult;)V";
            timeCostConsumer.interfaceMethod = false;
            timeCostConsumer.accessPublicStatic = true;
            return new AddTimeTraceMethodVisitor(Opcodes.ASM5,
                    tryCatchMethodVisitor,
                    access,
                    name,
                    desc,
                    ownerClassName,
                    timeCostConsumer);
        };


//        methodVisitorMap.put(addTryCatch, factory)
//        methodVisitorMap.put(emptyReturn, factory)
//        methodVisitorMap.put(emptyReturn1, factory)
//        methodVisitorMap.put(booleanCase, factory)
//        methodVisitorMap.put(charCase, factory)
        methodVisitorMap.put(methodBean, factory);
        methodVisitorMap.put(popMethod, factory);


        Params.methodVisitorMap = methodVisitorMap;
        Params.classInfoReaders = new LinkedList<>();
        Params.classInfoReaders.add(new RunnableOuterImplReader());


        Map<File, File> dirToScan = new HashMap<>();
        dirToScan.put(new File("./toscan/lifecycle_runtime.jar"), new File("./after_scan/lifecycle_runtime.jar"));

        Map<File, File> srcToScan = new HashMap<>();
        srcToScan.put(new File("toscan"), new File("after_scan"));

        new MethodTracer().trace(/*srcToScan*/ null, dirToScan);

        Log.i("ScanResult",
                "method scan result:%s",
                new Gson().toJson(Params.methodScannedList));
    }

    public static class RunnableOuterImplReader implements ClassInfoReader {
        private String TAG = "RunnableOuterImpl";

        @Override
        public void read(ClassBean classBean) {
            if (null == classBean) return;

            if (null == classBean.interfaces || classBean.interfaces.length <= 0) {
                return;
            }
            final String runnable = "java/lang/Runnable";
            boolean impl = false;
            for (String interfaceName : classBean.interfaces) {
                if (Objects.equals(interfaceName, runnable)) {
                    impl = true;
                    break;
                }
            }
            if (!impl) {
                return;
            }
            if (!classBean.name.contains("\\$")) {
                Log.i(TAG, "find one implements Runnable:%s", classBean.name);
            }
        }
    }

}
