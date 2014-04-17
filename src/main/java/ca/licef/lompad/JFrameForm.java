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
    private JMenuItem itemProfileNone;
    private JMenuItem itemProfileCanCore;
    private JMenuItem itemProfileScorm;
    private JMenuItem itemProfileNormetic1;
    private JMenuItem itemProfileNormetic2;
    private JMenuItem itemProfileNormetic3;

    private JMenuItem itemPreferencesManageLocalClassifs;

    private JMenuItem itemHelpRefDoc;
    private JMenuItem itemAbout;

    private JPanelForm jPanelForm;
    private JMenu menuFile;
    private JMenu menuLanguage;
    private JMenu menuProfiles;
    private JMenu menuProfilesNormetic;
    private JMenu menuPreferences;
    private JMenu menuHelp;

    public JFrameForm() {
        setTitle("title");

        SymAction lSymAction = new SymAction();
        setJMenuBar(createMenu(lSymAction));

        getContentPane().setLayout(new BorderLayout(0, 0));
        setSize(1024, 800);

        jPanelForm = new JPanelForm();
        getContentPane().add(jPanelForm);

        setIconImage(Util.imageApp);

        SymWindow aSymWindow = new SymWindow();
        this.addWindowListener(aSymWindow);

        updateLocalization();

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    void updateLocalization() {
        ResourceBundle resBundle = ResourceBundle.getBundle("properties.JFrameFormRes", Util.locale);

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
        itemProfileNone.setText(resBundle.getString("none"));
        itemProfileNormetic1.setText(resBundle.getString("normetic1"));
        itemProfileNormetic2.setText(resBundle.getString("normetic2"));
        itemProfileNormetic3.setText(resBundle.getString("normetic3"));
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
        itemLanguageFrench.setSelected(true);
        itemLanguageFrench.addActionListener(lSymAction);
        menuLanguage.add(itemLanguageFrench);
        itemLanguageEnglish = new JRadioButtonMenuItem("english");
        itemLanguageEnglish.setFont(menuFile.getFont());
        itemLanguageEnglish.addActionListener(lSymAction);
        menuLanguage.add(itemLanguageEnglish);
        ButtonGroup groupmenuLanguage = new ButtonGroup();
        groupmenuLanguage.add(itemLanguageFrench);
        groupmenuLanguage.add(itemLanguageEnglish);
        mb.add(menuLanguage);

        menuProfiles = new JMenu("standards");
        menuProfiles.setFont(menuFile.getFont());
        itemProfileNone = new JMenuItem("none");
        itemProfileNone.setFont(menuFile.getFont());
        itemProfileNone.setSelected(true);
        itemProfileNone.addActionListener(lSymAction);
        menuProfiles.add(itemProfileNone);
        menuProfiles.addSeparator();
        itemProfileCanCore = new JMenuItem("CanCore");
        itemProfileCanCore.setFont(menuFile.getFont());
        itemProfileCanCore.addActionListener(lSymAction);
        menuProfiles.add(itemProfileCanCore);
        itemProfileScorm = new JMenuItem("SCORM");
        itemProfileScorm.setFont(menuFile.getFont());
        itemProfileScorm.addActionListener(lSymAction);
        menuProfiles.add(itemProfileScorm);
        menuProfilesNormetic = new JMenu("Normetic");
        menuProfilesNormetic.setFont(menuFile.getFont());
        itemProfileNormetic1 = new JMenuItem("normetic1");
        itemProfileNormetic1.setFont(menuFile.getFont());
        itemProfileNormetic1.addActionListener(lSymAction);
        menuProfilesNormetic.add(itemProfileNormetic1);
        itemProfileNormetic2 = new JMenuItem("normetic2");
        itemProfileNormetic2.setFont(menuFile.getFont());
        itemProfileNormetic2.addActionListener(lSymAction);
        menuProfilesNormetic.add(itemProfileNormetic2);
        itemProfileNormetic3 = new JMenuItem("normetic3");
        itemProfileNormetic3.setFont(menuFile.getFont());
        itemProfileNormetic3.addActionListener(lSymAction);
        menuProfilesNormetic.add(itemProfileNormetic3);
        menuProfiles.add(menuProfilesNormetic);
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
                JFrame1_windowClosing(null);
            else if (object == itemLanguageFrench) {
                Util.locale = Locale.FRENCH;
                updateLocalization();
                jPanelForm.changeLanguage("fr");
            } else if (object == itemLanguageEnglish) {
                Util.locale = Locale.ENGLISH;
                updateLocalization();
                jPanelForm.changeLanguage("en");
            } else if (object == itemProfileNone) {
                jPanelForm.changeToIEEE();
                jPanelForm.updateProfile(itemProfileNone.getText());
            }
            else if (object == itemProfileCanCore) {
                jPanelForm.changeStandardActionPerformed("CANCORE", false);
                jPanelForm.updateProfile(itemProfileCanCore.getText());
            }
            else if (object == itemProfileScorm) {
                jPanelForm.changeStandardActionPerformed("SCORM", false);
                jPanelForm.updateProfile(itemProfileScorm.getText());
            }
            else if (object == itemProfileNormetic1) {
                jPanelForm.changeStandardActionPerformed("NORMETIC1", false);
                jPanelForm.updateProfile("Normetic (" + itemProfileNormetic1.getText() + ")");
            }
            else if (object == itemProfileNormetic2) {
                jPanelForm.changeStandardActionPerformed("NORMETIC2", false);
                jPanelForm.updateProfile("Normetic (" + itemProfileNormetic2.getText() + ")");
            }
            else if (object == itemProfileNormetic3) {
                jPanelForm.changeStandardActionPerformed("NORMETIC3", false);
                jPanelForm.updateProfile("Normetic (" + itemProfileNormetic3.getText() + ")");
            }
            else if (object == itemPreferencesManageLocalClassifs ) {
                manageLocalClassifications();
            }
            else if (object == itemHelpRefDoc)
                Util.launchFile("http://ltsc.ieee.org/wg12/files/LOM_1484_12_1_v1_Final_Draft.pdf");
            else if (object == itemAbout)
                about();
        }
    }

    private void about() {
        JWindowAbout w = new JWindowAbout(this, false);
        w.setSize(376, 314);
        w.setVisible(true);
    }

    private void manageLocalClassifications() {
        JDialogManageClassifications dialogManageClassif = new JDialogManageClassifications( this );
        dialogManageClassif.setSize( 500, 400 );
        dialogManageClassif.setVisible( true );
    }

}
