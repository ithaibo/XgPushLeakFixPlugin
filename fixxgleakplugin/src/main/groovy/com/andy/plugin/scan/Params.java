package com.andy.plugin.scan;

import com.andy.plugin.model.InvokeScanned;

import java.util.LinkedList;
import java.util.List;

public class Params {
    /**path of root project*/
    public static String ROOT_DIR_PATH;
    public static List<String> methodToScan = new LinkedList<>();
    static {

        methodToScan.add("com/andy/test/Demo#log(Ljava/lang/String;[Ljava/lang/Object;)V");
    }

    public static String[] pathScanArray = new String[]{"E:/labs/ScanPlugin/app/src/main/java/com/andy"};

    public static List<InvokeScanned> methodScannedList = new LinkedList<>();
    public static List<InvokeScanned> fieldScannedList = new LinkedList<>();
}
