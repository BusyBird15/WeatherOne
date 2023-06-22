package gnu.kawa.swingviews;

import gnu.mapping.Procedure;
import gnu.mapping.WrappedException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* compiled from: SwingDisplay */
class ProcActionListener implements ActionListener {
    Procedure proc;

    public ProcActionListener(Procedure proc2) {
        this.proc = proc2;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            this.proc.apply1(e);
        } catch (Throwable ex) {
            throw new WrappedException(ex);
        }
    }
}
