package cn.savory.codedom.java;

import com.google.common.collect.Lists;

import java.util.List;

public class CodePackage {

    private String name;

    private List<String> imports;

    private List<CodeClass> codeClassList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public List<CodeClass> getCodeClassList() {
        return codeClassList;
    }

    public void setCodeClassList(List<CodeClass> codeClassList) {
        this.codeClassList = codeClassList;
    }

    // region CodePackage Extensions

    public CodePackage addImport(String importn) {
        if (this.imports == null) {
            this.imports = Lists.newArrayList();
        }

        this.imports.add(importn);

        return this;
    }

    public CodeClass addClass() {
        return addClass(null);
    }

    public CodeClass addClass(String name) {
        if (codeClassList == null) {
            codeClassList = Lists.newArrayList();
        }

        CodeClass codeClass = new CodeClass();
        codeClass.setName(name);

        codeClassList.add(codeClass);

        return codeClass;
    }

    // endregion
}
