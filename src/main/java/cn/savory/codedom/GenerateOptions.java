package cn.savory.codedom;

public class GenerateOptions {

    /**
     * 用于缩进的字符串
     */
    private String tabString = "    ";

    /**
     * 缩进个数
     */
    private int indentSize = 0;

    /**
     * 缩进字符串
     */
    private String indentString = "";

    public String getTabString() {
        return tabString;
    }

    public void setTabString(String tabString) {
        this.tabString = tabString;
    }

    /**
     * 缩进个数
     */
    public int getIndentSize() {
        return this.indentSize;
    }

    /**
     * 缩进个数
     */
    public void setIndentSize(int value) {
        this.indentSize = value;
        buildIndentString();
    }

    /**
     * 缩进字符
     *
     * @return
     */
    public String getIndentString() {
        return indentString;
    }

    /**
     * 增加缩进
     */
    public void pushIndent() {
        this.indentSize = this.indentSize + 1;
        buildIndentString();
    }

    /**
     * 减少缩进
     */
    public void popIndent() {
        if (this.indentSize > 0) {
            this.indentSize = this.indentSize - 1;
        }

        buildIndentString();
    }

    /**
     * 构建缩进
     */
    private void buildIndentString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.indentSize; i++) {
            builder.append(this.tabString);
        }
        this.indentString = builder.toString();
    }
}
