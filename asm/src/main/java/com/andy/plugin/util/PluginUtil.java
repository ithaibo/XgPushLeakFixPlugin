package com.andy.plugin.util;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PluginUtil {
    static Map<String, String> asmJavaBasicMap = new HashMap();
    static {
        asmJavaBasicMap.put("Z","boolean");
        asmJavaBasicMap.put("B","byte");
        asmJavaBasicMap.put("C","char");
        asmJavaBasicMap.put("S","short");
        asmJavaBasicMap.put("I","int");
        asmJavaBasicMap.put("J","long");
        asmJavaBasicMap.put("F","float");
        asmJavaBasicMap.put("D","double");
    }

// todo move   static boolean isApp(@NonNull Project project) {
//        return project.plugins.hasPlugin(AppPlugin)
//    }

    public static String getJavaBasicTypeByAsm(String asmType) {
        if (null == asmType) {
            return null;
        }
        return asmJavaBasicMap.get(asmType);
    }

    public static String cleanAsmMethodDesc(String desc) {
        if (null == desc || desc.isEmpty()) {
            return "";
        }

        int left = desc.indexOf('(');
        int right = desc.indexOf(')');
        String descClean = desc.substring(left + 1, right);
        if ((left + 1) == right) {
//            Log.i("PluginUtil", "desc:" + desc.substring(0, right+1))
            return desc.substring(0, right+1);
        }

        StringBuilder sb = new StringBuilder();
//        Log.i("PluginUtil", "desc:" + descClean)

        String[] paramsType = descClean.split(";");
        if (null != paramsType) {
            for (int i = 0; i < paramsType.length; i++) {
                String param = paramsType[i];
                if (null != param && param.length() < 1) {
                    continue;
                } else if (param.length() == 1) {
                    String type = getJavaBasicTypeByAsm(param);
                    if (null != type) {
                        sb.append(type);
                    } else {
                        sb.append(param);
                    }
                } else {
                    sb.append(param);
                }
                if (i != (paramsType.length - 1)) {
                    sb.append(";");
                }
            }
        }
//        Log.i("PluginUtil", "desc after clean:" + String.format("(%s)", sb.toString()))
        return String.format("(%s)", sb.toString());
    }

    public static boolean isNeedScanClassFile(File file) {
        if (null == file) return false;

        if (!file.isFile()) return false;

        if (!file.getAbsolutePath().endsWith(".class")) return false;

        return true;
    }

    public static boolean isPathNeedScan(String path) {
        if (null == path || path.isEmpty()) return false;
        return true;
    }

    public static boolean isGeneratedClass(String path) {
        if (null == path || path.isEmpty()) {
            return false;
        }
        if (path.endsWith("BuildConfig.class")) {
            return true;
        }
        if (path.endsWith("R.class")) {
            return true;
        }
        int index = path.lastIndexOf("/");
        if (path.length() -1 > index) {
            String subStr = path.substring(index+1);
            if (subStr.startsWith("R$")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isJarNeedScan(String name) {
        if (null == name || name.isEmpty()) {
            return false;
        }

        //TODO refactor 提供配置功能
        if (name.endsWith(".class") && !isGeneratedClass(name)) {
            return true;
        }

        return false;
    }

    public static void dumpJsonFile(Object obj, String fileName) {
        String json = new Gson().toJson(obj);

        File file = new File(fileName);
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(json);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}