package metatagger;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;
import java.util.Hashtable;

class VocabularyComponent extends FormComponent {
    JComboBox jComboBoxVocabulary;
    OrderedValue previousVocabulary;
    boolean isEditable;
    boolean isLomVocabulary;

    public VocabularyComponent(ControlValueMediator mediator, boolean isLomVocabulary, boolean isEditable) {
        super(mediator);

        this.isLomVocabulary = isLomVocabulary;

        jComboBoxVocabulary = new JComboBox();
        jComboBoxVocabulary.setFont(new Font("Dialog", Font.PLAIN, 11));
        jPanelGauche.add(jComboBoxVocabulary);

        this.isEditable = isEditable;
        jComboBoxVocabulary.setEditable(isEditable);

        if (mediator != null) {
            for (Enumeration e = mediator.getAvailableValues().elements(); e.hasMoreElements();)
                jComboBoxVocabulary.addItem(e.nextElement());
            previousVocabulary = getSelectedValue();
        }

        SymAction lSymAction = new SymAction();
        jComboBoxVocabulary.addActionListener(lSymAction);
    }

    boolean isFilled() {
        return getSelectedValue().value != null && !getSelectedValue().value.equals("");
    }

    public void setEnabled(boolean b) {
        jComboBoxVocabulary.setEnabled(b);
        jComboBoxVocabulary.setEditable(b && isEditable);
    }

    class SymAction implements java.awt.event.ActionListener {
        public void actionPerformed(java.awt.event.ActionEvent event) {
            Object object = event.getSource();
            if (object == jComboBoxVocabulary)
                jComboBoxVocabulary_actionPerformed();
        }
    }

    void jComboBoxVocabulary_actionPerformed() {
        if (mediator != null) {
            if (!getSelectedValue().equals(previousVocabulary)) {
                ((ControlValueMediator) mediator).comboBoxValuePerformed(this);
                previousVocabulary = getSelectedValue();
            }
        }
    }

    public JComboBox getJComboBox() {
        return jComboBoxVocabulary;
    }

    public OrderedValue getSelectedValue() {
        Object obj = jComboBoxVocabulary.getSelectedItem();
        OrderedValue v = null;
        if (obj instanceof OrderedValue)
            v = (OrderedValue) obj;
        else
            v = new OrderedValue((String) obj, -1, false);

        return v;
    }

    public Object getMatchedValue(String s) {
        for (int i = 0; i < jComboBoxVocabulary.getItemCount(); i++) {
            Object o = jComboBoxVocabulary.getItemAt(i);
            if (((OrderedValue) o).toString().equals(s))
                return o;
        }
        return s;
    }

    public OrderedValue getPreviousSelectedValue() {
        return previousVocabulary;
    }

    //XML
    String toXML(String key) {
        String xml = null;
        if (isFilled()) {
            if (isLomVocabulary)
                xml = "<source>LOMv1.0</source>\n" +
                        "<value>" + Util.getXMLVocabulary(getSelectedValue().value.toString()) + "</value>\n";
            else
                xml = getSelectedValue() + "\n";
        }
        return xml;
    }

    void fromXML(String path, Element e, Hashtable tableImportXML, boolean firstField) {
        if (isLomVocabulary) {
            NodeList list = e.getElementsByTagName("value");
            Element child = (Element) list.item(0);
            try {
                if (child.getFirstChild() != null) {
                    String key = child.getFirstChild().getNodeValue().trim().replace(' ', '_');
                    int pos = Util.getPosVocabulary(key);
                    Integer index = (Integer) tableImportXML.remove(new Integer(pos));
                    if (index == null) return;
                    jComboBoxVocabulary.setSelectedIndex(index.intValue());
                    for (Enumeration en = tableImportXML.keys(); en.hasMoreElements();) {
                        Integer i = (Integer) en.nextElement();
                        int val = ((Integer) tableImportXML.get(i)).intValue();
                        if (firstField) val--;
                        if (i.intValue() > pos) val--;
                        tableImportXML.put(i, new Integer(val));
                    }
                }
            } catch (IllegalTagException ite) {
            }
        } else if (e.getFirstChild() != null)
            jComboBoxVocabulary.setSelectedItem(getMatchedValue(e.getFirstChild().getNodeValue().trim()));
    }
}