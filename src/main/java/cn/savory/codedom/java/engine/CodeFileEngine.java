package cn.savory.codedom.java.engine;

import cn.savory.codedom.CodeWriter;
import cn.savory.codedom.GenerateOptions;
import cn.savory.codedom.Marks;
import cn.savory.codedom.StringExtension;
import cn.savory.codedom.java.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class CodeFileEngine {

    public void generateFile(CodeFile codeFile, CodeWriter codeWriter, GenerateOptions options) throws IOException {
        if (codeFile.getPackages() == null || codeFile.getPackages().isEmpty()) {
            return;
        }
        for (CodePackage $package : codeFile.getPackages()) {
            generatePackage($package, codeWriter, options);
        }
    }

    public void generatePackage(CodePackage codePackage, CodeWriter codeWriter, GenerateOptions options) throws IOException {
        codeWriter.write(Keywords.PACKAGE).write(Marks.WHITESPACE).write(codePackage.getName()).writeLine(Marks.SEMICOLON);

        codeWriter.writeLine();
        codeWriter.writeLine("/*");
        codeWriter.writeLine(" *------------------------------------------------------------------------------");
        codeWriter.writeLine(" *     DO NOT GO GENTLE INTO THAT GOOD NIGHT.");
        codeWriter.writeLine(" *");
        codeWriter.writeLine(" *     hc_zhang@trip.com");
        codeWriter.writeLine(" *");
        codeWriter.writeLine("------------------------------------------------------------------------------");
        codeWriter.writeLine(" */");

        codeWriter.writeLine();
        codeWriter.writeLine("import org.junit.Test;");

        if (codePackage.getCodeClassList().stream().anyMatch(CodeClass::isSpringComponent)) {
            codeWriter.writeLine("import org.junit.runner.RunWith;");
            codeWriter.writeLine("import org.springframework.beans.factory.annotation.Autowired;");
            codeWriter.writeLine("import org.springframework.test.context.ContextConfiguration;");
            codeWriter.writeLine("import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;");
        }

        if (codePackage.getImports() != null && !codePackage.getImports().isEmpty()) {
            codeWriter.writeLine();
            for (String anImport : codePackage.getImports()) {
                codeWriter.write(Keywords.IMPORT).write(Marks.WHITESPACE).write(anImport).writeLine(Marks.SEMICOLON);
            }
        }

        codeWriter.writeLine();
        if (codePackage.getCodeClassList() == null || codePackage.getCodeClassList().isEmpty()) {
            return;
        }

        for (CodeClass codeClass : codePackage.getCodeClassList()) {
            generateClass(codeClass, codeWriter, options);
        }
    }

    public void generateClass(CodeClass codeClass, CodeWriter codeWriter, GenerateOptions options) throws IOException {

        if (codeClass.isSpringComponent()) {
            codeWriter.writeLine("@RunWith(SpringJUnit4ClassRunner.class)");
            codeWriter.writeLine("@ContextConfiguration(locations = {\"classpath:spring.xml\"})");
        }

        codeWriter.write(Keywords.PUBLIC)
                .write(Marks.WHITESPACE).write(Keywords.CLASS)
                .write(Marks.WHITESPACE).write(codeClass.getName() + "SimpleTest")
                .write(Marks.WHITESPACE).writeLine(Marks.LEFT_BRACE);
        options.pushIndent();

        if (!codeClass.isAbstractClass()) {

            codeWriter.writeLine();
            if (codeClass.isSpringComponent()) {
                codeWriter.write(options.getIndentString()).writeLine("@Autowired");
            }
            codeWriter.write(options.getIndentString()).write(Keywords.PRIVATE)
                    .write(Marks.WHITESPACE).write(codeClass.getName())
                    .write(Marks.WHITESPACE).write(StringExtension.ToLowerCamelCase(codeClass.getName()));

            if (!codeClass.isSpringComponent()) {
                codeWriter.write(Marks.WHITESPACE).write(Marks.EQUAL).write(Marks.WHITESPACE)
                        .write(Keywords.NEW).write(Marks.WHITESPACE).write(codeClass.getName())
                        .write(Marks.LEFT_BRACKET).write(Marks.RIGHT_BRACKET);
            }

            codeWriter.writeLine(Marks.SEMICOLON);

            generateMethods(codeClass, codeWriter, options);
        }

        options.popIndent();
        codeWriter.writeLine(Marks.RIGHT_BRACE);
    }

    private void generateMethods(CodeClass codeClass, CodeWriter codeWriter, GenerateOptions options) throws IOException {

        if (codeClass.getConstructorList() != null && !codeClass.getConstructorList().isEmpty()) {
            for (CodeMethod codeMethod : codeClass.getConstructorList()) {
                if (codeMethod.getAccessModifiers() == AccessModifiers.Private) {
                    continue;
                }
                codeWriter.writeLine();
                generateConstrctor(codeClass, codeMethod, codeWriter, options);
                break;
            }
        }

        if (codeClass.getCodeMethodList() != null && !codeClass.getCodeMethodList().isEmpty()) {
            for (CodeMethod codeMethod : codeClass.getCodeMethodList()) {
                if (codeMethod.getAccessModifiers() == AccessModifiers.Private) {
                    continue;
                }
                if ("hashCode".equalsIgnoreCase(codeMethod.getName())) {
                    continue;
                }
                if ("toString".equalsIgnoreCase(codeMethod.getName())) {
                    continue;
                }
                if ("equals".equalsIgnoreCase(codeMethod.getName())) {
                    continue;
                }

                List<CodeMethod> sameNameMerthods = codeClass.getCodeMethodList().stream()
                        .filter(v -> v.getName().equalsIgnoreCase(codeMethod.getName()))
                        .collect(Collectors.toList());
                long overrideCount = sameNameMerthods.size();
                if (overrideCount > 1) {
                    continue;
                }

                codeWriter.writeLine();
                generateMethod(codeClass, codeMethod, codeWriter, options);
            }
        }
    }

    public void generateConstrctor(CodeClass codeClass, CodeMethod codeMethod, CodeWriter codeWriter, GenerateOptions options) throws IOException {
        codeWriter.write(options.getIndentString()).write(Keywords.PUBLIC)
                .write(Marks.WHITESPACE).write(codeClass.getName() + "SimpleTest");
        codeWriter.write(Marks.LEFT_BRACKET).write(Marks.RIGHT_BRACKET);

        if (codeMethod.getThrowList() != null && !codeMethod.getThrowList().isEmpty()) {
            codeWriter.write(Marks.WHITESPACE).write(Keywords.THROWS);
            Iterator<String> iterator = codeMethod.getThrowList().iterator();
            boolean hasNext = iterator.hasNext();
            while (hasNext) {
                codeWriter.write(Marks.WHITESPACE).write(iterator.next());
                hasNext = iterator.hasNext();
                if (hasNext) {
                    codeWriter.write(Marks.COMMA);
                }
            }
        }

        codeWriter.writeLine(Marks.LEFT_BRACE);

        codeWriter.write(options.getIndentString()).writeLine(Marks.RIGHT_BRACE);
    }

    public void generateMethod(CodeClass codeClass, CodeMethod codeMethod, CodeWriter codeWriter, GenerateOptions options) throws IOException {

        codeWriter.write(options.getIndentString()).writeLine("@Test");
        codeWriter.write(options.getIndentString()).write(Keywords.PUBLIC)
                .write(Marks.WHITESPACE).write(Keywords.VOID)
                .write(Marks.WHITESPACE);

        codeWriter.write(codeMethod.getName());

        List<CodeMethod> sameNameMerthods = codeClass.getCodeMethodList().stream()
                .filter(v -> v.getName().equalsIgnoreCase(codeMethod.getName()))
                .collect(Collectors.toList());
        long overrideCount = sameNameMerthods.size();
        if (overrideCount > 1) {
            codeWriter.write(sameNameMerthods.indexOf(codeMethod) + 1);
        }

        codeWriter.write(Marks.LEFT_BRACKET).write(Marks.RIGHT_BRACKET);
        options.pushIndent();

        if (codeMethod.getThrowList() != null && !codeMethod.getThrowList().isEmpty()) {
            codeWriter.write(Marks.WHITESPACE).write(Keywords.THROWS);
            Iterator<String> iterator = codeMethod.getThrowList().iterator();
            boolean hasNext = iterator.hasNext();
            while (hasNext) {
                codeWriter.write(Marks.WHITESPACE).write(iterator.next());
                hasNext = iterator.hasNext();
                if (hasNext) {
                    codeWriter.write(Marks.COMMA);
                }
            }
        }

        codeWriter.write(Marks.WHITESPACE).writeLine(Marks.LEFT_BRACE);

        generateCallMethod(codeClass, codeMethod, codeWriter, options);

        options.popIndent();
        codeWriter.write(options.getIndentString()).writeLine(Marks.RIGHT_BRACE);
    }

    private void generateCallMethod(CodeClass codeClass, CodeMethod codeMethod, CodeWriter codeWriter, GenerateOptions options) throws IOException {

        codeWriter.write(options.getIndentString());
        if (codeMethod.isStaticMethod()) {
            codeWriter.write(codeClass.getName());
        } else {
            codeWriter.write(StringExtension.ToLowerCamelCase(codeClass.getName()));
        }

        codeWriter.write(Marks.DOT).write(codeMethod.getName()).write(Marks.LEFT_BRACKET);
        useParameters(codeMethod, codeWriter, options);
        codeWriter.write(Marks.RIGHT_BRACKET).writeLine(Marks.SEMICOLON);
    }

    private void useParameters(CodeMethod codeMethod, CodeWriter codeWriter, GenerateOptions options) throws IOException {
        if (codeMethod.getParameters() == null || codeMethod.getParameters().isEmpty()) {
            return;
        }

        Iterator<CodeParameter> iterator = codeMethod.getParameters().iterator();
        boolean hasNext = iterator.hasNext();
        while (hasNext) {
            CodeParameter parameter = iterator.next();

            switch (parameter.getType()) {
                case "int":
                case "long":
                    codeWriter.write(0);
                    break;
                case "boolean":
                    codeWriter.write(false);
                    break;
                default:
                    codeWriter.write(Keywords.NULL);
                    break;
            }

            hasNext = iterator.hasNext();
            if (hasNext) {
                codeWriter.write(Marks.COMMA).write(Marks.WHITESPACE);
            }
        }
    }
}
