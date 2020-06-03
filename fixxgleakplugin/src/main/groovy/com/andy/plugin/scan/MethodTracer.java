/*
 * Tencent is pleased to support the open source community by making wechat-matrix available.
 * Copyright (C) 2018 THL A29 Limited, a Tencent company. All rights reserved.
 * Licensed under the BSD 3-Clause License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.andy.plugin.scan;

import org.objectweb.asm.ClassWriter;
import com.andy.plugin.util.Log;
import com.andy.plugin.util.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by caichongyang on 2017/6/4.
 * <p>
 * This class hooks all collected methods in oder to trace method in/out.
 * </p>
 */

public class MethodTracer {

    private static final String TAG = "Matrix.MethodTracer";
    private static AtomicInteger traceMethodCount = new AtomicInteger();

    public MethodTracer() {
    }


    public void trace(Map<File, File> srcFolderList, Map<File, File> dependencyJarList) {
        traceMethodFromSrc(srcFolderList);
        traceMethodFromJar(dependencyJarList);
    }

    private void traceMethodFromSrc(Map<File, File> srcMap) {
        if (null != srcMap) {
            for (Map.Entry<File, File> entry : srcMap.entrySet()) {
                innerTraceMethodFromSrc(entry.getKey(), entry.getValue());
            }
        }
    }

    private void traceMethodFromJar(Map<File, File> dependencyMap) {
        if (null != dependencyMap) {
            for (Map.Entry<File, File> entry : dependencyMap.entrySet()) {
                innerTraceMethodFromJar(entry.getKey(), entry.getValue());
            }
        }
    }

    private void innerTraceMethodFromSrc(File input, File output) {

        ArrayList<File> classFileList = new ArrayList<>();
        if (input.isDirectory()) {
            listClassFiles(classFileList, input);
        } else {
            classFileList.add(input);
        }

        for (File classFile : classFileList) {
            InputStream is = null;
            FileOutputStream os = null;
            try {
                final String changedFileInputFullPath = classFile.getAbsolutePath();
                final File changedFileOutput = new File(changedFileInputFullPath.replace(input.getAbsolutePath(), output
                                                                                                                    .getAbsolutePath()));
                if (!changedFileOutput.exists()) {
                    changedFileOutput.getParentFile().mkdirs();
                }
                changedFileOutput.createNewFile();

                if (isNeedTraceClassInSrc(classFile)) {
                    ClassWriter classWriter = JarScanner.scanClass(classFile);
                    if (output.isDirectory()) {
                        os = new FileOutputStream(changedFileOutput);
                    } else {
                        os = new FileOutputStream(output);
                    }
                    os.write(classWriter.toByteArray());
                    os.close();
                } else {
                    Util.copyFileUsingStream(classFile, changedFileOutput);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Util.closeQuietly(is);
                Util.closeQuietly(os);
            }
        }
    }

    private void innerTraceMethodFromJar(File input, File output) {
        JarScanner.innerTraceMethodFromJar(input, output);
    }

    private void listClassFiles(ArrayList<File> classFiles, File folder) {
        File[] files = folder.listFiles();
        if (null == files) {
            Log.e(TAG, "[listClassFiles] files is null! %s", folder.getAbsolutePath());
            return;
        }
        for (File file : files) {
            if (file == null) {
                continue;
            }
            if (file.isDirectory()) {
                listClassFiles(classFiles, file);
            } else {
                if (null != file && file.isFile()) {
                    classFiles.add(file);
                }

            }
        }
    }

    public boolean isNeedTraceClassInJar(String fileName) {
        boolean isClass = fileName.endsWith(".class");
//        boolean isBizCode = fileName.startsWith("com/sample/asm");
//        boolean isBizCode = fileName.startsWith("com/howbuy/piggy");
//        return isClass && isBizCode;
        return isClass;
    }

    private boolean isNeedTraceClassInSrc(File file) {
        if (null == file) {
            return false;
        }

        boolean result = false;
        boolean isClass = file.getName().endsWith(".class");
        if (isClass) {
//            String filePath = file.getPath();
//            if (filePath.contains(convertPackageToPath("com.howbuy.piggy"))) {
//                result = true;
//            }
            result = true;
        }
        return result;
    }

    private static String convertPackageToPath(String packageName) {
        if (null == packageName || packageName.isEmpty()) {
            return packageName;
        }
        return packageName.replace(".", "\\");
    }
}
