package metatagger;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import java.awt.*;

class IdentifierComponent extends FormComponent {
    JPanel jPanelCatEntr;
    JPanel jPanelCatalog;
    JPanel jPanelEntry;
    LocalizeJLabel jLabelCatalog;
    LocalizeJLabel jLabelEntry;
    JTextField jTextFieldCatalog;
    JTextField jTextFieldEntry;

    public IdentifierComponent() {
        super(null);

        jPanelCatEntr = new JPanel();
        jPanelCatEntr.setOpaque(false);
        jPanelCatalog = new JPanel();
        jPanelCatalog.setOpaque(false);
        jPanelEntry = new JPanel();
        jPanelEntry.setOpaque(false);
        jLabelCatalog = new LocalizeJLabel("1.1.1");
        jLabelCatalog.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
        jLabelEntry = new LocalizeJLabel("1.1.2");
        jLabelEntry.setFont(jLabelCatalog.getFont());
        jTextFieldCatalog = new JTextField();
        jTextFieldEntry = new JTextField();

        jPanelCatEntr.setLayout(new GridLayout(1, 2, 5, 0));

        jPanelCatalog.setLayout(new BoxLayout(jPanelCatalog, BoxLayout.X_AXIS));
        jPanelCatalog.add(Box.createHorizontalStrut(5));
        jPanelCatalog.add(jLabelCatalog);
        jPanelCatalog.add(Box.createHorizontalStrut(5));
        jPanelCatalog.add(jTextFieldCatalog);
        jPanelCatEntr.add(jPanelCatalog);

        jPanelEntry.setLayout(new BoxLayout(jPanelEntry, BoxLayout.X_AXIS));
        jPanelCatalog.add(Box.createHorizontalStrut(5));
        jPanelEntry.add(jLabelEntry);
        jPanelEntry.add(Box.createHorizontalStrut(5));
        jPanelEntry.add(jTextFieldEntry);
        jPanelCatEntr.add(jPanelEntry);

        jPanelGauche.add(jPanelCatEntr);
    }

    boolean isFilled() {
        return !(jTextFieldCatalog.getText().equals("") &&
                jTextFieldEntry.getText().equals(""));
    }

    public void setEnabled(boolean b) {
        jTextFieldCatalog.setEditable(b);
        jTextFieldCatalog.setBackground(Color.white);
        jTextFieldEntry.setEditable(b);
        jTextFieldEntry.setBackground(Color.white);
    }


    //XML
    String toXML(String key) {
        String xml = "";
        if (!jTextFieldCatalog.getText().equals(""))
            xml += "<" + Util.getTag(key + ".1") + ">" + Util.convertSpecialCharactersForXML(jTextFieldCatalog.getText()) +
                    "</" + Util.getTag(key + ".1") + ">\n";
        if (!jTextFieldEntry.getText().equals(""))
            xml += "<" + Util.getTag(key + ".2") + ">" + Util.convertSpecialCharactersForXML(jTextFieldEntry.getText()) +
                    "</" + Util.getTag(key + ".2") + ">\n";

        if (xml.equals("")) xml = null;
        return xml;
    }

    void fromXML(String path, Element e) {
        NodeList list = e.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element child = (Element) node;
                if (child.getFirstChild() != null) {
                    String value = child.getFirstChild().getNodeValue();
                    try {
                        int pos = Util.getPosTag(path + "/" + child.getTagName().toLowerCase());
                        if (pos == 1) {
                            jTextFieldCatalog.setText(value);
                            jTextFieldCatalog.setCaretPosition(0);
                        }
                        if (pos == 2) {
                            jTextFieldEntry.setText(value);
                            jTextFieldEntry.setCaretPosition(0);
                        }
                    } catch (IllegalTagException ite) {
                    }
                }
            }
        }
    }
}