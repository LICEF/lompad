package metatagger;


class LifeCycleForm extends FormContainer {
    public LifeCycleForm(String title, boolean isLine) {
        super(title, isLine);
    }

    void addFormContent() {
        LangstringForm version = new LangstringForm("2.1", true, true, true);
        version.setIcon(Util.redIcon);
        version.addToggle();
        version.addFormContent();
        addComponent(version);

        VocabularyForm status = new VocabularyForm("2.2",
                true, false, new Object[]{null, "2.2-1", "2.2-2", "2.2-3", "2.2-4"}, null, true);
        status.setIcon(Util.yellowIcon);
        status.addToggle();
        status.setAlignRight();
        status.addFormContent();
        addComponent(status);

        Object[] values = new Object[]{
            null, "2.3.1-1", "2.3.1-2", "2.3.1-3", "2.3.1-4", "2.3.1-5", "2.3.1-6", "2.3.1-7",
            "2.3.1-8", "2.3.1-9", "2.3.1-10", "2.3.1-11", "2.3.1-12", "2.3.1-13", "2.3.1-14", "2.3.1-15"};
        ContributeForm contribute = new ContributeForm("2.3", true, Util.redIcon, true, "2.3", values);
        contribute.setIcon(Util.redIcon);
        contribute.addToggle();
        contribute.addFormContent();
        addComponent(contribute);
    }
}