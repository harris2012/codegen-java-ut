package cn.savory;

import cn.savory.codedom.java.*;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.JavacTask;
import com.sun.tools.javac.api.JavacTool;
import com.sun.tools.javac.file.JavacFileManager;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class JavaParser {


    public CodeFile parseJavaFiles(String path) throws IOException {

        CodeFile codeFile = new CodeFile();

        Context context = new Context();

        JavacFileManager fileManager = new JavacFileManager(context, true, Charset.defaultCharset());

        JavacTool javacTool = JavacTool.create();

        Iterable<? extends JavaFileObject> files = fileManager.getJavaFileObjects(path);

        JavaCompiler.CompilationTask compilationTask = javacTool.getTask(null, fileManager, null, null, null, files);

        Iterable<? extends CompilationUnitTree> result = ((JavacTask) compilationTask).parse();

        for (CompilationUnitTree compilationUnitTree : result) {
            if (compilationUnitTree == null) {
                System.out.println("compilationUnitTree is null.");
                continue;
            }
            if (compilationUnitTree.getPackageName() == null) {
                System.out.println("compilationUnitTree.getPackageName() is null.");
                continue;
            }
            preparePackage(codeFile, compilationUnitTree);
        }

        return codeFile;
    }

    private void preparePackage(CodeFile codeFile, CompilationUnitTree compilationUnitTree) {
        CodePackage codePackage = codeFile.addPackage(compilationUnitTree.getPackageName().toString());

        List<? extends ImportTree> importTrees = compilationUnitTree.getImports();
        if (importTrees != null && !importTrees.isEmpty()) {
            for (ImportTree importTree : importTrees) {
                JCTree.JCImport jcImport = (JCTree.JCImport) importTree;
                codePackage.addImport(jcImport.getQualifiedIdentifier().toString());
            }
        }

        List<? extends Tree> typeDecls = compilationUnitTree.getTypeDecls();
        for (Tree tree : typeDecls) {
            if (tree.getKind() == Tree.Kind.CLASS) {
                JCTree.JCClassDecl jcClassDecl = (JCTree.JCClassDecl) tree;
                CodeClass codeClass = codePackage.addClass(jcClassDecl.getSimpleName().toString());

                JCTree.JCModifiers jcModifiers = jcClassDecl.getModifiers();
                if (jcModifiers != null) {
                    com.sun.tools.javac.util.List<JCTree.JCAnnotation> annotations = jcModifiers.getAnnotations();
                    for (JCTree.JCAnnotation jcAnnotation : annotations) {
                        JCTree.JCIdent annotationType = (JCTree.JCIdent) jcAnnotation.getAnnotationType();
                        if ("Component".equalsIgnoreCase(annotationType.getName().toString())) {
                            codeClass.setSpringComponent(true);
                        }
                    }
                }

                String modifiers = jcClassDecl.getModifiers().toString();
                if (modifiers.contains("abstract")) {
                    codeClass.setAbstractClass(true);
                }

                prepareClass(codeClass, jcClassDecl);
            }
        }
    }

    private void prepareClass(CodeClass codeClass, JCTree.JCClassDecl jcClassDecl) {
        for (JCTree jcTree : jcClassDecl.getMembers()) {
            switch (jcTree.getKind()) {
                case VARIABLE:
                    JCTree.JCVariableDecl jcVariableDecl = (JCTree.JCVariableDecl) jcTree;
                    break;
                case METHOD:
                    JCTree.JCMethodDecl jcMethodDecl = (JCTree.JCMethodDecl) jcTree;
                    CodeMethod codeMethod = new CodeMethod();
                    codeMethod.setName(jcMethodDecl.getName().toString());
                    if ("<init>".equalsIgnoreCase(codeMethod.getName())) {
                        codeClass.addConstructor(codeMethod);
                    } else {
                        codeClass.addMethod(codeMethod);
                    }

                    if (jcMethodDecl.getThrows() != null && !jcMethodDecl.getThrows().isEmpty()) {
                        for (JCTree.JCExpression aThrow : jcMethodDecl.getThrows()) {
                            JCTree.JCIdent jcIdent = (JCTree.JCIdent) aThrow;
                            codeMethod.addThrow(jcIdent.getName().toString());
                        }
                    }

                    String modifiers = jcMethodDecl.getModifiers().toString();
                    if (modifiers.contains("static")) {
                        codeMethod.setStaticMethod(true);
                    }
                    if (modifiers.contains("private")) {
                        codeMethod.setAccessModifiers(AccessModifiers.Private);
                    }

                    if (jcMethodDecl.getParameters() != null && !jcMethodDecl.getParameters().isEmpty()) {
                        for (JCTree.JCVariableDecl parameter : jcMethodDecl.getParameters()) {
                            CodeParameter codeParameter = codeMethod.addParameter();
                            codeParameter.setName(parameter.getName().toString());
                            codeParameter.setType(parameter.getType().toString());
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }
}