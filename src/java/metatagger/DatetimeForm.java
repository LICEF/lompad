package metatagger;

import java.awt.*;

class DatetimeForm extends FormContainer {
    public DatetimeForm(String title, boolean isLine, boolean isMultiple) {
        super(title, isLine, isMultiple);
    }

    void addFormContent() {
        DatetimeComponent date = new DatetimeComponent();
        date.setMinimumSize(new Dimension(10, 70));
        date.setPreferredSize(new Dimension(10, 70));
        date.setMaximumSize(new Dimension(2000, 70));
        addComponent(date);

        LangstringForm description = new LangstringForm("description", false, true, true);
        description.addFormContent();
        addComponent(description);
    }
}