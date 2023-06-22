package gnu.kawa.swingviews;

import gnu.kawa.models.Button;
import javax.swing.DefaultButtonModel;

/* compiled from: SwingButton */
class SwModel extends DefaultButtonModel {
    Button model;

    public SwModel(Button model2) {
        this.model = model2;
        setActionCommand(model2.getText());
    }
}
