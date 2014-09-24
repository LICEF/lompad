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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LocalizeTaxon {

    public String id;
    public String uri;
    
    private Map<String,String> titles = new HashMap<String,String>();

    public LocalizeTaxon(String id, String uri, Map<String,String> titles) {
        this.id = id;
        this.uri = uri;
        this.titles = titles;
    }

    public LocalizeTaxon(String id, Map<String,String> titles) {
        this.id = id;
        this.titles = titles;
    }

    public String getTitle( String language ) {
        String title = Util.getTitle( language, titles );
        if( title == null )
            return( id );
        if( Preferences.getInstance().isShowTaxumId() ) {
            if( id != null && !"".equals( id ) )
                return( id + " " + title );
        }
        return( title );
    }

    public List<String> getOrderedTitles() {
        List<String> orderedTitles = new ArrayList<String>();
        Object[] languages = titles.keySet().toArray();
        Arrays.sort( languages );
        for( Object lang : languages ) {
            orderedTitles.add( (String)lang );
            orderedTitles.add( (String)titles.get( lang ) );
        }
        return( orderedTitles );
    }

    public String toString() {
        String lang = Preferences.getInstance().getLocale().getLanguage();
        return( getTitle( lang ) );
    }

}
