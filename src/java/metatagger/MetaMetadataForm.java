package metatagger;


class MetaMetadataForm extends FormContainer {
    public MetaMetadataForm(String title, boolean isLine) {
        super(title, isLine);
    }

    void addFormContent() {
        IdentifierForm identifier = new IdentifierForm("3.1", true, true);
        identifier.setIcon(Util.greenIcon);
        identifier.addToggle();
        identifier.addFormContent();
        addComponent(identifier);

        Object[] values = new Object[]{null, "3.2.1-1", "3.2.1-2"};
        ContributeForm contribute = new ContributeForm("3.2", true, Util.greenIcon, true, "3.2", values);
        contribute.setIcon(Util.greenIcon);
        contribute.addToggle();
        contribute.addFormContent();
        addComponent(contribute);

        TextForm metadataSchema = new TextForm("3.3", true, true, true);
        metadataSchema.setIcon(Util.redIcon);
        metadataSchema.addToggle();
        metadataSchema.addFormContent();
        addComponent(metadataSchema);

        VocabularyForm language = new VocabularyForm("3.4", true, false,
                new Object[]{null, "fr", "en", "es"}, null, false);
        language.setIcon(Util.greenIcon);
        language.setEditable(true);
        language.addToggle();
        language.setAlignRight();
        language.addFormContent();
        addComponent(language);
    }
}