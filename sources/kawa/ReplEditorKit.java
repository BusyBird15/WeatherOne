package kawa;

import gnu.kawa.models.Paintable;
import gnu.kawa.models.Viewable;
import gnu.kawa.swingviews.SwingDisplay;
import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.text.AttributeSet;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

/* compiled from: ReplPane */
class ReplEditorKit extends StyledEditorKit {
    ViewFactory factory;
    final ReplPane pane;
    ViewFactory styledFactory = ReplEditorKit.super.getViewFactory();

    public ReplEditorKit(final ReplPane pane2) {
        this.pane = pane2;
        this.factory = new ViewFactory() {
            public View create(Element elem) {
                String kind = elem.getName();
                if (kind == ReplPane.ViewableElementName) {
                    return new ComponentView(elem) {
                        /* access modifiers changed from: protected */
                        public Component createComponent() {
                            AttributeSet attr = getElement().getAttributes();
                            Component panel = new JPanel();
                            ((Viewable) attr.getAttribute(ReplPane.ViewableAttribute)).makeView(SwingDisplay.getInstance(), panel);
                            if (panel.getComponentCount() == 1) {
                                Component comp = panel.getComponent(0);
                                panel.removeAll();
                                return comp;
                            }
                            panel.setBackground(pane2.getBackground());
                            return panel;
                        }
                    };
                }
                if (kind == ReplPane.PaintableElementName) {
                    return new PaintableView(elem, (Paintable) elem.getAttributes().getAttribute(ReplPane.PaintableAttribute));
                }
                return ReplEditorKit.this.styledFactory.create(elem);
            }
        };
    }

    public ViewFactory getViewFactory() {
        return this.factory;
    }
}
