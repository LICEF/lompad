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

class LifeCycleForm extends FormContainer {
    public LifeCycleForm(String title, boolean isLine) {
        super(title, isLine);
    }

    void addFormContent() {
        LangstringForm version = new LangstringForm("2.1", true, true, true);
        version.setIcon(Util.redIcon);
        version.addToggle();
        version.addFormContent();
        addComponent(version);

        VocabularyForm status = new VocabularyForm("2.2", true, false, null, true);
        status.setIcon(Util.yellowIcon);
        status.addToggle();
        status.setAlignRight();
        status.addFormContent();
        addComponent(status);

        ContributeForm contribute = new ContributeForm("2.3", true, Util.redIcon, true);
        contribute.setIcon(Util.redIcon);
        contribute.addToggle();
        contribute.addFormContent();
        addComponent(contribute);
    }

    //HTML
    String toHTML(String key) {
        return toHTMLCategory(key);
    }
}
