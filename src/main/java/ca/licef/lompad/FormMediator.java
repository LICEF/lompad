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
        try {
        ((FormComponent) form.vComponents.elementAt(index - 1)).updateAfterAdded();
        } catch (Exception e) {
        }
    }
}
