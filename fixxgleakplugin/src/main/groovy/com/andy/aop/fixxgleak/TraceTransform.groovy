package com.andy.aop.fixxgleak

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformTask
import com.andy.plugin.model.FieldBean
import com.andy.plugin.model.MethodBean
import com.andy.plugin.util.Log
import com.andy.plugin.util.Util
import com.andy.plugin.visitor.FieldVisitorFactory
import com.andy.plugin.visitor.MethodVisitorFactory
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionGraph
import org.gradle.api.execution.TaskExecutionGraphListener

import java.lang.reflect.Field


abstract class TraceTransform extends BaseProxyTransform {

    Transform origTransform
    Project project
    def variant

    TraceTransform() {
        super()
    }

    TraceTransform(Project project, def variant, Transform origTransform) {
        super(origTransform)
        this.origTransform = origTransform
        this.variant = variant
        this.project = project
    }

    /**
     * 获取配置 方法ASM处理逻辑
     * @return map key: 需要处理的方法的描述（用于匹配）;value: 方法处理逻辑
     */
    protected abstract Map<MethodBean, MethodVisitorFactory> obtainMethodVisitorMap();

    /**
     * 获取配置 域变量ASM处理逻辑
     * @return key: 需要处理的域变量描述（用于匹配）;value: 域变量处理逻辑
     */
    protected abstract Map<FieldBean, FieldVisitorFactory> obtainFieldVistorMap();

    /**
     * 实际的ASM处理逻辑
     * @param scrInputMap
     * @param jarInputMap
     */
    protected abstract void trace(Map<File, File> scrInputMap, Map<File, File> jarInputMap);


    static void inject(Project project, def variant) {
        String hackTransformTaskName = getTransformTaskName("", "", variant.name)
        String hackTransformTaskNameForWrapper = getTransformTaskName("", "Builder",variant.name)

        project.logger.info("prepare inject dex transform :" + hackTransformTaskName +" hackTransformTaskNameForWrapper:"+hackTransformTaskNameForWrapper)

        project.getGradle().getTaskGraph().addTaskExecutionGraphListener(new TaskExecutionGraphListener() {
            @Override
            void graphPopulated(TaskExecutionGraph taskGraph) {
                for (Task task : taskGraph.getAllTasks()) {
                    if ((task.name.equalsIgnoreCase(hackTransformTaskName) ||
                            task.name.equalsIgnoreCase(hackTransformTaskNameForWrapper))
                            &&
                            !(((TransformTask) task).getTransform() instanceof TraceTransform)) {
                        project.logger.warn("find dex transform. transform class: " + task.transform.getClass() + " . task name: " + task.name)
                        project.logger.info("variant name: $variant.name")
                        Field field = TransformTask.class.getDeclaredField("transform")
                        field.setAccessible(true)
                        field.set(task, new ASMTraceTransform(project, variant, task.transform))
                        project.logger.warn("transform class after hook: " + task.transform.getClass())
                        break
                    }
                }
            }
        })
    }

    @Override
    String getName() {
        return "TraceTransform"
    }

    @Override
    void transform(TransformInvocation transformInvocation)
            throws TransformException,
                    InterruptedException,
                    IOException {
        long start = System.currentTimeMillis()
        final boolean isIncremental = transformInvocation.isIncremental() && this.isIncremental()
        final File rootOutput = new File(project.getBuildDir().getAbsolutePath() + File.separator + "asmoutput",
                "classes/${getName()}/")
        if (!rootOutput.exists()) {
            rootOutput.mkdirs()
        }

        Map<File, File> jarInputMap = new HashMap<>()
        Map<File, File> scrInputMap = new HashMap<>()

        transformInvocation.inputs.each { TransformInput input ->
            input.directoryInputs.each { DirectoryInput dirInput ->
                collectAndIdentifyDir(scrInputMap, dirInput, rootOutput, isIncremental)
            }
            input.jarInputs.each { JarInput jarInput ->
                if (jarInput.getStatus() != Status.REMOVED) {
                    collectAndIdentifyJar(jarInputMap, scrInputMap, jarInput, rootOutput, isIncremental)
                }
            }
        }


        trace(scrInputMap, jarInputMap)
        origTransform.transform(transformInvocation)
        Log.i("ASM." + getName(), "[transform] cost time: %dms", System.currentTimeMillis() - start)
    }



