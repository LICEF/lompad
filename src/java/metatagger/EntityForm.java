package metatagger;

import org.w3c.dom.Element;

import java.awt.*;
import java.util.Enumeration;

class EntityForm extends FormContainer {
    public EntityForm(String title, boolean isLine, boolean isMultipleContainer) {
        super(title, isLine, isMultipleContainer);
        offset = 2;
    }

    void addFormContent() {
        boolean isFirst = vComponents.size() == 0;
        EntityComponent c = new EntityComponent(isFirst);
        int height = isFirst ? 50 : 25;
        c.setMinimumSize(new Dimension(10, height));
        c.setPreferredSize(new Dimension(10, height));
        c.setMaximumSize(new Dimension(2000, height));
        addComponent(c);
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
        EntityComponent c = null;
        if (isFilled()) {
            if (!isMultipleContainer) return; //pas d'import multiple possible
            addFormContent();
            c = (EntityComponent) vComponents.lastElement();
        } else
            c = (EntityComponent) vComponents.firstElement();

        c.fromXML(path, e);
    }
}