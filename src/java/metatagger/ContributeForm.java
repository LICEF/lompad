package metatagger;

import org.w3c.dom.Element;

import javax.swing.*;
import java.util.Enumeration;

class ContributeForm extends FormContainer {
    String rootNumber;
    Object[] roleValues;
    ImageIcon icon;

    public ContributeForm(String title, boolean isLine, ImageIcon icon, boolean isMultiple, String rootNumber, Object[] roleValues) {
        super(title, isLine, isMultiple);
        this.rootNumber = rootNumber;
        this.roleValues = roleValues;
        this.icon = icon;
    }

    void addFormContent() {
        FormWrapper wrapper = new FormWrapper("", true);
        wrapper.addToggle();

        VocabularyForm role = new VocabularyForm(rootNumber + ".1", true, false, roleValues, null, true);
        role.setIcon(icon);
        role.setAlignRight();
        role.addFormContent();
        wrapper.addComponent(role);

        EntityForm entity = new EntityForm(rootNumber + ".2", true, true);
        entity.setIcon(icon);
        entity.addFormContent();
        wrapper.addComponent(entity);

        DatetimeForm date = new DatetimeForm(rootNumber + ".3", true, false);
        date.setIcon(icon);
        date.addFormContent();
        wrapper.addComponent(date);

        addComponent(wrapper);
    }

    //XML
    String toXML(String key) {
        String xml = "";

        for (Enumeration e = vComponents.elements(); e.hasMoreElements();) {
            FormComponent c = (FormComponent) e.nextElement();
            String res = ((FormWrapper) c).toXML(key, true);
            if (res != null)
                xml += "<" + Util.getTag(key) + ">\n" + res + "</" + Util.getTag(key) + ">\n";
        }

        if (xml.equals(""))
            xml = null;

        return xml;
    }

    void fromXML(String path, Element e) {
        FormWrapper c = null;
        if (isFilled()) {
            if (!isMultipleContainer) return; //pas d'import multiple possible
            addFormContent();
            c = (FormWrapper) vComponents.lastElement();
        } else
            c = (FormWrapper) vComponents.firstElement();

        c.fromXML(path, e, true);
    }
}