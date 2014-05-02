/*
 * Copyright (C) 2005  Alexis Miara (alexis.miara@licef.ca)
 *
 * This file is part of LomPad.
 *
 * LomPad is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * LomPad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LomPad; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package ca.licef.lompad;

class TechnicalForm extends FormContainer {
    public TechnicalForm(String title, boolean isLine) {
        super(title, isLine);
    }

    void addFormContent() {
        FormatForm format = new FormatForm("4.1", true, true);
        format.addToggle();
        format.addFormContent();
        addComponent(format);

        TextForm size = new TextForm("4.2", true, false, true);
        size.setNumericTextField(-1, "1", null);
        size.addToggle();
        size.setAlignRight();
        size.addFormContent();
        addComponent(size);

        TextForm location = new TextForm("4.3", true, true, true);
        location.addToggle();
        location.addFormContent();
        addComponent(location);

        RequirementForm requirement = new RequirementForm("4.4", true, true);
        requirement.addToggle();
        requirement.addFormContent();
        addComponent(requirement);

        LangstringForm installationRemarks = new LangstringForm("4.5", true, true, false);
        installationRemarks.addToggle();
        installationRemarks.addFormContent();
        addComponent(installationRemarks);

        LangstringForm otherPlatformRequirements = new LangstringForm("4.6", true, true, false);
        otherPlatformRequirements.addToggle();
        otherPlatformRequirements.addFormContent();
        addComponent(otherPlatformRequirements);

        DurationForm duration = new DurationForm("4.7", true, false);
        duration.addToggle();
        duration.addFormContent();
        addComponent(duration);
    }

    //HTML
    String toHTML(String key) {
        return toHTMLCategory(key);
    }
}
