package kawa;

import gnu.lists.CharBuffer;
import gnu.mapping.OutPort;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.EditorKit;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;

public class ReplPane extends JTextPane implements KeyListener {
    public static final Object PaintableAttribute = new String(PaintableElementName);
    public static final String PaintableElementName = "Paintable";
    public static final Object ViewableAttribute = new String(ViewableElementName);
    public static final String ViewableElementName = "Viewable";
    ReplDocument document;

    public ReplPane(ReplDocument document2) {
        super(document2);
        this.document = document2;
        document2.pane = this;
        document2.paneCount++;
        addKeyListener(this);
        addFocusListener(document2);
        EditorKit editorKit = getEditorKit();
        setCaretPosition(document2.outputMark);
    }

    /* access modifiers changed from: protected */
    public EditorKit createDefaultEditorKit() {
        return new ReplEditorKit(this);
    }

    public void removeNotify() {
        ReplPane.super.removeNotify();
        ReplDocument replDocument = this.document;
        int i = replDocument.paneCount - 1;
        replDocument.paneCount = i;
        if (i == 0) {
            this.document.close();
        }
    }

    /* access modifiers changed from: package-private */
    public void enter() {
        String str;
        int pos = getCaretPosition();
        CharBuffer b = this.document.content.buffer;
        int len = b.length() - 1;
        this.document.endMark = -1;
        if (pos >= this.document.outputMark) {
            int lineAfterCaret = b.indexOf(10, pos);
            if (lineAfterCaret == len) {
                if (len <= this.document.outputMark || b.charAt(len - 1) != 10) {
                    this.document.insertString(len, "\n", (AttributeSet) null);
                } else {
                    lineAfterCaret--;
                }
            }
            this.document.endMark = lineAfterCaret;
            synchronized (this.document.in_r) {
                this.document.in_r.notifyAll();
            }
            if (pos <= lineAfterCaret) {
                setCaretPosition(lineAfterCaret + 1);
                return;
            }
            return;
        }
        int lineBefore = pos == 0 ? 0 : b.lastIndexOf(10, pos - 1) + 1;
        Element el = this.document.getCharacterElement(lineBefore);
        int lineAfter = b.indexOf(10, pos);
        if (el.getAttributes().isEqual(ReplDocument.promptStyle)) {
            lineBefore = el.getEndOffset();
        }
        if (lineAfter < 0) {
            str = b.substring(lineBefore, len) + 10;
        } else {
            str = b.substring(lineBefore, lineAfter + 1);
        }
        setCaretPosition(this.document.outputMark);
        this.document.write(str, ReplDocument.inputStyle);
        if (this.document.in_r != null) {
            this.document.in_r.append((CharSequence) str, 0, str.length());
        }
    }

    public MutableAttributeSet getInputAttributes() {
        return ReplDocument.inputStyle;
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 10) {
            enter();
            e.consume();
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public OutPort getStdout() {
        return this.document.out_stream;
    }

    public OutPort getStderr() {
        return this.document.err_stream;
    }
}
