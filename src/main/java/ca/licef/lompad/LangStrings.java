/*
 * Copyright (C) 2014  Frederic Bergeron (frederic.bergeron@licef.ca)
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

import java.util.ArrayList;

class LocalizeValue {
    ArrayList titles;

    public LocalizeValue(ArrayList titles) {
        this.titles = titles;
    }

    public String toString() {
        String lang = Preferences.getInstance().getLocale().getLanguage();
        if ("".equals(lang))
            lang = "en";
        int i = titles.indexOf(lang);
        if (i == -1) //first choice if language not defined
            i = 0;
        return (String) titles.get(i + 1);
    }

}

