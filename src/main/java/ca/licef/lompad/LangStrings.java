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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class LangStrings {

    public LangStrings(Map<String,String> strings) {
        this.strings = strings;
    }

    public String toString() {
        String lang = Preferences.getInstance().getLocale().getLanguage();
        return( getString( lang ) );
    }

    /*
     * If no string is provided for the asked language,
     * try to find a good substitute.
     * Either the only string if there is only one provided.
     * If more than one string is available, look for one that
     * has a language code that has the same prefix as the one
     * passed in parameter.  In other words, if we call getstring( "fr" )
     * and that we have a string that has a language code like "fr-CA", 
     * we will take it.  If this does not work,
     * take the English one or the French one.  
     * It will return null if no pertinent string is found.
     */
    public String getString( String language ) {
        String string = strings.get( language );

        if( string == null ) {
            if( strings.size() == 1 ) {
                Iterator<String> it = strings.values().iterator();
                string = it.next();
            }
            else {
                //removing country code
                for( Iterator<String> it = strings.keySet().iterator(); it.hasNext(); ) {
                    String key = it.next();
                    int indexOfDash = key.indexOf( "-" );
                    if( indexOfDash != -1 ) {
                        String lang = key.substring( 0, indexOfDash );
                        if( language.equals( lang ) ) 
                            return( strings.get( key ) );
                    }
                }

                //code ISO. 3 to 2 digits
                for( Iterator<String> it = strings.keySet().iterator(); it.hasNext(); ) {
                    String key = it.next();
                    if (key.length() > 2) {
                        String lang = key.substring( 0, 2 );
                        if( language.equals( lang ) )
                            return( strings.get( key ) );
                    }
                }

                string = strings.get( "en" );
                if( string == null )
                    string = strings.get( "fr" );
            }
        }
        return( string );
    }

    public List<String> getOrderedStrings() {
        List<String> orderedStrings = new ArrayList<String>();
        Object[] languages = strings.keySet().toArray();
        Arrays.sort( languages );
        for( Object lang : languages ) {
            orderedStrings.add( (String)lang );
            orderedStrings.add( (String)strings.get( lang ) );
        }
        return( orderedStrings );
    }

    private Map<String,String> strings;

}

