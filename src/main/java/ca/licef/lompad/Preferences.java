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

import java.io.File;

public class Preferences {

    public static Preferences getInstance() {
        if( instance == null )
            instance = new Preferences();
        return( instance );
    }

    public boolean isShowHiddenFolders() {
        return( isShowHiddenFoldersEnabled );
    }

    public void setShowHiddenFolders( boolean isShowHiddenFoldersEnabled ) {
        this.isShowHiddenFoldersEnabled = isShowHiddenFoldersEnabled;
    }

    public boolean isShowTaxumId() {
        return( isShowTaxumIdEnabled );
    }

    public void setShowTaxumId( boolean isShowTaxumIdEnabled ) {
        this.isShowTaxumIdEnabled = isShowTaxumIdEnabled;
    }

    public File getPrevClassifDir() {
        return( prevClassifDir );
    }

    public void setPrevClassifDir( File prevClassifDir ) {
        this.prevClassifDir = prevClassifDir;
    }

    public int getPrevSelectedClassif() {
        return( prevSelectedClassif );
    }

    public void setPrevSelectedClassif( int prevSelectedClassif ) {
        this.prevSelectedClassif = prevSelectedClassif;
    }

    private Preferences() {
    }

    private boolean isShowHiddenFoldersEnabled;
    private boolean isShowTaxumIdEnabled;
    private File prevClassifDir = null;
    private int prevSelectedClassif = -1; 

    private static Preferences instance;

}
