package metatagger;

import org.w3c.dom.Element;

import java.util.Enumeration;

class FormWrapper extends FormContainer {
    public FormWrapper() {
        super();
    }

    public FormWrapper(String title, boolean isLine) {
        super(title, isLine);
    }

    void addFormContent() {
    }

    //XML
    String toXML(String key, boolean incr) {
        String xml = "";

        int i = 1;
        for (Enumeration e = vComponents.elements(); e.hasMoreElements(); i++) {
            FormComponent c = (FormComponent) e.nextElement();
            String res = c.toXML(key + (incr ? "." + i : ""));
            if (res != null)
                xml += res;
        }

        if (xml.equals(""))
            xml = null;

        return xml;
    }

    void fromXML(String path, Element e, boolean loop) {
        if (loop)
            fromXML(path, e);
        else {
            FormComponent c = (FormComponent) vComponents.firstElement();
            c.fromXML(path, e);
        }
    }
}