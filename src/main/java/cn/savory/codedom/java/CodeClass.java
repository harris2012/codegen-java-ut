package cn.savory.codedom.java;

import com.google.common.collect.Lists;

import java.util.List;

public class CodeClass extends CodeObject {

    private boolean abstractClass;

    private boolean springComponent;

    private List<CodeMethod> constructorList;

    private List<CodeMethod> codeMethodList;

    public boolean isAbstractClass() {
        return abstractClass;
    }

    public void setAbstractClass(boolean abstractClass) {
        this.abstractClass = abstractClass;
    }

    public boolean isSpringComponent() {
        return springComponent;
    }

    public void setSpringComponent(boolean springComponent) {
        this.springComponent = springComponent;
    }

    public List<CodeMethod> getConstructorList() {
        return constructorList;
    }

    public void setConstructorList(List<CodeMethod> constructorList) {
        this.constructorList = constructorList;
    }

    public List<CodeMethod> getCodeMethodList() {
        return codeMethodList;
    }

    public void setCodeMethodList(List<CodeMethod> codeMethodList) {
        this.codeMethodList = codeMethodList;
    }

    // region CodeClass Extensions

    /**
     * 添加一个方法
     */
    public CodeMethod addMethod(String name) {
        if (this.codeMethodList == null) {
            this.codeMethodList = Lists.newArrayList();
        }

        CodeMethod codeMethod = new CodeMethod();
        codeMethod.setName(name);

        this.codeMethodList.add(codeMethod);

        return codeMethod;
    }

    /**
     * 添加一个构造函数
     */
    public CodeMethod addConstructor(String name) {
        if (this.constructorList == null) {
            this.constructorList = Lists.newArrayList();
        }

        CodeMethod codeMethod = new CodeMethod();
        codeMethod.setName(name);

        this.constructorList.add(codeMethod);

        return codeMethod;
    }

    public CodeClass addMethod(CodeMethod codeMethod) {
        if (this.codeMethodList == null) {
            this.codeMethodList = Lists.newArrayList();
        }

        this.codeMethodList.add(codeMethod);

        return this;
    }

    public CodeClass addConstructor(CodeMethod codeMethod) {
        if (this.constructorList == null) {
            this.constructorList = Lists.newArrayList();
        }

        this.constructorList.add(codeMethod);

        return this;
    }

    // endregion
}
