package metatagger;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;
import java.util.Enumeration;

class OrCompositeForm extends FormContainer {
    public OrCompositeForm(boolean isMultiple) {
        super(isMultiple);
    }

    void addFormContent() {
        FormWrapper wrapper = new FormWrapper("4.4.1", true);
        wrapper.setIcon(Util.greenIcon);
        wrapper.addToggle();

        OrCompositeComponent c = new OrCompositeComponent();
        c.setMinimumSize(new Dimension(10, 100));
        c.setPreferredSize(new Dimension(10, 100));
        c.setMaximumSize(new Dimension(2000, 100));
        wrapper.addComponent(c);

        addComponent(wrapper);
    }

    //XML
    String toXML(String key) {
        String xml = "";

        for (Enumeration e = vComponents.elements(); e.hasMoreElements();) {
            FormComponent c = (FormComponent) e.nextElement();
            String res = ((FormWrapper) c).toXML(key, false);
            if (res != null)
                xml += "<" + Util.getTag(key) + ">\n" + res + "</" + Util.getTag(key) + ">\n";
        }

        if (xml.equals(""))
            xml = null;

        return xml;
    }

    void fromXML(String path, Element e) {
        NodeList list = e.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element child = (Element) node;
                String pathElem = path + "/" + child.getTagName().toLowerCase();
                FormWrapper c = null;
                try {
                    int pos = Util.getPosTag(pathElem); //pour être sur que
                    if (isFilled()) {
                        if (!isMultipleContainer) return; //pas d'import multiple possible
                        addFormContent();
                        c = (FormWrapper) vComponents.lastElement();
                    } else
                        c = (FormWrapper) vComponents.firstElement();

                    c.fromXML(pathElem, child, false);
                } catch (IllegalTagException ite) {
                }

            }
        }
    }
}
