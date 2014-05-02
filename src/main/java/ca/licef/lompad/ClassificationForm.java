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

class ClassificationForm extends FormContainer {
    public ClassificationForm(String title, boolean isLine) {
        super(title, isLine);
    }

    void addFormContent() {
        VocabularyForm purpose = new VocabularyForm("9.1", true, false, null, true);
        purpose.addToggle();
        purpose.setAlignRight();
        purpose.addFormContent();
        addComponent(purpose);

        TaxonPathForm taxonPath = new TaxonPathForm("9.2", true, true);
        taxonPath.addToggle();
        taxonPath.addFormContent();
        addComponent(taxonPath);

        LangstringForm description = new LangstringForm("9.3", true, true, false);
        description.addToggle();
        description.addFormContent();
        addComponent(description);

        MultiLangstringForm keyword = new MultiLangstringForm("9.4", true, true, true, "", true);
        keyword.addToggle();
        keyword.addFormContent();
        addComponent(keyword);
    }

    //HTML
    String toHTML(String key) {
        return toHTMLCategory(key);
    }
}
