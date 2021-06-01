package cn.savory.codedom.java;

import com.google.common.collect.Lists;

import java.util.List;

public class CodeMethod extends CodeObject {

    private AccessModifiers accessModifiers;

    private boolean staticMethod;

    private List<String> throwList;

    private List<CodeParameter> parameters;

    public AccessModifiers getAccessModifiers() {
        return accessModifiers;
    }

    public void setAccessModifiers(AccessModifiers accessModifiers) {
        this.accessModifiers = accessModifiers;
    }

    public boolean isStaticMethod() {
        return staticMethod;
    }

    public void setStaticMethod(boolean staticMethod) {
        this.staticMethod = staticMethod;
    }

    public List<String> getThrowList() {
        return throwList;
    }

    public void setThrowList(List<String> throwList) {
        this.throwList = throwList;
    }

    public List<CodeParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<CodeParameter> parameters) {
        this.parameters = parameters;
    }

    // region CodeMethod Extensions

    public CodeMethod addThrow(String thrown) {
        if (this.throwList == null) {
            this.throwList = Lists.newArrayList();
        }
        this.throwList.add(thrown);
        return this;
    }

    public CodeMethod addParameter(String type, String name) {
        CodeParameter parameter = new CodeParameter();
        parameter.setType(type);
        parameter.setName(name);

        return addParameter(parameter);
    }

    public CodeMethod addParameter(CodeParameter parameter) {
        if (this.parameters == null) {
            this.parameters = Lists.newArrayList();
        }

        this.parameters.add(parameter);
        return this;
    }

    public CodeParameter addParameter() {
        if (this.parameters == null) {
            this.parameters = Lists.newArrayList();
        }

        CodeParameter parameter = new CodeParameter();

        this.parameters.add(parameter);

        return parameter;
    }

    // endregion
}
