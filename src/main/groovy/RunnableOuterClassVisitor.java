import com.andy.plugin.util.Log;
import org.objectweb.asm.ClassVisitor;

import java.util.Objects;

public class RunnableOuterClassVisitor extends ClassVisitor {
    private final String TAG = "RunnableOuterClassScan";
    public RunnableOuterClassVisitor(int api) {
        super(api);
    }

    public RunnableOuterClassVisitor(int api, ClassVisitor cv) {
        super(api, cv);
    }


    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        if (null == interfaces || interfaces.length <= 0) {
            return;
        }
        final String runnable = "java/lang/Runnable";
        boolean impl = false;
        for (String interfaceName : interfaces) {
            if (Objects.equals(interfaceName, runnable)) {
                impl = true;
                break;
            }
        }
        if (!impl) {
            return;
        }
        Log.i(TAG, "find one implements Runnable:%s", name);
    }
}
