package metatagger;


class GeneralForm extends FormContainer {
    public GeneralForm(String title, boolean isLine) {
        super(title, isLine);
    }

    void addFormContent() {
        IdentifierForm identifier = new IdentifierForm("1.1", true, true);
        identifier.setIcon(Util.yellowIcon);
        identifier.addToggle();
        identifier.addFormContent();
        addComponent(identifier);

        LangstringForm title = new LangstringForm("1.2", true, true, true);
        title.setIcon(Util.redIcon);
        title.addToggle();
        title.addFormContent();
        addComponent(title);

        VocabularyForm language = new VocabularyForm("1.3", true, true,
                new Object[]{null, "none", "fr", "en", "es"}, new Object[]{null, "none"}, false);
        language.setIcon(Util.redIcon);
        language.setEditable(true);
        language.addToggle();
        language.addFormContent();
        addComponent(language);

        MultiLangstringForm description = new MultiLangstringForm("1.4", true, true, false, "", true);
        description.setIcon(Util.redIcon);
        description.addToggle();
        description.addFormContent();
        addComponent(description);

        MultiLangstringForm keyword = new MultiLangstringForm("1.5", true, true, true, "", true);
        keyword.setIcon(Util.redIcon);
        keyword.addToggle();
        keyword.addFormContent();
        addComponent(keyword);

        MultiLangstringForm coverage = new MultiLangstringForm("1.6", true, true, true, "", true);
        coverage.setIcon(Util.greenIcon);
        coverage.addToggle();
        coverage.addFormContent();
        addComponent(coverage);

        VocabularyForm structure = new VocabularyForm("1.7", true, false,
                new Object[]{null, "1.7-1", "1.7-2", "1.7-3", "1.7-4", "1.7-5"}, null, true);
        structure.setIcon(Util.greenIcon);
        structure.addToggle();
        structure.setAlignRight();
        structure.addFormContent();
        addComponent(structure);

        VocabularyForm aggregationLevel = new VocabularyForm("1.8", true, false,
                new Object[]{null, "1.8-1", "1.8-2", "1.8-3", "1.8-4"}, null, true);
        aggregationLevel.setIcon(Util.greenIcon);
        aggregationLevel.addToggle();
        aggregationLevel.setAlignRight();
        aggregationLevel.addFormContent();
        addComponent(aggregationLevel);
    }
}