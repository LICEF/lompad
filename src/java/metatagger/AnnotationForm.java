package metatagger;


class AnnotationForm extends FormContainer {
    public AnnotationForm(String title, boolean isLine) {
        super(title, isLine);
    }

    void addFormContent() {
        EntityForm entity = new EntityForm("8.1", true, false);
        entity.setIcon(Util.greenIcon);
        entity.addToggle();
        entity.setAlignRight();
        entity.addFormContent();
        addComponent(entity);

        DatetimeForm date = new DatetimeForm("8.2", true, false);
        date.setIcon(Util.greenIcon);
        date.addToggle();
        date.addFormContent();
        addComponent(date);

        LangstringForm description = new LangstringForm("8.3", true, true, false);
        description.setIcon(Util.greenIcon);
        description.addToggle();
        description.addFormContent();
        addComponent(description);
    }
}