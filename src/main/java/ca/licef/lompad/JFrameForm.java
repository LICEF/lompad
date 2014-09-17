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
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.ResourceBundle;

class JFrameForm extends JFrame {
    private JMenuItem itemNewFile;
    private JMenuItem itemOpenFile;
    private JMenuItem itemSaveFile;
    private JMenuItem itemSaveFileAs;
    private JMenuItem itemBrowseFolder;
    private JMenuItem itemViewHtml;
    private JMenuItem itemViewXml;
    private JMenuItem itemQuit;
    private JRadioButtonMenuItem itemLanguageFrench;
    private JRadioButtonMenuItem itemLanguageEnglish;
    private JMenuItem itemProfileIEEE;
    private JMenuItem itemProfileCanCore;
    private JMenuItem itemProfileScorm;
    private JMenuItem itemProfileNormetic_1_1_MandatoryElements;
    private JMenuItem itemProfileNormetic_1_1_MandatoryAndRecommendedElements;
    private JMenuItem itemProfileNormetic_1_1_AllElements;
    private JMenuItem itemProfileNormetic_1_2_MandatoryElements;
    private JMenuItem itemProfileNormetic_1_2_MandatoryAndRecommendedElements;
    private JMenuItem itemProfileNormetic_1_2_AllElements;

    private JMenuItem itemPreferencesManageLocalClassifs;

    private JMenuItem itemHelpRefDoc;
    private JMenuItem itemAbout;

    private JPanelForm jPanelForm;
    private JMenu menuFile;
    private JMenu menuLanguage;
    private JMenu menuProfiles;
    private JMenu menuProfilesNormetic_1_1;
    private JMenu menuProfilesNormetic_1_2;
    private JMenu menuPreferences;
    private JMenu menuHelp;

