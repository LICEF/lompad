package metatagger;

import org.w3c.dom.Element;

import java.util.Enumeration;

class TaxonForm extends FormContainer {
    public TaxonForm(String title, boolean isLine, boolean isMultiple) {
        super(title, isLine, isMultiple);
    }

    void addFormContent() {
        FormWrapper wrapper = new FormWrapper("9.2.2", true);
        wrapper.setIcon(Util.redIcon);
        wrapper.addToggle();

        TaxonElementForm taxonElementForm = new TaxonElementForm();
        taxonElementForm.addFormContent();
        wrapper.addComponent(taxonElementForm);

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
        FormWrapper c = null;
        if (isFilled()) {
            if (!isMultipleContainer) return; //pas d'import multiple possible
            addFormContent();
            c = (FormWrapper) vComponents.lastElement();
        } else
            c = (FormWrapper) vComponents.firstElement();

        c.fromXML(path, e, false);
    }
}