import com.andy.asm.runtime.MethodTimeCostResult;
import com.andy.plugin.util.Log;

public class TimeCostPrinter {
    public static void print(MethodTimeCostResult result) {
        Log.i("MethodTimeCost",
                "class:%s\nmethod:%s\ndesc:%s\nstar:%l\nend:%l",
                result.owner,
                result.name,
                result.desc,
                result.timestampStart,
                result.timestampEnd);
    }
}
