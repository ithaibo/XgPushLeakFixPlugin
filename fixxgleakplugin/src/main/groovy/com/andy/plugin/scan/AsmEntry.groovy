package com.andy.plugin.scan

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
            if (com.andy.plugin.util.PluginUtil.isNeedScanClassFile(file) &&
                    com.andy.plugin.util.PluginUtil.isPathNeedScan(path) &&
                    !com.andy.plugin.util.PluginUtil.isGeneratedClass(path)) {
                JarScanner.scanClass(file)
            }
        }
    }

    static void scanClassFile(File classFile) {
        JarScanner.scanClass(classFile)
    }


    static void transformJarEntryToScan(File srcJar) {
        JarScanner.scanJar(srcJar)
    }
}
