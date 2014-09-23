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
        String title = titles.get( language );

        // If no title is provided for the asked language,
        // try to find a good substitute.
        // Either the only title if there is only one provided.
        // If more than one title is available, look for one that
        // has a language code that has the same prefix as the one
        // passed in parameter.  In other words, if we call getTitle( "fr" )
        // and that we have a title that has a language code like "fr-CA", 
        // we will take it.  If this does not work,
        // take the English one or the French one.  
        // If all fails, use the url.
        if( title == null ) {
            if( titles.size() == 1 ) {
                Iterator<String> it = titles.values().iterator();
                title = it.next();
            }
            else {
                for( Iterator<String> it = titles.keySet().iterator(); it.hasNext(); ) {
                    String key = it.next();
                    int indexOfDash = key.indexOf( "-" );
                    if( indexOfDash != -1 ) {
                        String lang = key.substring( 0, indexOfDash );
                        if( language.equals( lang ) ) 
                            return( titles.get( key ) );
                    }
                }

                title = titles.get( "en" );
                if( title == null )
                    title = titles.get( "fr" );
                if( title == null )
                    return( id );
            }
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
