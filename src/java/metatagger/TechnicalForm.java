package metatagger;


class TechnicalForm extends FormContainer {
    public TechnicalForm(String title, boolean isLine) {
        super(title, isLine);
    }

    void addFormContent() {
        FormatForm format = new FormatForm("4.1", true, true);
        format.setIcon(Util.redIcon);
        format.addToggle();
        format.addFormContent();
        addComponent(format);

        TextForm size = new TextForm("4.2", true, false, true);
        size.setIcon(Util.yellowIcon);
        size.setNumericTextField(-1, "1", null);
        size.addToggle();
        size.setAlignRight();
        size.addFormContent();
        addComponent(size);

        TextForm location = new TextForm("4.3", true, true, true);
        location.setIcon(Util.redIcon);
        location.addToggle();
        location.addFormContent();
        addComponent(location);

        RequirementForm requirement = new RequirementForm("4.4", true, true);
        requirement.setIcon(Util.greenIcon);
        requirement.addToggle();
        requirement.addFormContent();
        addComponent(requirement);

        LangstringForm installationRemarks = new LangstringForm("4.5", true, true, false);
        installationRemarks.setIcon(Util.yellowIcon);
        installationRemarks.addToggle();
        installationRemarks.addFormContent();
        addComponent(installationRemarks);

        LangstringForm otherPlatformRequirements = new LangstringForm("4.6", true, true, false);
        otherPlatformRequirements.setIcon(Util.yellowIcon);
        otherPlatformRequirements.addToggle();
        otherPlatformRequirements.addFormContent();
        addComponent(otherPlatformRequirements);

        DurationForm duration = new DurationForm("4.7", true, false);
        duration.setIcon(Util.greenIcon);
        duration.addToggle();
        duration.addFormContent();
        addComponent(duration);
    }
}