package metatagger;

import javax.swing.*;
import java.awt.*;

class LangstringMediator extends FormMediator {
    Object[] valueArray;
    private Dimension comboBoxPreferredSize = null;

    LangstringMediator(FormContainer form, Object[] valueArray) {
        super(form);
        this.valueArray = valueArray;
    }

    public void computePreferredSize(JComboBox jComboBox) {
        int max = 0;
        FontMetrics fm = form.getFontMetrics(jComboBox.getFont());
        for (int i = 0; i < jComboBox.getItemCount(); i++) {
            Object item = jComboBox.getItemAt(i);
            if (item != null) {
                int width = fm.stringWidth(item.toString());
                if (width > max)
                    max = width;
            }
        }
        comboBoxPreferredSize = new Dimension(max + 48, 20);
    }

    public Dimension getComboBoxPreferredSize() {
        return comboBoxPreferredSize;
    }

    Object[] getValues() {
        return valueArray;
    }
}