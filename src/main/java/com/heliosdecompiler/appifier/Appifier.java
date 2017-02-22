/*
 * Copyright 2017 Sam Sun <github-contact@samczsun.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Copyright 2017 Sam Sun <github-contact@samczsun.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.heliosdecompiler.appifier;

import org.apache.commons.io.IOUtils;
import org.objectweb.asm.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class Appifier {
    public static void main(String[] args) throws Throwable {
        if (args.length == 0) {
            System.out.println("An input JAR must be specified");
            return;
        }

        File in = new File(args[0]);

        if (!in.exists()) {
            System.out.println("Input not found");
            return;
        }

        String outName = args[0];
        outName = outName.substring(0, outName.length() - ".jar".length()) + "-appified.jar";

        File out = new File(outName);

        if (out.exists()) {
            if (!out.delete()) {
                System.out.println("Could not delete out file");
                return;
            }
        }

        try (ZipOutputStream outstream = new ZipOutputStream(new FileOutputStream(out));
             ZipFile zipFile = new ZipFile(in)) {

            {
                ZipEntry systemHook = new ZipEntry("com/heliosdecompiler/appifier/SystemHook.class");
                outstream.putNextEntry(systemHook);
                outstream.write(SystemHookDump.dump());
                outstream.closeEntry();
            }

            Enumeration<? extends ZipEntry> enumeration = zipFile.entries();

            while (enumeration.hasMoreElements()) {
                ZipEntry next = enumeration.nextElement();

                if (!next.isDirectory()) {
                    ZipEntry result = new ZipEntry(next.getName());
                    outstream.putNextEntry(result);
                    if (next.getName().endsWith(".class")) {
                        byte[] classBytes = IOUtils.toByteArray(zipFile.getInputStream(next));
                        outstream.write(transform(classBytes));
                    } else {
                        IOUtils.copy(zipFile.getInputStream(next), outstream);
                    }
                    outstream.closeEntry();
                }
            }
        }
    }

    private static byte[] transform(byte[] classBytes) {
        ClassReader reader = new ClassReader(classBytes);
        ClassWriter writer = new ClassWriter(Opcodes.ASM5);

        reader.accept(new ClassVisitor(Opcodes.ASM5, writer) {
            @Override
            public MethodVisitor visitMethod(int i, String s, String s1, String s2, String[] strings) {
                return new MethodVisitor(Opcodes.ASM5, writer.visitMethod(i, s, s1, s2, strings)) {
                    @Override
                    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
                        if (opcode == Opcodes.GETSTATIC) {
                            if (owner.equals("java/lang/System")) {
                                if (desc.equals("Ljava/io/PrintStream;")) {
                                    if (name.equals("out")) {
                                        super.visitFieldInsn(Opcodes.GETSTATIC, "com/heliosdecompiler/appifier/SystemHook", "out", "Ljava/lang/ThreadLocal;");
                                        super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/ThreadLocal", "get", "()Ljava/lang/Object;", false);
                                        super.visitTypeInsn(Opcodes.CHECKCAST, "java/io/PrintStream");
                                        return;
                                    } else if (name.equals("err")) {
                                        super.visitFieldInsn(Opcodes.GETSTATIC, "com/heliosdecompiler/appifier/SystemHook", "err", "Ljava/lang/ThreadLocal;");
                                        super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/ThreadLocal", "get", "()Ljava/lang/Object;", false);
                                        super.visitTypeInsn(Opcodes.CHECKCAST, "java/io/PrintStream");
                                        return;
                                    }
                                }
                            }
                        }
                        super.visitFieldInsn(opcode, owner, name, desc);
                    }
                };
            }
        }, 0);

        return writer.toByteArray();
    }
}
