package metatagger;

import org.w3c.dom.Element;

import javax.swing.*;
import java.awt.*;

class TaxonComponent extends FormComponent {
    JPanel jPanelIdEntry;
    LocalizeJLabel jLabelId;
    LocalizeJLabel jLabelEntry;
    JTextFieldPopup jTextFieldId;
    JTextFieldPopup jTextFieldEntry;
    JComboBox jComboBoxLang;
    private boolean isFirst;

    public TaxonComponent(LangstringMediator mediator, boolean isFirst) {
        super(mediator);
        this.isFirst = isFirst;

        jPanelIdEntry = new JPanel();
        jPanelIdEntry.setLayout(new BoxLayout(jPanelIdEntry, BoxLayout.X_AXIS));
        jPanelIdEntry.setOpaque(false);
        jLabelId = new LocalizeJLabel("9.2.2.1");
        jLabelId.setFont(new Font("Dialog", Font.PLAIN, 12));
        jLabelEntry = new LocalizeJLabel("9.2.2.2");
        jLabelEntry.setFont(jLabelId.getFont());
        jTextFieldId = new JTextFieldPopup();
        jTextFieldId.setMinimumSize(new Dimension(50, 25));
        jTextFieldId.setPreferredSize(new Dimension(50, 25));
        jTextFieldId.setMaximumSize(new Dimension(50, 25));
        jTextFieldEntry = new JTextFieldPopup();
        jTextFieldEntry.setMinimumSize(new Dimension(50, 25));
        jTextFieldEntry.setPreferredSize(new Dimension(50, 25));
        jTextFieldEntry.setMaximumSize(new Dimension(1000, 25));

        if (this.isFirst) {
            jPanelIdEntry.add(Box.createHorizontalStrut(5));
            jPanelIdEntry.add(jLabelId);
            jPanelIdEntry.add(Box.createHorizontalStrut(5));
            jPanelIdEntry.add(jTextFieldId);
        } else {
            FontMetrics fm = getFontMetrics(jLabelId.getFont());
            jPanelIdEntry.add(Box.createHorizontalStrut(5 + fm.stringWidth(jLabelId.getText()) + 5 + 50));
        }
        jPanelIdEntry.add(Box.createHorizontalStrut(5));
        jPanelIdEntry.add(jLabelEntry);
        jPanelIdEntry.add(Box.createHorizontalStrut(5));
        jPanelIdEntry.add(jTextFieldEntry);

        jPanelGauche.add(jPanelIdEntry);

        jComboBoxLang = new JComboBox();
        jComboBoxLang.setEditable(true);
        jPanelControl.add(Box.createHorizontalStrut(5));
        jPanelControl.add(jComboBoxLang);
        jComboBoxLang.setFont(new Font("Dialog", Font.PLAIN, 10));

        Object[] values = mediator.getValues();
        for (int i = 0; i < values.length; i++)
            jComboBoxLang.addItem(values[i]);

        mediator.computePreferredSize(jComboBoxLang);
        jComboBoxLang.setPreferredSize(mediator.getComboBoxPreferredSize());
    }

    boolean isFilled() {
        return !jTextFieldEntry.getText().equals("");
    }

    public void setEnabled(boolean b) {
        jTextFieldId.setEditable(b);
        jTextFieldId.setBackground(Color.white);
        jTextFieldEntry.setEditable(b);
        jTextFieldEntry.setBackground(Color.white);
        jComboBoxLang.setEnabled(b);
        jComboBoxLang.setEditable(b);
    }


    //XML
    String toXML_Id(String key) {
        String xml = "";
        if (!jTextFieldId.getText().equals(""))
            xml += "<" + Util.getTag(key + ".1") + ">" + jTextFieldId.getText() +
                    "</" + Util.getTag(key + ".1") + ">\n";
        if (xml.equals("")) xml = null;
        return xml;
    }

    String toXML_Entry() {
        String xml = "";

        if (!jTextFieldEntry.getText().equals("")) {
            String lang = "";
            if (jComboBoxLang.getSelectedItem() != null)
                lang = "language=\"" + jComboBoxLang.getSelectedItem() + "\"";
            xml = "<string " + lang + ">" +
                    jTextFieldEntry.getText() + "</string>" + "\n";
        }
        if (xml.equals("")) xml = null;
        return xml;
    }

    void fromXML_Id(Element e) {
        String value = e.getFirstChild().getNodeValue();
        jTextFieldId.setText(value);
        jTextFieldId.setCaretPosition(0);
    }

    void fromXML_Entry(Element e) {
        String value = e.getFirstChild().getNodeValue();
        jTextFieldEntry.setText(value);
        jTextFieldEntry.setCaretPosition(0);
        jComboBoxLang.setSelectedItem(e.getAttribute("language"));
    }
}