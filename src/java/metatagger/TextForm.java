package metatagger;

import org.w3c.dom.Element;

import java.awt.*;
import java.util.Enumeration;

class TextForm extends FormContainer {
    boolean isOneLine;
    Object[] numericParam;

    public TextForm(String title, boolean isLine, boolean isMultipleContainer, boolean isOneLine) {
        super(title, isLine, isMultipleContainer);
        this.isOneLine = isOneLine;
        mediator = new FormMediator(this);
        offset = 2;
    }

    void addFormContent() {
        TextComponent c = new TextComponent(mediator, isOneLine);
        if (numericParam != null)
            c.setNumericTextField(numericParam);
        int height = isOneLine ? 25 : 60;
        c.setMinimumSize(new Dimension(10, height));
        c.setPreferredSize(new Dimension(10, height));
        c.setMaximumSize(new Dimension(2000, height));
        addComponent(c);

        mediator.buttonAddComponentPerformed(c);
    }

    void setNumericTextField(int length, String minMask, String maxMask) {
        numericParam = new Object[]{new Integer(length), minMask, maxMask};
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

    String defaultToXML(String key) {
        return super.toXML(key);
    }

    void fromXML(String path, Element e) {
        TextComponent c = null;
        if (isFilled()) {
            if (!isMultipleContainer) return; //pas d'import multiple possible
            addFormContent();
            c = (TextComponent) vComponents.lastElement();
        } else
            c = (TextComponent) vComponents.firstElement();

        c.fromXML(path, e);
    }
}