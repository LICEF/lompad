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

import javax.swing.*;
import java.awt.*;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

class LightCalendar extends JComponent {
    public static final Color   BG_MONTH_NAME       = new Color(117, 123, 114);
    public static final Color   FG_MONTH_NAME       = new Color(225, 222, 215);
    public static final Color   BG_DAY_NAME         = new Color(117, 123, 114);
    public static final Color   FG_DAY_NAME         = new Color(225, 222, 215);
    public static final Color   BG_DAY              = Color.white;
    public static final Color   FG_DAY              = Color.black;
    public static final Color   SELECTION_COLOR     = Color.blue;
    public static final Color   TODAY_FRAME_COLOR   = Color.red;

    JDialogDateSelector parent;

    /**
     * Le parametre calendrier peut etre null si aucun update n'est requis
     * sur le parent lorsque l'on selectionne une journee du petit calendrier.
     */
    public LightCalendar(JDialogDateSelector parent, boolean drawMonthName) {
        super();
        this.parent = parent;

        offGraphics = null;
        offImage = null;

        this.drawMonthName = drawMonthName;

        selectedDate = null;
        selectedRow = -1;
        selectedCol = -1;

        font = new Font( "Helvetica", Font.PLAIN, 12 );
        fontHighlighted = new Font( "Helvetica", Font.BOLD, 12 );

        //{{REGISTER_LISTENERS
        SymMouse aSymMouse = new SymMouse();
        this.addMouseListener(aSymMouse);
        //}}
    }

    public void setDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime( date );
        annee = cal.get( Calendar.YEAR );
        mois = cal.get( Calendar.MONTH );
        //Date dateAjustee = ajusteDate( currentDate );
        dateDebutMois = trouveDateDebutMois( date );

