package cn.savory;

import cn.savory.codedom.CodeWriter;
import cn.savory.codedom.GenerateOptions;
import cn.savory.codedom.java.CodeFile;
import cn.savory.codedom.java.engine.CodeFileEngine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException {

        //String folder = "D:\\Gitlab\\financial-shopping\\fs-onlineshopping-searchservice";
        String folder = "D:\\Gitlab\\Fin\\buyh5service-java";

        File file = new File(folder);

        recursion(file);

        System.out.println("done.");
    }


    private static void recursion(File file) throws IOException {
        if (!file.exists()) {
            return;
        }

        if (file.isDirectory()) {
            if (".git".equalsIgnoreCase(file.getName())) {
                return;
            }

            if ("test".equalsIgnoreCase(file.getName())) {
                return;
            }
        }

        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            return;
        }

        for (File subFile : files) {
            if (subFile.isFile() && subFile.getAbsolutePath().endsWith(".java")) {
                processFile(subFile);
                continue;
            }

            if (subFile.isDirectory()) {
                recursion(subFile);
            }
        }
    }

    private static void processFile(File file) throws IOException {

        CodeFileEngine codeFileEngine = new CodeFileEngine();

        JavaParser javaParser = new JavaParser();

        CodeFile codeFile = javaParser.parseJavaFiles(file.getAbsolutePath());
        if (codeFile.getPackages() == null || codeFile.getPackages().stream().noneMatch(v -> v.getCodeClassList() != null && !v.getCodeClassList().isEmpty())) {
            return;
        }

        String targetFilePath = file.getAbsolutePath()
                .replace("\\main\\", "\\test\\")
                .replace(".java", "SimpleTest.java");
        File targetFileParent = new File(targetFilePath).getParentFile();
        if (!targetFileParent.exists() && !targetFileParent.mkdirs()) {
            System.out.println("!targetFileParent.exists() && !targetFileParent.mkdirs()");
        }

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(targetFilePath));

        codeFileEngine.generateFile(codeFile, new CodeWriter(bufferedWriter), new GenerateOptions());

        bufferedWriter.close();
    }
}
