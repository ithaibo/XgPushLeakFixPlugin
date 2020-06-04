package com.andy.plugin.scan

import com.andy.plugin.util.Log
import com.andy.plugin.util.PluginUtil
import com.andy.plugin.util.Util
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

class JarScanner {
    private static String TAG = "JarScanner"


    /**
     * 扫描并处理jar中的类
     * @param input 待扫描的jar
     * @param output 处理后的jar
     */
    static void innerTraceMethodFromJar(File input, File output) {
        ZipOutputStream zipOutputStream = null
        ZipFile zipFile = null
        try {
            zipOutputStream = new ZipOutputStream(new FileOutputStream(output))
            zipFile = new ZipFile(input)
            Enumeration<? extends ZipEntry> enumeration = zipFile.entries()
            while (enumeration.hasMoreElements()) {
                ZipEntry zipEntry = enumeration.nextElement()
                String zipEntryName = zipEntry.getName()
                if (PluginUtil.isJarNeedScan(zipEntryName)) {
                    InputStream inputStream = zipFile.getInputStream(zipEntry)
                    ClassWriter classWriter = scanClass(inputStream)
                    byte[] data = classWriter.toByteArray()
                    InputStream byteArrayInputStream = new ByteArrayInputStream(data)
                    ZipEntry newZipEntry = new ZipEntry(zipEntryName)
                    Util.addZipEntry(zipOutputStream, newZipEntry, byteArrayInputStream)
                } else {
                    InputStream inputStream = zipFile.getInputStream(zipEntry)
                    ZipEntry newZipEntry = new ZipEntry(zipEntryName)
                    Util.addZipEntry(zipOutputStream, newZipEntry, inputStream)
                }
            }
        } catch (Exception e) {
            e.printStackTrace()
            Log.e(TAG, "[traceMethodFromJar] err! %s", output.getAbsolutePath())
        } finally {
            try {
                if (zipOutputStream != null) {
                    zipOutputStream.finish()
                    zipOutputStream.flush()
                    zipOutputStream.close()
                }
                if (zipFile != null) {
                    zipFile.close()
                }
            } catch (Exception ignore) {
                Log.e(TAG, "close stream err!")
            }
        }
    }


    /**
     * 扫描并处理单个class文件
     * @param file class文件
     * @return 处理后的class文件
     */
    static ClassWriter scanClass(File file) {
        if(null == file || !file.isFile() || !file.exists()) {
            return
        }
        FileInputStream fis = new FileInputStream(file)
        return scanClass(fis)
    }


    /**
     * 根据输入的class，返回对应处理的ClassWriter
     * @param inputStream class文件内容
     * @return 处理class的ClassWriter
     */
    static ClassWriter scanClass(InputStream inputStream) {
        ClassReader classReader = new ClassReader(inputStream)
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS)
        ClassVisitor classVisitor = new CommonClassVisitor(Opcodes.ASM5, classWriter)
        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
        return classWriter
    }
}