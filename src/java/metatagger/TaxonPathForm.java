package metatagger;

import org.w3c.dom.Element;

import java.util.Enumeration;

class TaxonPathForm extends FormContainer {
    public TaxonPathForm(String title, boolean isLine, boolean isMultiple) {
        super(title, isLine, isMultiple);
    }

    void addFormContent() {
        FormWrapper wrapper = new FormWrapper("", true);
        wrapper.addToggle();

        LangstringForm source = new LangstringForm("9.2.1", false, true, true);
        source.setIcon(Util.redIcon);
        source.addFormContent();
        wrapper.addComponent(source);

        wrapper.addSpace(5);

        TaxonForm taxon = new TaxonForm(null, false, true);
        taxon.addFormContent();
        wrapper.addComponent(taxon);

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