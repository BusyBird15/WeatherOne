package gnu.kawa.swingviews;

import com.google.appinventor.components.common.PropertyTypeConstants;
import gnu.kawa.models.Button;
import gnu.kawa.models.Model;
import gnu.kawa.models.ModelListener;
import java.awt.Color;
import javax.swing.JButton;

public class SwingButton extends JButton implements ModelListener {
    Button model;

    public SwingButton(Button model2) {
        super(model2.getText());
        setModel(new SwModel(model2));
        this.model = model2;
        Object action = model2.getAction();
        if (action != null) {
            addActionListener(SwingDisplay.makeActionListener(action));
        }
        model2.addListener((ModelListener) this);
        Color fg = model2.getForeground();
        if (fg != null) {
            SwingButton.super.setBackground(fg);
        }
        Color bg = model2.getBackground();
        if (bg != null) {
            SwingButton.super.setBackground(bg);
        }
    }

    public void setText(String text) {
        if (this.model == null) {
            SwingButton.super.setText(text);
        } else {
            this.model.setText(text);
        }
    }

    public void setForeground(Color fg) {
        if (this.model == null) {
            SwingButton.super.setForeground(fg);
        } else {
            this.model.setForeground(fg);
        }
    }

    public void setBackground(Color bg) {
        if (this.model == null) {
            SwingButton.super.setBackground(bg);
        } else {
            this.model.setBackground(bg);
        }
    }

    public void modelUpdated(Model model2, Object key) {
        if (key == PropertyTypeConstants.PROPERTY_TYPE_TEXT && model2 == this.model) {
            SwingButton.super.setText(this.model.getText());
        } else if (key == "foreground" && model2 == this.model) {
            SwingButton.super.setForeground(this.model.getForeground());
        } else if (key == "background" && model2 == this.model) {
            SwingButton.super.setBackground(this.model.getBackground());
        }
    }
}
