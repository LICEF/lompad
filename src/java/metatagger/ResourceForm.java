package metatagger;


class ResourceForm extends FormContainer {
    public ResourceForm(String title, boolean isLine, boolean isMultiple) {
        super(title, isLine, isMultiple);
    }

    void addFormContent() {
        IdentifierForm identifier = new IdentifierForm("7.2.1", true, true);
        identifier.setIcon(Util.yellowIcon);
        identifier.addToggle();
        identifier.addFormContent();
        addComponent(identifier);

        MultiLangstringForm description = new MultiLangstringForm("7.2.2", true, true, false, "", true);
        description.setIcon(Util.greenIcon);
        description.addToggle();
        description.addFormContent();
        addComponent(description);
    }
}