        selectionner(date);
    }

    public Date getDate() {
        return dateDebutMois;
    }

    public Date getDateSelectionnee() {
        return( selectedDate );
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        int width = getSize().width;
        int height = getSize().height;

        offImage = createImage( width, height );
        offGraphics = offImage.getGraphics();

        offGraphics.setFont( font );
        fm = offGraphics.getFontMetrics( font );

        int xPen = 0;
        int yPen = 0;

        DateFormatSymbols dfs = new DateFormatSymbols( Util.locale );

        // Nom du mois
        if( drawMonthName ) {
            String strMonthName = Util.capitalize( dfs.getMonths()[ mois ] ) + ", " + new Integer( annee ).toString();

            offGraphics.setColor( BG_MONTH_NAME );
            offGraphics.fillRect( xPen, yPen, (width - 1), fm.getHeight() + 2 );
            offGraphics.setColor( FG_MONTH_NAME );
            offGraphics.drawString( strMonthName, xPen + 5, fm.getHeight() );
            yPen += fm.getHeight() + 2;
        }

        int caseWidth = ( width - 2 ) / 7;

        // Nom des jours
        offGraphics.setColor( BG_DAY_NAME );
        offGraphics.fillRect( 1, yPen, width - 2, fm.getHeight() + 5 );

        offGraphics.setColor( FG_DAY_NAME );
        for( int i = 1; i <= 7; i++ ) {
            String strNomJour = Util.capitalize( dfs.getWeekdays()[ i ].substring( 0, 1 ) );
            
            int x = 1 + (i-1) * caseWidth + ( caseWidth - fm.stringWidth( strNomJour ) ) / 2;
            int y = yPen + fm.getHeight() - 1;
            offGraphics.drawString( strNomJour, x, y );
        }
        yPen += fm.getHeight() + 5;


        // Cases des jours
        Rectangle r = new Rectangle( 1, yPen, width - 2, height - yPen - 1);

        offGraphics.setFont( font );
        fm = offGraphics.getFontMetrics( font );

        offGraphics.setColor( BG_DAY );
        offGraphics.fillRect( r.x, r.y, r.width, r.height );

        int caseHeight = r.height / 7;
        Calendar cal = Calendar.getInstance();
        cal.setTime( dateDebutMois );

        int nbJoursVides = cal.get( Calendar.DAY_OF_WEEK ) - 1;
        int iJoursVides = 0;
        //int iJour = 1;

        yPen += 3;

        for( int iRow = 0; iRow < 6; iRow++ ) {
            for( int iCol = 0; iCol < 7; iCol++ ) {
                int day = cal.get( Calendar.DATE );
                if( iJoursVides < nbJoursVides )
                    iJoursVides++;
                else if( cal.get( Calendar.MONTH ) == mois ) {
                    String  strDay = (new Integer( day )).toString();
                    int     xCase = 3 + ( iCol * caseWidth );
                    int     yCase = yPen + ( iRow * caseHeight );
                    int     x = xCase + ( caseWidth - fm.stringWidth( strDay ) ) / 2;
                    //int     y = yCase + ( caseHeight - fm.getHeight() ) / 2 + fm.getHeight();
                    int     y = yCase + ( caseHeight - fm.getHeight() ) / 2 + fm.getHeight() - 2;

                    offGraphics.setFont( font );
                    offGraphics.setColor( FG_DAY );

                    // Selection
                    if( ( iRow == selectedRow ) && ( iCol == selectedCol ) ) {
                        offGraphics.setColor( SELECTION_COLOR );
                        offGraphics.drawRect( xCase + 2, yCase+1, caseWidth - 4, caseHeight);
                        offGraphics.setColor( FG_DAY );
                    }

                    offGraphics.drawString( strDay, x, y );

                    int dayTemp = cal.get( Calendar.DATE );
                    cal.add( Calendar.DATE, 1 );
                    while( cal.get( Calendar.DATE ) == dayTemp ) {
                        // Cette boucle inefficace est la pour contrer le probleme de l'heure avancee
                        cal.add( Calendar.MINUTE, 1 );
                    }
                }
            }
        }

        // Cadre
        offGraphics.setColor( Color.black );
        offGraphics.drawRect( 0, 0, width - 1, height - 6 );

        g.drawImage( offImage, 0, 5, null );
    }

    public void selectionner(Date date) {
        selectedDate = date;
        selectedRow = -1;
        selectedCol = -1;

        Calendar cal = Calendar.getInstance();
        cal.setTime( date );


        if( cal.get( Calendar.YEAR ) != annee )
            return;
        if( cal.get( Calendar.MONTH ) != mois )
            return;

        selectedCol = cal.get( Calendar.DAY_OF_WEEK ) - 1;

        Calendar calDebut = Calendar.getInstance();
        calDebut.setTime( dateDebutMois );
        int minJour = 1;
        int maxJour = Calendar.SATURDAY - calDebut.get( Calendar.DAY_OF_WEEK ) + 1;

        int jour = cal.get( Calendar.DATE );
        int testedRow = 0;

        while( ( selectedRow == -1 ) && ( testedRow < 6 ) ) {
            if( ( minJour <= jour ) && ( jour <= maxJour ) ) {
                selectedRow = testedRow;
            } else {
                minJour += maxJour - minJour + 1;
                maxJour += 7;
                testedRow += 1;
            }
        }

        repaint();
    }

    public void deselectionner() {
        selectedRow = -1;
        selectedCol = -1;
        repaint();
    }

    private Date trouveDateDebutMois(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime( date );
        cal.set( Calendar.DATE, 1 );
        cal.set( Calendar.HOUR_OF_DAY, 0 );
        cal.set( Calendar.MINUTE, 0 );
        cal.set( Calendar.SECOND, 0 );
        cal.set( Calendar.MILLISECOND, 0 );
        return cal.getTime();
    }

    // CV_Variables
    protected   int             annee;
    protected   Date            dateDebutMois;
    protected   boolean         drawMonthName;
    protected   Font            font;
    protected   Font            fontHighlighted;
    protected   FontMetrics     fm;
    protected   FontMetrics     fmHighlighted;
    protected   int             mois;
    protected   Date            selectedDate;
    protected   int             selectedRow;
    protected   int             selectedCol;

    private     Image           offImage;
    private     Graphics        offGraphics;

    class SymMouse extends java.awt.event.MouseAdapter
    {
        public void mousePressed(java.awt.event.MouseEvent event)
        {
            Object object = event.getSource();
            if (object == LightCalendar.this)
                LightCalendar_MousePress(event);
        }
    }

    // Cette fonction pourrait etre optimisee en calculant le deplacement directement
    // Pas besoin de boucler
    void LightCalendar_MousePress(java.awt.event.MouseEvent event)
    {
        int x = 1;
        int y = drawMonthName ? 2 * ( fm.getHeight() + 2 ) : ( fm.getHeight() );
        int w = getSize().width - 2;
        int h = getSize().height - y + 5;

        Rectangle r = new Rectangle( x, y, w, h  );

        if( r.contains( event.getPoint() ) ) {          
            int caseWidth = w / 7;
            int caseHeight = h / 7;

            Calendar cal = Calendar.getInstance();
            cal.setTime( dateDebutMois );

            int nbJoursVides = cal.get( Calendar.DAY_OF_WEEK ) - 1;
            int iJoursVides = 0;
            //int iJour = 1;

            Cases:
            for( int iRow = 0; iRow < 6; iRow++ ) {
                for( int iCol = 0; iCol < 7; iCol++ ) {
                    //int day = cal.get( Calendar.DATE );
                    if( iJoursVides < nbJoursVides )
                        iJoursVides++;
                    else if( cal.get( Calendar.MONTH ) == mois ) {
                        int xCase = 1 + ( iCol * caseWidth );
                        int yCase = y + ( iRow * caseHeight );
                        Rectangle rCase = new Rectangle( xCase, yCase + 5, caseWidth, caseHeight);

                        if( rCase.contains( event.getPoint() ) ) {
                            selectedDate = cal.getTime();
                            selectedRow = iRow;
                            selectedCol = iCol;
                            parent.currentDate = selectedDate;
                            repaint();
                            break Cases;
                        }

                        cal.add( Calendar.DATE, 1 );

                    }
                }
            }
        }
    }
}
