package metatagger;


class RightsForm extends FormContainer {
    public RightsForm(String title, boolean isLine) {
        super(title, isLine);
    }

    void addFormContent() {
        VocabularyForm cost = new VocabularyForm("6.1", true, false, new Object[]{null, "6.1-1", "6.1-2"}, null, true);
        cost.setIcon(Util.redIcon);
        cost.addToggle();
        cost.setAlignRight();
        cost.addFormContent();
        addComponent(cost);

        VocabularyForm copyright = new VocabularyForm("6.2", true, false, new Object[]{null, "6.2-1", "6.2-2"}, null, true);
        copyright.setIcon(Util.redIcon);
        copyright.addToggle();
        copyright.setAlignRight();
        copyright.addFormContent();
        addComponent(copyright);

        LangstringForm description = new LangstringForm("6.3", true, true, false);
        description.setIcon(Util.redIcon);
        description.addToggle();
        description.addFormContent();
        addComponent(description);
    }
}