package metatagger;

import javax.swing.*;

class LocalizeJLabel extends JLabel {
    public LocalizeJLabel(String text) {
        super(text);
    }

    public String getText() {
        return Util.getLabel(super.getText());
    }
}