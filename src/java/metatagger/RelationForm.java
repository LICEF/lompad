package metatagger;


class RelationForm extends FormContainer {
    public RelationForm(String title, boolean isLine) {
        super(title, isLine);
    }

    void addFormContent() {
        VocabularyForm kind = new VocabularyForm("7.1", true, false,
                new Object[]{null, "7.1-1", "7.1-2", "7.1-3", "7.1-4", "7.1-5", "7.1-6",
                             "7.1-7", "7.1-8", "7.1-9", "7.1-10", "7.1-11", "7.1-12"}, null, true);
        kind.setIcon(Util.yellowIcon);
        kind.addToggle();
        kind.setAlignRight();
        kind.addFormContent();
        addComponent(kind);

        ResourceForm resource = new ResourceForm("7.2", true, false);
        resource.setIcon(Util.yellowIcon);
        resource.addToggle();
        resource.addFormContent();
        addComponent(resource);
    }
}