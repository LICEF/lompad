package metatagger;


class FormMediator {
    protected FormContainer form;

    FormMediator(FormContainer form) {
        this.form = form;
    }

    void buttonAddComponentPerformed(FormComponent c) {
        c.updateAfterAdded();
    }

    void buttonRemoveComponentPerformed(FormComponent c) {
        int index = form.vComponents.indexOf(c);
        form.vComponents.removeElement(c);
        ((FormComponent) form.vComponents.elementAt(index - 1)).updateAfterAdded();
    }
}