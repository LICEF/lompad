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

import java.util.List;

public class LocalizeTaxon {

    public String id;
    public String uri;
    
    private LangStrings titles;

    public LocalizeTaxon(String id, String uri, LangStrings titles) {
        this.id = id;
        this.uri = uri;
        this.titles = titles;
    }

    public LocalizeTaxon(String id, LangStrings titles) {
        this.id = id;
        this.titles = titles;
    }

    public String getTitle( String language, boolean useShowTaxumIdPref ) {
        String title = titles.getString( language );
        if( title == null )
            return( id );
        if( useShowTaxumIdPref && Preferences.getInstance().isShowTaxumId() ) {
            if( id != null && !"".equals( id ) )
                return( id + " " + title );
        }
        return( title );
    }

    public List<String> getOrderedTitles() {
        return( titles.getOrderedStrings() );
    }

    public String toString() {
        String lang = Preferences.getInstance().getLocale().getLanguage();
        return( getTitle( lang, true ) );
    }

}
