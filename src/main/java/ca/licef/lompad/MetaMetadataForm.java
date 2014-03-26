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

class MetaMetadataForm extends FormContainer {
    public MetaMetadataForm(String title, boolean isLine) {
        super(title, isLine);
    }

    void addFormContent() {
        IdentifierForm identifier = new IdentifierForm("3.1", true, true);
        identifier.setIcon(Util.greenIcon);
        identifier.addToggle();
        identifier.addFormContent();
        addComponent(identifier);

        ContributeForm contribute = new ContributeForm("3.2", true, Util.greenIcon, true);
        contribute.setIcon(Util.greenIcon);
        contribute.addToggle();
        contribute.addFormContent();
        addComponent(contribute);

        TextForm metadataSchema = new TextForm("3.3", true, true, true);
        metadataSchema.setIcon(Util.redIcon);
        metadataSchema.addToggle();
        metadataSchema.addFormContent();
        addComponent(metadataSchema);

        VocabularyForm language = new VocabularyForm("3.4", true, false,
                new Object[]{null, "fr", "en", "es"}, null, false);
        language.setIcon(Util.greenIcon);
        language.setEditable(true);
        language.addToggle();
        language.setAlignRight();
        language.addFormContent();
        addComponent(language);
    }

    //HTML
    String toHTML(String key) {
        return toHTMLCategory(key);
    }
}
