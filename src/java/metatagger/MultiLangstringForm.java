package metatagger;

import org.w3c.dom.Element;

import java.util.Enumeration;

class MultiLangstringForm extends FormContainer {
    boolean isOneLine;
    String titleLocal;
    boolean isLineLocal;

    public MultiLangstringForm(String title, boolean isLine, boolean isMultiple, boolean isOneLine, String titleLocal, boolean isLineLocal) {
        super(title, isLine, isMultiple);
        this.isOneLine = isOneLine;
        this.titleLocal = titleLocal;
        this.isLineLocal = isLineLocal;
    }

    void addFormContent() {
        FormWrapper wrapper = new FormWrapper(titleLocal, isLineLocal);

        LangstringForm langstringForm = new LangstringForm(null, false, true, isOneLine);
        langstringForm.addFormContent();
        wrapper.addComponent(langstringForm);

        addComponent(wrapper);
    }

    //XML
    String toXML(String key) {
        String xml = "";
        for (Enumeration e = vComponents.elements(); e.hasMoreElements();) {
            FormComponent c = (FormComponent) e.nextElement();
            String res = ((FormWrapper) c).toXML(key, false);
            if (res != null) xml += res;
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