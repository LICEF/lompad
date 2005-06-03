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

class RelationForm extends FormContainer {
    public RelationForm(String title, boolean isLine) {
        super(title, isLine);
    }

    void addFormContent() {
        VocabularyForm kind = new VocabularyForm("7.1", true, false,
                new Object[]{null, "7.1-1", "7.1-2", "7.1-3", "7.1-4", "7.1-5", "7.1-6",
                             "7.1-7", "7.1-8", "7.1-9", "7.1-10", "7.1-11", "7.1-12"}, null, true);
        kind.setIcon(Util.yellowIcon);
        kind.addToggle();
        kind.setAlignRight();
        kind.addFormContent();
        addComponent(kind);

        ResourceForm resource = new ResourceForm("7.2", true, false);
        resource.setIcon(Util.yellowIcon);
        resource.addToggle();
        resource.addFormContent();
        addComponent(resource);
    }

    //HTML
    String toHTML(String key) {
        return toHTMLCategory(key);
    }
}
