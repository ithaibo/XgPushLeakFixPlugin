package com.andy.plugin.model;

import com.andy.plugin.visitor.FieldVisitorFactory;
import com.andy.plugin.visitor.MethodVisitorFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Params {
    /**path of root project*/
    public static String ROOT_DIR_PATH;

    public static List<InvokeScanned> methodScannedList = new LinkedList<>();
    public static List<InvokeScanned> fieldScannedList = new LinkedList<>();

    public static Map<MethodBean, MethodVisitorFactory> methodVisitorMap;
    public static Map<FieldBean, FieldVisitorFactory> fieldVisitorMap;
    public static List<String> methodToScan;
}
