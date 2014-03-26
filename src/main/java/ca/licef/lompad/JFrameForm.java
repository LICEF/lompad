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
    private JMenuItem item11;
    private JMenuItem item12;
    private JMenuItem item13;
    private JMenuItem item14;
    private JMenuItem itemBrowseFolder;
    private JMenuItem item15;
    private JMenuItem item16;
    private JMenuItem item17;
    private JRadioButtonMenuItem item21;
    private JRadioButtonMenuItem item22;
    private JMenuItem item31;
    private JMenuItem item32;
    private JMenuItem item33;
    private JMenuItem item341;
    private JMenuItem item342;
    private JMenuItem item343;

    private JMenuItem item41;
    private JMenuItem item42;

    private JPanelForm jPanelForm;
    private JMenu menu1;
    private JMenu menu2;
    private JMenu menu3;
    private JMenu menu34;
    private JMenu menu4;

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

        menu1.setText(resBundle.getString("file"));
        item11.setText(resBundle.getString("new"));
        item12.setText(resBundle.getString("open"));
        item13.setText(resBundle.getString("save"));
        item14.setText(resBundle.getString("saveas"));
        itemBrowseFolder.setText(resBundle.getString("browseFolder"));
        item15.setText(resBundle.getString("viewhtml"));
        item16.setText(resBundle.getString("viewxml"));
        item17.setText(resBundle.getString("quit"));
        menu2.setText(resBundle.getString("language"));
        item21.setText(resBundle.getString("french"));
        item22.setText(resBundle.getString("english"));
        menu3.setText(resBundle.getString("standards"));
        item31.setText(resBundle.getString("none"));
        item341.setText(resBundle.getString("normetic1"));
        item342.setText(resBundle.getString("normetic2"));
        item343.setText(resBundle.getString("normetic3"));
        menu4.setText(resBundle.getString("help"));
        item41.setText(resBundle.getString("ref"));
        item42.setText(resBundle.getString("about"));

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

        menu1 = new JMenu("file");
        menu1.setFont(new Font("Dialog", Font.PLAIN, 12));
        item11 = new JMenuItem("new");
        item11.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        item11.setFont(menu1.getFont());
        item11.addActionListener(lSymAction);
        menu1.add(item11);
        item12 = new JMenuItem("open");
        item12.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        item12.setFont(menu1.getFont());
        item12.addActionListener(lSymAction);
        menu1.add(item12);
        item13 = new JMenuItem("save");
        item13.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        item13.setFont(menu1.getFont());
        item13.addActionListener(lSymAction);
        menu1.add(item13);
        item14 = new JMenuItem("saveas");
        item14.setFont(menu1.getFont());
        item14.addActionListener(lSymAction);
        menu1.add(item14);
        menu1.addSeparator();

        itemBrowseFolder = new JMenuItem("browseFolder");
        itemBrowseFolder.setFont(menu1.getFont());
        itemBrowseFolder.addActionListener(lSymAction);
        menu1.add(itemBrowseFolder);
        menu1.addSeparator();

        item15 = new JMenuItem("viewhtml");
        item15.setFont(menu1.getFont());
        item15.addActionListener(lSymAction);
        menu1.add(item15);
        item16 = new JMenuItem("viewxml");
        item16.setFont(menu1.getFont());
        item16.addActionListener(lSymAction);
        menu1.add(item16);
        menu1.addSeparator();
        item17 = new JMenuItem("quit");
        item17.setFont(menu1.getFont());
        item17.addActionListener(lSymAction);
        menu1.add(item17);
        mb.add(menu1);

        menu2 = new JMenu("language");
        menu2.setFont(menu1.getFont());
        item21 = new JRadioButtonMenuItem("french");
        item21.setFont(menu1.getFont());
        item21.setSelected(true);
        item21.addActionListener(lSymAction);
        menu2.add(item21);
        item22 = new JRadioButtonMenuItem("english");
        item22.setFont(menu1.getFont());
        item22.addActionListener(lSymAction);
        menu2.add(item22);
        ButtonGroup groupMenu2 = new ButtonGroup();
        groupMenu2.add(item21);
        groupMenu2.add(item22);
        mb.add(menu2);

        menu3 = new JMenu("standards");
        menu3.setFont(menu1.getFont());
        item31 = new JMenuItem("none");
        item31.setFont(menu1.getFont());
        item31.setSelected(true);
        item31.addActionListener(lSymAction);
        menu3.add(item31);
        menu3.addSeparator();
        item32 = new JMenuItem("CanCore");
        item32.setFont(menu1.getFont());
        item32.addActionListener(lSymAction);
        menu3.add(item32);
        item33 = new JMenuItem("SCORM");
        item33.setFont(menu1.getFont());
        item33.addActionListener(lSymAction);
        menu3.add(item33);
        menu34 = new JMenu("Normetic");
        menu34.setFont(menu1.getFont());
        item341 = new JMenuItem("normetic1");
        item341.setFont(menu1.getFont());
        item341.addActionListener(lSymAction);
        menu34.add(item341);
        item342 = new JMenuItem("normetic2");
        item342.setFont(menu1.getFont());
        item342.addActionListener(lSymAction);
        menu34.add(item342);
        item343 = new JMenuItem("normetic3");
        item343.setFont(menu1.getFont());
        item343.addActionListener(lSymAction);
        menu34.add(item343);
        menu3.add(menu34);
        mb.add(menu3);

        menu4 = new JMenu("help");
        menu4.setFont(menu1.getFont());
        item41 = new JMenuItem("ref");
        item41.setFont(menu1.getFont());
        item41.addActionListener(lSymAction);
        menu4.add(item41);
        menu4.addSeparator();
        item42 = new JMenuItem("about");
        item42.setFont(menu1.getFont());
        item42.addActionListener(lSymAction);
        menu4.add(item42);
        mb.add(menu4);

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

            if (object == item11)
                jPanelForm.newForm(true);
            else if (object == item12)
                jPanelForm.openFile(item12.getText());
            else if (object == item13)
                jPanelForm.saveFile(item13.getText());
            else if (object == item14)
                jPanelForm.saveAsFile(item14.getText());
            else if (object == itemBrowseFolder)
                jPanelForm.showBrowser();
            else if (object == item15)
                jPanelForm.viewHTML();
            else if (object == item16)
                jPanelForm.viewXML();
            else if (object == item17)
                JFrame1_windowClosing(null);
            else if (object == item21) {
                Util.locale = Locale.FRENCH;
                updateLocalization();
                jPanelForm.changeLanguage("fr");
            } else if (object == item22) {
                Util.locale = Locale.ENGLISH;
                updateLocalization();
                jPanelForm.changeLanguage("en");
            } else if (object == item31) {
                jPanelForm.changeToIEEE();
                jPanelForm.updateProfile(item31.getText());
            }
            else if (object == item32) {
                jPanelForm.changeStandardActionPerformed("CANCORE", false);
                jPanelForm.updateProfile(item32.getText());
            }
            else if (object == item33) {
                jPanelForm.changeStandardActionPerformed("SCORM", false);
                jPanelForm.updateProfile(item33.getText());
            }
            else if (object == item341) {
                jPanelForm.changeStandardActionPerformed("NORMETIC1", false);
                jPanelForm.updateProfile("Normetic (" + item341.getText() + ")");
            }
            else if (object == item342) {
                jPanelForm.changeStandardActionPerformed("NORMETIC2", false);
                jPanelForm.updateProfile("Normetic (" + item342.getText() + ")");
            }
            else if (object == item343) {
                jPanelForm.changeStandardActionPerformed("NORMETIC3", false);
                jPanelForm.updateProfile("Normetic (" + item343.getText() + ")");
            }
            else if (object == item41)
                //Util.launchRessourceName("iexplore", "http://ltsc.ieee.org/wg12/files/LOM_1484_12_1_v1_Final_Draft.pdf");
            Util.launchFile("http://ltsc.ieee.org/wg12/files/LOM_1484_12_1_v1_Final_Draft.pdf");
            else if (object == item42)
                about();
        }
    }

    private void about() {
        JWindowAbout w = new JWindowAbout(this, false);
        w.setSize(376, 314);
        w.setVisible(true);
    }
}
