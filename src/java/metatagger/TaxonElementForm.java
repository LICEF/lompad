package metatagger;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;
import java.util.Enumeration;

class TaxonElementForm extends FormContainer {
    Object[] values = new Object[]{null, "fr", "en", "es"};

    public TaxonElementForm() {
        super(true);
        mediator = new LangstringMediator(this, values);
    }

    void addFormContent() {
        TaxonComponent c = new TaxonComponent((LangstringMediator) mediator, vComponents.size() == 0);
        c.setMinimumSize(new Dimension(10, 25));
        c.setPreferredSize(new Dimension(10, 25));
        c.setMaximumSize(new Dimension(2000, 25));
        addComponent(c);
    }

    //XML
    String toXML(String key) {
        String xml = "";
        String xml_Id = null;
        String xml_Entry = "";

        int i = 0;
        for (Enumeration e = vComponents.elements(); e.hasMoreElements(); i++) {
            TaxonComponent c = (TaxonComponent) e.nextElement();
            if (i == 0)
                xml_Id = c.toXML_Id(key);

            String res = c.toXML_Entry();
            if (res != null)
                xml_Entry += res + "\n";
        }

        if (xml_Id != null)
            xml += xml_Id;

        if (!xml_Entry.equals(""))
            xml += "<" + Util.getTag(key + ".2") + ">" + xml_Entry +
                    "</" + Util.getTag(key + ".2") + ">\n";

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
                try {
                    TaxonComponent c = null;
                    int pos = Util.getPosTag(pathElem);
                    c = (TaxonComponent) vComponents.firstElement();
                    if (pos == 1) //Id
                        c.fromXML_Id(child);
                    if (pos == 2) //Entry
                    {
                        if (isFilled()) {
                            addFormContent();
                            c = (TaxonComponent) vComponents.lastElement();
                        }

                        NodeList list2 = child.getElementsByTagName("string");
                        for (int i2 = 0; i2 < list2.getLength(); i2++) {
                            Node node2 = list2.item(i2);
                            if (node2.getNodeType() == Node.ELEMENT_NODE) {
                                Element child2 = (Element) node2;
                                if (c == null) {
                                    addFormContent();
                                    c = (TaxonComponent) vComponents.lastElement();
                                }
                                c.fromXML_Entry(child2);
                                c = null;
                            }
                        }
                    }
                } catch (IllegalTagException ite) {
                }
            }
        }
    }
}