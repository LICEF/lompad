/*
 * Copyright (C) 2007 Alexis Miara (alexis.miara@licef.ca)
 * 
 * This file is part of LomPad.
 * 
 * PALOMA is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 * 
 * PALOMA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with PALOMA; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package ca.licef.lompad;

import java.util.ArrayList;

public class LocalizeTaxon {

    public String id;
    public ArrayList titles;

    public LocalizeTaxon(String id, ArrayList titles) {
        this.id = id;
        this.titles = titles;
    }

    public String toString() {
        String lang = Util.locale.getLanguage();
        if ("".equals(lang))
            lang = "en";
        int index = titles.indexOf(lang);
        if (index == -1) //first choice if language not defined
            index = 0;

        String res = "";
        try {
            res = (String) titles.get(index + 1);
        } catch (IndexOutOfBoundsException e) {
        }
        if (id != null && !"".equals(id))
            res = id + "-" + res;

        return res;
    }
}


