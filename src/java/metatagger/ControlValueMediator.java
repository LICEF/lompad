package metatagger;

import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;
import java.util.Vector;

class ControlValueMediator extends FormMediator {
    Object[] valueArray;
    Vector vExclusiveValues;
    private Vector vAvailableValues;
    private Dimension comboBoxPreferredSize = null;

    ControlValueMediator(VocabularyForm form, Object[] valueArray, Object[] exclusiveValues) {
        super(form);
        this.valueArray = valueArray;
        vAvailableValues = new Vector();
        if (exclusiveValues != null)
            vExclusiveValues = new Vector();

        if (valueArray != null) {
            for (int i = 0; i < valueArray.length; i++) {
                OrderedValue v = new OrderedValue(valueArray[i], i, form.isLomVocabulary);
                if (exclusiveValues != null) {
                    for (int j = 0; j < exclusiveValues.length; j++) {
                        if (exclusiveValues[j] == null) {
                            if (valueArray[i] == null)
                                vExclusiveValues.addElement(v);
                        } else if (exclusiveValues[j].equals(valueArray[i]))
                            vExclusiveValues.addElement(v);
                    }
                }
                vAvailableValues.addElement(v);
            }
        }
    }

    void buttonAddComponentPerformed(FormComponent c) {
        super.buttonAddComponentPerformed(c);
        setNotAvailableValue((VocabularyComponent) c, ((VocabularyComponent) c).getSelectedValue(), false);
        if (vExclusiveValues != null && form.vComponents.size() == 2) {
            for (Enumeration e = form.vComponents.elements(); e.hasMoreElements();) {
                VocabularyComponent comp = (VocabularyComponent) e.nextElement();
                for (Enumeration e2 = vExclusiveValues.elements(); e2.hasMoreElements();)
                    setNotAvailableValue(comp, (OrderedValue) e2.nextElement(), true);
            }
        }
        boolean exclValSelected = (vExclusiveValues != null && vExclusiveValues.contains(((VocabularyComponent) c).getSelectedValue()));
        updateAddComponentButton(exclValSelected);
    }

    void buttonRemoveComponentPerformed(FormComponent c) {
        super.buttonRemoveComponentPerformed(c);
        setAvailableValue((VocabularyComponent) c, ((VocabularyComponent) c).getSelectedValue());
        if (vExclusiveValues != null && form.vComponents.size() == 1) {
            for (Enumeration e = vExclusiveValues.elements(); e.hasMoreElements();)
                setAvailableValue((VocabularyComponent) c, (OrderedValue) e.nextElement());
        }
        updateAddComponentButton(false);
    }

    void comboBoxValuePerformed(VocabularyComponent c) {
        setAvailableValue(c, c.getPreviousSelectedValue());
        setNotAvailableValue(c, c.getSelectedValue(), false);
        boolean exclValSelected = (vExclusiveValues != null && vExclusiveValues.contains(c.getSelectedValue()));
        updateAddComponentButton(exclValSelected);
    }

    void updateAddComponentButton(boolean exclValSelected) {
        if (exclValSelected)
            form.setEnabledButtonAdd(false);
        else
            form.setEnabledButtonAdd(((VocabularyForm) form).isEditable || vAvailableValues.size() > 0);
    }

    public void setComboBoxPreferredSize(Dimension d) {
        if (comboBoxPreferredSize == null)
            comboBoxPreferredSize = d;
    }

    public Dimension getComboBoxPreferredSize() {
        return comboBoxPreferredSize;
    }

    Vector getAvailableValues() {
        return vAvailableValues;
    }

    /**
     * Cette fonction fait la gestion des valeurs disponibles.
     * Elle retire la valeur utilisée par le nouveau composant
     * pour les prochaines entrées. Elle notifie aussi les composants
     * existant de le retirer de leur liste.
     *
     * @param c     le composant venant d'etre créé
     * @param value la valeur a retirer
     */
    void setNotAvailableValue(VocabularyComponent c, OrderedValue value, boolean forceSelf) {
        if (value.pos == -1) return;
        vAvailableValues.removeElement(value);
        for (Enumeration e = form.vComponents.elements(); e.hasMoreElements();) {
            VocabularyComponent comp = (VocabularyComponent) e.nextElement();
            if (!comp.equals(c) || forceSelf) {
                JComboBox comboBox = comp.getJComboBox();
                comboBox.removeItem(value);
            }
        }
    }

    /**
     * Cette fonction fait la gestion des valeurs disponibles.
     * Elle réintroduit la valeur libérée par le composant
     * pour les prochaines entrées. Elle notifie aussi les composants
     * existant de la rajouter a leur liste.
     *
     * @param c     le composant venant d'etre supprimé, ou dont la comboBox vient d'etre modifiée
     * @param value la valeur a rajouter
     */
    void setAvailableValue(VocabularyComponent c, OrderedValue value) {
        if (value.pos == -1) return;
        int pos = 0;
        for (Enumeration e = vAvailableValues.elements(); e.hasMoreElements(); pos++) {
            OrderedValue l = (OrderedValue) e.nextElement();
            if (value.pos < l.pos) break;
        }
        vAvailableValues.insertElementAt(value, pos);

        for (Enumeration e = form.vComponents.elements(); e.hasMoreElements();) {
            VocabularyComponent comp = (VocabularyComponent) e.nextElement();
            if (!comp.equals(c)) {
                JComboBox comboBox = comp.getJComboBox();
                pos = 0;
                for (; pos < comboBox.getItemCount(); pos++) {
                    OrderedValue l = (OrderedValue) comboBox.getItemAt(pos);
                    if (value.pos < l.pos) break;
                }
                comboBox.insertItemAt(value, pos);
            }
        }
    }
}