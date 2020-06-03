package com.andy.plugin.scan

import com.andy.plugin.util.PluginUtil
import org.objectweb.asm.ClassWriter

class AsmEntry {

    static void transformDirrectoryToScan(File directory) {
        directory.eachFileRecurse { File file ->
            String path = file.absolutePath
            if (!File.separator.equals("/")) {
                path = path.replace(File.separator, "/")
            }
            String rootPath = Params.ROOT_DIR_PATH.replace(File.separator, "/") + "/"
            path = path.replace(rootPath, "")
//                Log.i("DirScan", "findOutClassFileToScan, path: " + path)
            if (PluginUtil.isNeedScanClassFile(file) &&
                    PluginUtil.isPathNeedScan(path) &&
                    !PluginUtil.isGeneratedClass(path)) {
                JarScanner.scanClass(file)
            }
        }
    }

    static void scanClassFile(File classFile) {
        ClassWriter classWriter = JarScanner.scanClass(classFile)
        FileOutputStream fos = new FileOutputStream("after_visit_$classFile.name")
        fos.write(classWriter.toByteArray())
        fos.close()
    }

}
