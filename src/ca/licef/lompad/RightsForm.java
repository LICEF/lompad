/*
 * Copyright (C) 2005  Alexis Miara (amiara@licef.teluq.uquebec.ca)
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

class RightsForm extends FormContainer {
    public RightsForm(String title, boolean isLine) {
        super(title, isLine);
    }

    void addFormContent() {
        VocabularyForm cost = new VocabularyForm("6.1", true, false, new Object[]{null, "6.1-1", "6.1-2"}, null, true);
        cost.setIcon(Util.redIcon);
        cost.addToggle();
        cost.setAlignRight();
        cost.addFormContent();
        addComponent(cost);

        VocabularyForm copyright = new VocabularyForm("6.2", true, false, new Object[]{null, "6.2-1", "6.2-2"}, null, true);
        copyright.setIcon(Util.redIcon);
        copyright.addToggle();
        copyright.setAlignRight();
        copyright.addFormContent();
        addComponent(copyright);

        LangstringForm description = new LangstringForm("6.3", true, true, false);
        description.setIcon(Util.redIcon);
        description.addToggle();
        description.addFormContent();
        addComponent(description);
    }
}