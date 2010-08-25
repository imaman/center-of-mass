package com.blogspot.javadots.cexplorer;

import java.io.IOException;
import java.io.InputStream;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;


public class MethodInspector {

   private Collector collector;

   public MethodInspector(Collector c) {
      this.collector = c;
   }

   public void inspect(InputStream classfile) {
      try {
         inspectProtected(classfile);
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }
   
   
   public void inspectProtected(InputStream classfile) throws IOException {
      ClassReader cr = new ClassReader(classfile);
      ClassVisitor cv = new ClassVisitor()
      {

         @Override
         public void visit(int version, int access, String name,
            String signature, String superName, String[] interfaces) {
         }

         @Override
         public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            return null;
         }

         @Override
         public void visitAttribute(Attribute attr) {
         }

         @Override
         public void visitEnd() {
         }

         @Override
         public FieldVisitor visitField(int access, String name, String desc,
            String signature, Object value) {
            return null;
         }

         @Override
         public void visitInnerClass(String name, String outerName,
            String innerName, int access) {
         }

         @Override
         public MethodVisitor visitMethod(int access, String name, String desc,
            String signature, String[] exceptions) {
            return new InstructionCounter(name, collector);
         }
         
         @Override
         public void visitOuterClass(String owner, String name, String desc) {
         }

         @Override
         public void visitSource(String source, String debug) {
         }
         
      };
      
      cr.accept(cv, 0);
   }
   
   private static class InstructionCounter implements MethodVisitor {

      public int size;
      private Collector collector;
      private String name;
      
      public InstructionCounter(String name, Collector collector) {
         this.name = name;
         this.collector = collector;
      }

      @Override
      public AnnotationVisitor visitAnnotation(String desc,
         boolean visible) {
         return null;
      }

      @Override
      public AnnotationVisitor visitAnnotationDefault() {
         return null;
      }

      @Override
      public void visitAttribute(Attribute attr) {
      }

      @Override
      public void visitCode() {
      }

      @Override
      public void visitEnd() {
         collector.collect(name, size);
      }

      @Override
      public void visitFieldInsn(int opcode, String owner, String name_,
         String desc) {
         ++size;
      }

      @Override
      public void visitFrame(int type, int nLocal, Object[] local,
         int nStack, Object[] stack) {
      }

      @Override
      public void visitIincInsn(int var, int increment) {
         ++size;
      }

      @Override
      public void visitInsn(int opcode) {
         ++size;
      }

      @Override
      public void visitIntInsn(int opcode, int operand) {
         ++size;
      }

      @Override
      public void visitJumpInsn(int opcode, Label label) {
         ++size;
      }

      @Override
      public void visitLabel(Label label) {
      }

      @Override
      public void visitLdcInsn(Object cst) {
         ++size;
      }

      @Override
      public void visitLineNumber(int line, Label start) {
      }

      @Override
      public void visitLocalVariable(String name_, String desc,
         String signature, Label start, Label end, int index) {
      }

      @Override
      public void visitLookupSwitchInsn(Label dflt, int[] keys,
         Label[] labels) {
         ++size;
      }

      @Override
      public void visitMaxs(int maxStack, int maxLocals) {
      }

      @Override
      public void visitMethodInsn(int opcode, String owner, String name_,
         String desc) {
         ++size;
      }

      @Override
      public void visitMultiANewArrayInsn(String desc, int dims) {
         ++size;
      }

      @Override
      public AnnotationVisitor visitParameterAnnotation(int parameter,
         String desc, boolean visible) {
         return null;
      }

      @Override
      public void visitTableSwitchInsn(int min, int max, Label dflt,
         Label[] labels) {
         ++size;
      }

      @Override
      public void visitTryCatchBlock(Label start, Label end,
         Label handler, String type) {
      }

      @Override
      public void visitTypeInsn(int opcode, String type) {
         ++size;
      }

      @Override
      public void visitVarInsn(int opcode, int var) {
         ++size;
      }
   }
   
}
