import com.andy.plugin.scan.AsmEntry
import com.andy.plugin.scan.JarScanner

class Entry {
    static void main(String[] args) {
        AsmEntry.scanClassFile(new File("g\$4.class"))
        AsmEntry.scanClassFile(new File("g\$5.class"))
        AsmEntry.scanClassFile(new File("d.class"))
//        JarScanner.innerTraceMethodFromJar(
//                new File('Xg_sdk_4.3.2_20190222_1535.jar'),
//                new File('Xg_sdk_4.3.2_after_fix.jar')
//        )
    }
}