    public JFrameForm() {
        try {
            Preferences.getInstance().load();
        }
        catch( Exception e ) {
            e.printStackTrace();
        }

        setTitle("title");

        SymAction lSymAction = new SymAction();
        setJMenuBar(createMenu(lSymAction));

        getContentPane().setLayout(new BorderLayout(0, 0));
        setSize(1024, 800);

        jPanelForm = new JPanelForm();
        setApplicationProfileView( Preferences.getInstance().getApplicationProfileView());
        getContentPane().add(jPanelForm);

        setIconImage(Util.imageApp);

        SymWindow aSymWindow = new SymWindow();
        this.addWindowListener(aSymWindow);

        setLanguage( Preferences.getInstance().getLocale() );

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    public void setLanguage( Locale locale ) {
        try {
            Preferences.getInstance().setLocale( locale );
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
        updateLocalization();
        jPanelForm.changeLanguage( locale == Locale.FRENCH ? "fr" : "en" );
    }

    void updateLocalization() {
        ResourceBundle resBundle = ResourceBundle.getBundle("properties.JFrameFormRes", Preferences.getInstance().getLocale());

        //title
        String s = "";
        int index = getTitle().indexOf(" -");
        if (index != -1)
            s = getTitle().substring(index);
        setTitle(" " + resBundle.getString("title") + s);
        //

        menuFile.setText(resBundle.getString("file"));
        itemNewFile.setText(resBundle.getString("new"));
        itemOpenFile.setText(resBundle.getString("open"));
        itemSaveFile.setText(resBundle.getString("save"));
        itemSaveFileAs.setText(resBundle.getString("saveas"));
        itemBrowseFolder.setText(resBundle.getString("browseFolder"));
        itemViewHtml.setText(resBundle.getString("viewhtml"));
        itemViewXml.setText(resBundle.getString("viewxml"));
        itemQuit.setText(resBundle.getString("quit"));
        menuLanguage.setText(resBundle.getString("language"));
        itemLanguageFrench.setText(resBundle.getString("french"));
        itemLanguageEnglish.setText(resBundle.getString("english"));
        menuProfiles.setText(resBundle.getString("standards"));
        itemProfileIEEE.setText(resBundle.getString("IEEE"));
        itemProfileCanCore.setText(resBundle.getString("CANCORE"));
        itemProfileScorm.setText(resBundle.getString("SCORM"));
        itemProfileNormetic_1_1_MandatoryElements.setText(resBundle.getString("NORMETIC_1p1_MandatoryElements"));
        itemProfileNormetic_1_1_MandatoryAndRecommendedElements.setText(resBundle.getString("NORMETIC_1p1_MandatoryAndRecommendedElements"));
        itemProfileNormetic_1_1_AllElements.setText(resBundle.getString("NORMETIC_1p1_AllElements"));
        itemProfileNormetic_1_2_MandatoryElements.setText(resBundle.getString("NORMETIC_1p2_MandatoryElements"));
        itemProfileNormetic_1_2_MandatoryAndRecommendedElements.setText(resBundle.getString("NORMETIC_1p2_MandatoryAndRecommendedElements"));
        itemProfileNormetic_1_2_AllElements.setText(resBundle.getString("NORMETIC_1p2_AllElements"));
        menuPreferences.setText(resBundle.getString("preferences"));
        itemPreferencesManageLocalClassifs.setText(resBundle.getString("manageLocalClassifs"));
        menuHelp.setText(resBundle.getString("help"));
        itemHelpRefDoc.setText(resBundle.getString("ref"));
        itemAbout.setText(resBundle.getString("about"));

        jPanelForm.updateLocalization();
    }

    /**
     * The entry point for this application.
     * Sets the Look and Feel to the System Look and Feel.
     * Creates a new JFrame1 and makes it visible.
     */
    public static void main(String args[]) {
        try {
            JFrameForm app = new JFrameForm();
            JWindowAbout splash = new JWindowAbout(app, true);
            splash.setSize(376, 314);
            splash.setVisible(true);

            try {
                Classification.loadAll();
            }
            catch( Exception e ) {
                e.printStackTrace();
            }

            Thread.sleep(1500);
            app.setVisible(true);
            splash.setVisible(false);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    JMenuBar createMenu(SymAction lSymAction) {
        JMenuBar mb = new JMenuBar();

        menuFile = new JMenu("file");
        menuFile.setFont(new Font("Dialog", Font.PLAIN, 12));
        itemNewFile = new JMenuItem("new");
        itemNewFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        itemNewFile.setFont(menuFile.getFont());
        itemNewFile.addActionListener(lSymAction);
        menuFile.add(itemNewFile);
        itemOpenFile = new JMenuItem("open");
        itemOpenFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        itemOpenFile.setFont(menuFile.getFont());
        itemOpenFile.addActionListener(lSymAction);
        menuFile.add(itemOpenFile);
        itemSaveFile = new JMenuItem("save");
        itemSaveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        itemSaveFile.setFont(menuFile.getFont());
        itemSaveFile.addActionListener(lSymAction);
        menuFile.add(itemSaveFile);
        itemSaveFileAs = new JMenuItem("saveas");
        itemSaveFileAs.setFont(menuFile.getFont());
        itemSaveFileAs.addActionListener(lSymAction);
        menuFile.add(itemSaveFileAs);
        menuFile.addSeparator();

        itemBrowseFolder = new JMenuItem("browseFolder");
        itemBrowseFolder.setFont(menuFile.getFont());
        itemBrowseFolder.addActionListener(lSymAction);
        menuFile.add(itemBrowseFolder);
        menuFile.addSeparator();

        itemViewHtml = new JMenuItem("viewhtml");
        itemViewHtml.setFont(menuFile.getFont());
        itemViewHtml.addActionListener(lSymAction);
        menuFile.add(itemViewHtml);
        itemViewXml = new JMenuItem("viewxml");
        itemViewXml.setFont(menuFile.getFont());
        itemViewXml.addActionListener(lSymAction);
        menuFile.add(itemViewXml);
        menuFile.addSeparator();
        itemQuit = new JMenuItem("quit");
        itemQuit.setFont(menuFile.getFont());
        itemQuit.addActionListener(lSymAction);
        menuFile.add(itemQuit);
        mb.add(menuFile);

        menuLanguage = new JMenu("language");
        menuLanguage.setFont(menuFile.getFont());
        itemLanguageFrench = new JRadioButtonMenuItem("french");
        itemLanguageFrench.setFont(menuFile.getFont());
        itemLanguageFrench.addActionListener(lSymAction);
        menuLanguage.add(itemLanguageFrench);
        itemLanguageEnglish = new JRadioButtonMenuItem("english");
        itemLanguageEnglish.setFont(menuFile.getFont());
        itemLanguageEnglish.addActionListener(lSymAction);
        menuLanguage.add(itemLanguageEnglish);
        ButtonGroup groupmenuLanguage = new ButtonGroup();
        groupmenuLanguage.add(itemLanguageFrench);
        groupmenuLanguage.add(itemLanguageEnglish);
        if( Preferences.getInstance().getLocale() == Locale.FRENCH )
            itemLanguageFrench.setSelected( true );
        else 
            itemLanguageEnglish.setSelected( true );
        mb.add(menuLanguage);

        menuProfiles = new JMenu("standards");
        menuProfiles.setFont(menuFile.getFont());
        itemProfileIEEE = new JMenuItem("IEEE");
        itemProfileIEEE.setFont(menuFile.getFont());
        itemProfileIEEE.setSelected(true);
        itemProfileIEEE.addActionListener(lSymAction);
        menuProfiles.add(itemProfileIEEE);
        menuProfiles.addSeparator();
        itemProfileCanCore = new JMenuItem("CanCore");
        itemProfileCanCore.setFont(menuFile.getFont());
        itemProfileCanCore.addActionListener(lSymAction);
        menuProfiles.add(itemProfileCanCore);
        itemProfileScorm = new JMenuItem("SCORM");
        itemProfileScorm.setFont(menuFile.getFont());
        itemProfileScorm.addActionListener(lSymAction);
        menuProfiles.add(itemProfileScorm);
        menuProfilesNormetic_1_1 = new JMenu("Normetic 1.1");
        menuProfilesNormetic_1_1.setFont(menuFile.getFont());
        itemProfileNormetic_1_1_MandatoryElements = new JMenuItem("NORMETIC_1p1_MandatoryElements");
        itemProfileNormetic_1_1_MandatoryElements.setFont(menuFile.getFont());
        itemProfileNormetic_1_1_MandatoryElements.addActionListener(lSymAction);
        menuProfilesNormetic_1_1.add(itemProfileNormetic_1_1_MandatoryElements);
        itemProfileNormetic_1_1_MandatoryAndRecommendedElements = new JMenuItem("NORMETIC_1p1_MandatoryAndRecommendedElements");
        itemProfileNormetic_1_1_MandatoryAndRecommendedElements.setFont(menuFile.getFont());
        itemProfileNormetic_1_1_MandatoryAndRecommendedElements.addActionListener(lSymAction);
        menuProfilesNormetic_1_1.add(itemProfileNormetic_1_1_MandatoryAndRecommendedElements);
        itemProfileNormetic_1_1_AllElements = new JMenuItem("NORMETIC_1p1_AllElements");
        itemProfileNormetic_1_1_AllElements.setFont(menuFile.getFont());
        itemProfileNormetic_1_1_AllElements.addActionListener(lSymAction);
        menuProfilesNormetic_1_1.add(itemProfileNormetic_1_1_AllElements);
        menuProfiles.add(menuProfilesNormetic_1_1);
        menuProfilesNormetic_1_2 = new JMenu("Normetic 1.2");
        menuProfilesNormetic_1_2.setFont(menuFile.getFont());
        itemProfileNormetic_1_2_MandatoryElements = new JMenuItem("NORMETIC_1p2_MandatoryElements");
        itemProfileNormetic_1_2_MandatoryElements.setFont(menuFile.getFont());
        itemProfileNormetic_1_2_MandatoryElements.addActionListener(lSymAction);
        menuProfilesNormetic_1_2.add(itemProfileNormetic_1_2_MandatoryElements);
        itemProfileNormetic_1_2_MandatoryAndRecommendedElements = new JMenuItem("NORMETIC_1p2_MandatoryAndRecommendedElements");
        itemProfileNormetic_1_2_MandatoryAndRecommendedElements.setFont(menuFile.getFont());
        itemProfileNormetic_1_2_MandatoryAndRecommendedElements.addActionListener(lSymAction);
        menuProfilesNormetic_1_2.add(itemProfileNormetic_1_2_MandatoryAndRecommendedElements);
        itemProfileNormetic_1_2_AllElements = new JMenuItem("NORMETIC_1p2_AllElements");
        itemProfileNormetic_1_2_AllElements.setFont(menuFile.getFont());
        itemProfileNormetic_1_2_AllElements.addActionListener(lSymAction);
        menuProfilesNormetic_1_2.add(itemProfileNormetic_1_2_AllElements);
        menuProfiles.add(menuProfilesNormetic_1_2);
        mb.add(menuProfiles);

        menuPreferences = new JMenu("preferences");
        menuPreferences.setFont(menuFile.getFont());
        itemPreferencesManageLocalClassifs = new JMenuItem("manageLocalClassifs");
        itemPreferencesManageLocalClassifs.setFont(menuFile.getFont());
        itemPreferencesManageLocalClassifs.addActionListener(lSymAction);
        menuPreferences.add(itemPreferencesManageLocalClassifs);
        mb.add(menuPreferences);

        menuHelp = new JMenu("help");
        menuHelp.setFont(menuFile.getFont());
        itemHelpRefDoc = new JMenuItem("ref");
        itemHelpRefDoc.setFont(menuFile.getFont());
        itemHelpRefDoc.addActionListener(lSymAction);
        menuHelp.add(itemHelpRefDoc);
        menuHelp.addSeparator();
        itemAbout = new JMenuItem("about");
        itemAbout.setFont(menuFile.getFont());
        itemAbout.addActionListener(lSymAction);
        menuHelp.add(itemAbout);
        mb.add(menuHelp);

        return mb;
    }

    class SymWindow extends java.awt.event.WindowAdapter {
        public void windowClosing(java.awt.event.WindowEvent event) {
            Object object = event.getSource();
            if (object == JFrameForm.this)
                JFrame1_windowClosing(event);
        }
    }

    void JFrame1_windowClosing(java.awt.event.WindowEvent event) {
        try {
            if (jPanelForm.manageCurrentLom())
                System.exit(0);

        } catch (Exception e) {
        }
    }

    class SymAction implements java.awt.event.ActionListener {
        public void actionPerformed(java.awt.event.ActionEvent event) {
            Object object = event.getSource();

            if (object == itemNewFile)
                jPanelForm.newForm(true);
            else if (object == itemOpenFile)
                jPanelForm.openFile(itemOpenFile.getText());
            else if (object == itemSaveFile)
                jPanelForm.saveFile(itemSaveFile.getText());
            else if (object == itemSaveFileAs)
                jPanelForm.saveAsFile(itemSaveFileAs.getText());
            else if (object == itemBrowseFolder)
                jPanelForm.showBrowser();
            else if (object == itemViewHtml)
                jPanelForm.viewHTML();
            else if (object == itemViewXml)
                jPanelForm.viewXML();
            else if (object == itemQuit)
                JFrame1_windowClosing( null );
            else if (object == itemLanguageFrench)
                setLanguage( Locale.FRENCH );
            else if (object == itemLanguageEnglish)
                setLanguage( Locale.ENGLISH );
            else if (object == itemProfileIEEE)
                setApplicationProfileView("IEEE");
            else if (object == itemProfileCanCore)
                setApplicationProfileView("CANCORE");
            else if (object == itemProfileScorm)
                setApplicationProfileView("SCORM");
            else if (object == itemProfileNormetic_1_1_MandatoryElements)
                setApplicationProfileView("NORMETIC_1p1_MandatoryElements");
            else if (object == itemProfileNormetic_1_1_MandatoryAndRecommendedElements)
                setApplicationProfileView("NORMETIC_1p1_MandatoryAndRecommendedElements");
            else if (object == itemProfileNormetic_1_1_AllElements)
                setApplicationProfileView("NORMETIC_1p1_AllElements");
            else if (object == itemProfileNormetic_1_2_MandatoryElements)
                setApplicationProfileView("NORMETIC_1p2_MandatoryElements");
            else if (object == itemProfileNormetic_1_2_MandatoryAndRecommendedElements)
                setApplicationProfileView("NORMETIC_1p2_MandatoryAndRecommendedElements");
            else if (object == itemProfileNormetic_1_2_AllElements)
                setApplicationProfileView("NORMETIC_1p2_AllElements");
            else if (object == itemPreferencesManageLocalClassifs )
                manageLocalClassifications();
            else if (object == itemHelpRefDoc)
                Util.launchFile("http://helios.licef.ca:8080/LomPad/LOM_1484_12_1_v1_Final_Draft.pdf");
            else if (object == itemAbout)
                about();
        }
    }

    void setApplicationProfileView( String applProfileView ) {
        if( "IEEE".equals( applProfileView ) ) {
            jPanelForm.changeToIEEE();
            jPanelForm.updateProfile(itemProfileIEEE.getText());
        }
        else {
            jPanelForm.changeStandardActionPerformed( applProfileView, false);
            ResourceBundle resBundle = ResourceBundle.getBundle("properties.JFrameFormRes", Preferences.getInstance().getLocale());
            jPanelForm.updateProfile( resBundle.getString( applProfileView + "Label" ) );
        }
    }

    private void about() {
        JWindowAbout w = new JWindowAbout(this, false);
        w.setSize(376, 314);
        w.setVisible(true);
    }

    private void manageLocalClassifications() {
        JDialogManageClassifications dialogManageClassif = new JDialogManageClassifications( this );
        dialogManageClassif.setSize( 660, 400 );
        dialogManageClassif.setVisible( true );
    }

}
