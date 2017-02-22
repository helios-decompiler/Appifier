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

import org.objectweb.asm.*;

public class SystemHookDump implements Opcodes {

    public static byte[] dump() {

        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av0;

        cw.visit(52, ACC_PUBLIC + ACC_SUPER, "com/heliosdecompiler/appifier/SystemHook", null, "java/lang/Object", null);

        cw.visitInnerClass("java/lang/invoke/MethodHandles$Lookup", "java/lang/invoke/MethodHandles", "Lookup", ACC_PUBLIC + ACC_FINAL + ACC_STATIC);

        {
            fv = cw.visitField(ACC_PUBLIC + ACC_STATIC, "out", "Ljava/lang/ThreadLocal;", "Ljava/lang/ThreadLocal<Ljava/io/PrintStream;>;", null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PUBLIC + ACC_STATIC, "err", "Ljava/lang/ThreadLocal;", "Ljava/lang/ThreadLocal<Ljava/io/PrintStream;>;", null);
            fv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PRIVATE + ACC_STATIC + ACC_SYNTHETIC, "lambda$static$1", "()Ljava/io/PrintStream;", null, null);
            mv.visitCode();
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");
            mv.visitInsn(ARETURN);
            mv.visitMaxs(1, 0);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PRIVATE + ACC_STATIC + ACC_SYNTHETIC, "lambda$static$0", "()Ljava/io/PrintStream;", null, null);
            mv.visitCode();
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitInsn(ARETURN);
            mv.visitMaxs(1, 0);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
            mv.visitCode();
            mv.visitInvokeDynamicInsn("get", "()Ljava/util/function/Supplier;", new Handle(Opcodes.H_INVOKESTATIC, "java/lang/invoke/LambdaMetafactory", "metafactory", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;"), new Object[]{Type.getType("()Ljava/lang/Object;"), new Handle(Opcodes.H_INVOKESTATIC, "com/heliosdecompiler/appifier/SystemHook", "lambda$static$0", "()Ljava/io/PrintStream;"), Type.getType("()Ljava/io/PrintStream;")});
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/ThreadLocal", "withInitial", "(Ljava/util/function/Supplier;)Ljava/lang/ThreadLocal;", false);
            mv.visitFieldInsn(PUTSTATIC, "com/heliosdecompiler/appifier/SystemHook", "out", "Ljava/lang/ThreadLocal;");
            mv.visitInvokeDynamicInsn("get", "()Ljava/util/function/Supplier;", new Handle(Opcodes.H_INVOKESTATIC, "java/lang/invoke/LambdaMetafactory", "metafactory", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;"), new Object[]{Type.getType("()Ljava/lang/Object;"), new Handle(Opcodes.H_INVOKESTATIC, "com/heliosdecompiler/appifier/SystemHook", "lambda$static$1", "()Ljava/io/PrintStream;"), Type.getType("()Ljava/io/PrintStream;")});
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/ThreadLocal", "withInitial", "(Ljava/util/function/Supplier;)Ljava/lang/ThreadLocal;", false);
            mv.visitFieldInsn(PUTSTATIC, "com/heliosdecompiler/appifier/SystemHook", "err", "Ljava/lang/ThreadLocal;");
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 0);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }
}