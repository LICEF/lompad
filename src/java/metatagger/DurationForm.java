package metatagger;

import java.awt.*;

class DurationForm extends FormContainer {
    public DurationForm(String title, boolean isLine, boolean isMultiple) {
        super(title, isLine, isMultiple);
    }

    void addFormContent() {
        DurationComponent duration = new DurationComponent();
        duration.setMinimumSize(new Dimension(10, 50));
        duration.setPreferredSize(new Dimension(10, 50));
        duration.setMaximumSize(new Dimension(2000, 50));
        addComponent(duration);

        LangstringForm description = new LangstringForm("description", false, true, true);
        description.addFormContent();
        addComponent(description);
    }
}