package metatagger;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.awt.*;

class LangstringForm extends TextForm {
    Object[] values = new Object[]{null, "fr", "en", "es"};

    public LangstringForm(String title, boolean isLine, boolean isMultipleContainer, boolean isOneLine) {
        super(title, isLine, isMultipleContainer, isOneLine);
        mediator = new LangstringMediator(this, values);
    }

    void addFormContent() {
        LangstringComponent c = new LangstringComponent((LangstringMediator) mediator, isOneLine);
        int height = isOneLine ? 25 : 60;
        c.setMinimumSize(new Dimension(10, height));
        c.setPreferredSize(new Dimension(10, height));
        c.setMaximumSize(new Dimension(2000, height));
        addComponent(c);

        mediator.buttonAddComponentPerformed(c);
    }

    //XML
    String toXML(String key) {
        return defaultToXML(key);
    }

    void fromXML(String path, Element e) {
        NodeList list = e.getElementsByTagName("string");
        for (int i = 0; i < list.getLength(); i++) {
            Element child = (Element) list.item(i);
            LangstringComponent c = null;
            if (isFilled()) {
                if (!isMultipleContainer) return; //pas d'import multiple possible
                addFormContent();
                c = (LangstringComponent) vComponents.lastElement();
            } else
                c = (LangstringComponent) vComponents.firstElement();

            c.fromXML(path, child);
        }
    }
}