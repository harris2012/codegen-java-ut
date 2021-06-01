package cn.savory.codedom.java;

import com.google.common.collect.Lists;

import java.util.List;

public class CodeFile {

    private List<CodePackage> packages;

    public List<CodePackage> getPackages() {
        return packages;
    }

    public void setPackages(List<CodePackage> packages) {
        this.packages = packages;
    }

    // region CodeFile Extensions

    public CodePackage addPackage() {
        return addPackage(null);
    }

    public CodePackage addPackage(String name) {
        if (packages == null) {
            packages = Lists.newArrayList();
        }

        CodePackage codePackage = new CodePackage();
        codePackage.setName(name);

        packages.add(codePackage);

        return codePackage;
    }

    // endregion
}
