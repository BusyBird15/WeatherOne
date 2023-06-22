package kawa.lang;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class SyntaxRule extends SyntaxTemplate implements Externalizable {
    SyntaxPattern pattern;

    public SyntaxRule() {
    }

    public SyntaxRule(SyntaxPattern pattern2, String pattern_nesting, String template_program, Object[] literal_values, int max_nesting) {
        super(pattern_nesting, template_program, literal_values, max_nesting);
        this.pattern = pattern2;
    }

    public SyntaxRule(SyntaxPattern pattern2, Object template, SyntaxForm template_syntax, Translator tr) {
        super(template, template_syntax, tr);
        this.pattern = pattern2;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(this.pattern);
        super.writeExternal(out);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.pattern = (SyntaxPattern) in.readObject();
        super.readExternal(in);
    }
}
