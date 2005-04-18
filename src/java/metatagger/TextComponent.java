package metatagger;

import org.w3c.dom.Element;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

class TextComponent extends FormComponent {
    JScrollPane jScrollPaneTextArea;
    JTextComponent currentJTextComponent;

    public TextComponent(FormMediator mediator, boolean isOneLine) {
        super(mediator);

        if (isOneLine) {
            currentJTextComponent = new JTextFieldPopup();
            jPanelGauche.add(currentJTextComponent);
        } else {
            jScrollPaneTextArea = new JScrollPane();
            jPanelGauche.add(BorderLayout.CENTER, jScrollPaneTextArea);
            currentJTextComponent = new JTextAreaPopup();
            ((JTextAreaPopup) currentJTextComponent).setLineWrap(true);
            jScrollPaneTextArea.getViewport().add(currentJTextComponent);
        }
    }

    boolean isFilled() {
        return !currentJTextComponent.getText().equals("");
    }

    public void setEnabled(boolean b) {
        currentJTextComponent.setEditable(b);
        currentJTextComponent.setBackground(Color.white);
    }

    void updateAfterAdded() {
        currentJTextComponent.requestFocus();
    }

    void setNumericTextField(Object[] numericParam) {
        jPanelGauche.removeAll();
        currentJTextComponent =
                new NumericTextField(((Integer) numericParam[0]).intValue(),
                        (String) numericParam[1], (String) numericParam[2]);
        jPanelGauche.add(currentJTextComponent);
    }

    //XML
    String toXML(String key) {
        String xml = null;
        if (!currentJTextComponent.getText().equals(""))
            xml = Util.convertSpecialCharactersForXML(currentJTextComponent.getText()) + "\n";
        return xml;
    }

    void fromXML(String path, Element e) {
        if (e.getFirstChild() != null) {
            currentJTextComponent.setText(e.getFirstChild().getNodeValue().trim());
            currentJTextComponent.setCaretPosition(0);
        }
    }
}