    private static void collectAndIdentifyDir(Map<File, File> dirInputMap,
                                              DirectoryInput input,
                                              File rootOutput,
                                              boolean isIncremental) {
        final File dirInput = input.file
        final File dirOutput = new File(rootOutput, input.file.getName())
        if (!dirOutput.exists()) {
            dirOutput.mkdirs()
        }
        if (isIncremental) {
            if (!dirInput.exists()) {
                dirOutput.deleteDir()
            } else {
                final Map<File, Status> obfuscatedChangedFiles = new HashMap<>()
                final String rootInputFullPath = dirInput.getAbsolutePath()
                final String rootOutputFullPath = dirOutput.getAbsolutePath()
                input.changedFiles.each { Map.Entry<File, Status> entry ->
                    final File changedFileInput = entry.getKey()
                    final String changedFileInputFullPath = changedFileInput.getAbsolutePath()
                    final File changedFileOutput = new File(
                            changedFileInputFullPath.replace(rootInputFullPath, rootOutputFullPath)
                    )
                    final Status status = entry.getValue()
                    switch (status) {
                        case Status.NOTCHANGED:
                            break
                        case Status.ADDED:
                        case Status.CHANGED:
                            dirInputMap.put(changedFileInput, changedFileOutput)
                            break
                        case Status.REMOVED:
                            changedFileOutput.delete()
                            break
                    }
                    obfuscatedChangedFiles.put(changedFileOutput, status)
                }
                replaceChangedFile(input, obfuscatedChangedFiles)
            }
        } else {
            dirInputMap.put(dirInput, dirOutput)
        }
        replaceFile(input, dirOutput)
    }

    private void collectAndIdentifyJar(Map<File, File> jarInputMaps,
                                       Map<File, File> dirInputMaps,
                                       JarInput input,
                                       File rootOutput,
                                       boolean isIncremental) {
        final File jarInput = input.file
        final File jarOutput = new File(rootOutput, getUniqueJarName(jarInput))
        if (Util.isRealZipOrJar(jarInput)) {
            switch (input.status) {
                case Status.NOTCHANGED:
                    if (isIncremental) {
                        break
                    }
                case Status.ADDED:
                case Status.CHANGED:
                    jarInputMaps.put(jarInput, jarOutput)
                    break
                case Status.REMOVED:
                    break
            }
        } else {
            // Special case for WeChat AutoDex. Its rootInput jar file is actually
            // a txt file contains path list.
            BufferedReader br = null
            BufferedWriter bw = null
            try {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(jarInput)))
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(jarOutput)))
                String realJarInputFullPath
                while ((realJarInputFullPath = br.readLine()) != null) {
                    // src jar.
                    final File realJarInput = new File(realJarInputFullPath)
                    // dest jar, moved to extraguard intermediate output dir.
                    File realJarOutput = new File(rootOutput, getUniqueJarName(realJarInput))

                    if (realJarInput.exists() && Util.isRealZipOrJar(realJarInput)) {
                        jarInputMaps.put(realJarInput, realJarOutput)
                    } else {
                        realJarOutput.delete()
                        if (realJarInput.exists() && realJarInput.isDirectory()) {
                            realJarOutput = new File(rootOutput, realJarInput.getName())
                            if (!realJarOutput.exists()) {
                                realJarOutput.mkdirs()
                            }
                            dirInputMaps.put(realJarInput, realJarOutput)
                        }
                    }
                    // write real output full path to the fake jar at rootOutput.
                    final String realJarOutputFullPath = realJarOutput.getAbsolutePath()
                    bw.writeLine(realJarOutputFullPath)
                }
            } catch (FileNotFoundException e) {
                Log.e("ASM." + getName(), "FileNotFoundException:%s", e.toString())
            } finally {
                Util.closeQuietly(br)
                Util.closeQuietly(bw)
            }
            jarInput.delete() // delete raw inputList
        }

        replaceFile(input, jarOutput)
    }


    private static String getTransformTaskName(String customDexTransformName,
                                               String wrappSuffix,
                                               String buildTypeSuffix) {
        if(customDexTransformName != null && customDexTransformName.length() > 0) {
            return customDexTransformName+"For${buildTypeSuffix}"
        }
        return "transformClassesWithDex${wrappSuffix}For${buildTypeSuffix}"
    }

}
