package metatagger;

import org.w3c.dom.Element;

import java.awt.*;
import java.util.Enumeration;

class FormatForm extends FormContainer {

    public FormatForm(String title, boolean isLine, boolean isMultipleContainer) {
        super(title, isLine, isMultipleContainer);
        mediator = new FormMediator(this);
        offset = 2;
    }

    void addFormContent() {
        FormatComponent c = new FormatComponent(mediator);
        c.setMinimumSize(new Dimension(10, 25));
        c.setPreferredSize(new Dimension(10, 25));
        c.setMaximumSize(new Dimension(2000, 25));
        addComponent(c);

        mediator.buttonAddComponentPerformed(c);
    }

    //XML
    String toXML(String key) {
        String xml = "";

        for (Enumeration e = vComponents.elements(); e.hasMoreElements();) {
            FormComponent c = (FormComponent) e.nextElement();
            String res = c.toXML(key);
            if (res != null)
                xml += "<" + Util.getTag(key) + ">\n" + res + "</" + Util.getTag(key) + ">\n";
        }

        if (xml.equals(""))
            xml = null;

        return xml;
    }

    void fromXML(String path, Element e) {
        FormatComponent c = null;
        if (isFilled()) {
            if (!isMultipleContainer) return; //pas d'import multiple possible
            addFormContent();
            c = (FormatComponent) vComponents.lastElement();
        } else
            c = (FormatComponent) vComponents.firstElement();

        c.fromXML(path, e);
    }
}