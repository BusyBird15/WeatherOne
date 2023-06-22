package kawa;

import gnu.kawa.models.Paintable;
import gnu.kawa.models.Viewable;
import gnu.mapping.OutPort;
import gnu.text.Path;
import java.awt.Component;
import javax.swing.text.AttributeSet;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class ReplPaneOutPort extends OutPort {
    ReplDocument document;
    String str;
    AttributeSet style;
    TextPaneWriter tout;

    public ReplPaneOutPort(ReplDocument document2, String path, AttributeSet style2) {
        this(new TextPaneWriter(document2, style2), document2, path, style2);
    }

    ReplPaneOutPort(TextPaneWriter tout2, ReplDocument document2, String path, AttributeSet style2) {
        super(tout2, true, true, Path.valueOf(path));
        this.str = "";
        this.tout = tout2;
        this.document = document2;
        this.style = style2;
    }

    public void write(String str2, MutableAttributeSet style2) {
        flush();
        this.document.write(str2, style2);
        setColumnNumber(1);
    }

    public synchronized void write(Component c) {
        MutableAttributeSet style2 = new SimpleAttributeSet();
        StyleConstants.setComponent(style2, c);
        write(" ", style2);
    }

    public void print(Object v) {
        if (v instanceof Component) {
            write((Component) v);
        } else if (v instanceof Paintable) {
            MutableAttributeSet style2 = new SimpleAttributeSet();
            style2.addAttribute("$ename", ReplPane.PaintableElementName);
            style2.addAttribute(ReplPane.PaintableAttribute, v);
            write(" ", style2);
        } else if (v instanceof Viewable) {
            MutableAttributeSet style3 = new SimpleAttributeSet();
            style3.addAttribute("$ename", ReplPane.ViewableElementName);
            style3.addAttribute(ReplPane.ViewableAttribute, v);
            write(" ", style3);
        } else {
            super.print(v);
        }
    }
}
