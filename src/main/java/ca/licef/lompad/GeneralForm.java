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

class GeneralForm extends FormContainer {
    public GeneralForm(String title, boolean isLine) {
        super(title, isLine);
    }

    void addFormContent() {
        IdentifierForm identifier = new IdentifierForm("1.1", true, true);
        identifier.addToggle();
        identifier.addFormContent();
        addComponent(identifier);

        LangstringForm title = new LangstringForm("1.2", true, true, true);
        title.addToggle();
        title.addFormContent();
        addComponent(title);

        VocabularyForm language = new VocabularyForm("1.3", true, true,
                LangstringForm.values, new Object[]{null, "none"}, false);
        language.setEditable(true);
        language.addToggle();
        language.addFormContent();
        addComponent(language);

        MultiLangstringForm description = new MultiLangstringForm("1.4", true, true, false, "", true);
        description.addToggle();
        description.addFormContent();
        addComponent(description);

        MultiLangstringForm keyword = new MultiLangstringForm("1.5", true, true, true, "", true);
        keyword.addToggle();
        keyword.addFormContent();
        addComponent(keyword);

        MultiLangstringForm coverage = new MultiLangstringForm("1.6", true, true, true, "", true);
        coverage.addToggle();
        coverage.addFormContent();
        addComponent(coverage);

        VocabularyForm structure = new VocabularyForm("1.7", true, false, null, true);
        structure.addToggle();
        structure.setAlignRight();
        structure.addFormContent();
        addComponent(structure);

        VocabularyForm aggregationLevel = new VocabularyForm("1.8", true, false, null, true);
        aggregationLevel.addToggle();
        aggregationLevel.setAlignRight();
        aggregationLevel.addFormContent();
        addComponent(aggregationLevel);
    }

    //HTML
    String toHTML(String key) {
        return toHTMLCategory(key);
    }
}
