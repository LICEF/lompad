package metatagger;


class ClassificationForm extends FormContainer {
    public ClassificationForm(String title, boolean isLine) {
        super(title, isLine);
    }

    void addFormContent() {
        VocabularyForm purpose = new VocabularyForm("9.1", true, false,
                new Object[]{null, "9.1-1", "9.1-2", "9.1-3", "9.1-4",
                             "9.1-5", "9.1-6", "9.1-7", "9.1-8", "9.1-9"}, null, true);
        purpose.setIcon(Util.redIcon);
        purpose.addToggle();
        purpose.setAlignRight();
        purpose.addFormContent();
        addComponent(purpose);

        TaxonPathForm taxonPath = new TaxonPathForm("9.2", true, true);
        taxonPath.setIcon(Util.redIcon);
        taxonPath.addToggle();
        taxonPath.addFormContent();
        addComponent(taxonPath);

        LangstringForm description = new LangstringForm("9.3", true, true, false);
        description.setIcon(Util.greenIcon);
        description.addToggle();
        description.addFormContent();
        addComponent(description);

        MultiLangstringForm keyword = new MultiLangstringForm("9.4", true, true, true, "", true);
        keyword.setIcon(Util.greenIcon);
        keyword.addToggle();
        keyword.addFormContent();
        addComponent(keyword);
    }
}