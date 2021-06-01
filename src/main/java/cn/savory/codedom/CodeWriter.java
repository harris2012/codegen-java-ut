package cn.savory.codedom;

import java.io.IOException;
import java.io.Writer;

public class CodeWriter {

    private final Writer textWriter;

    public CodeWriter(Writer textWriter) {
        this.textWriter = textWriter;
    }

    public CodeWriter write(int value) throws IOException {
        textWriter.write(String.valueOf(value));
        return this;
    }

    public CodeWriter write(long value) throws IOException {
        textWriter.write(String.valueOf(value) + "L");
        return this;
    }

    public CodeWriter write(boolean value) throws IOException {
        textWriter.write(String.valueOf(value));
        return this;
    }

    public CodeWriter write(String value) throws IOException {
        textWriter.write(value);
        return this;
    }

    public CodeWriter writeLine(String value) throws IOException {
        textWriter.write(value);
        textWriter.write("\r\n");
        return this;
    }

    public CodeWriter writeLine() throws IOException {
        textWriter.write("\r\n");
        return this;
    }
}
