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
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: amiara
 * Date: 2005-04-18
 */
public class JTextFieldPopup extends JTextField{

    PopupMenuMouseListener pop = new PopupMenuMouseListener();
    public JTextFieldPopup() {
        super();
        addMouseListener(pop);
    }

    public JTextFieldPopup(String text) {
        super(text);
        addMouseListener(pop);
    }

    class PopupMenuMouseListener extends MouseAdapter {
        private JMenuItem cutItem, copyItem, pasteItem, selectAllItem;
        private JTextComponent textComponent;
        String savedstring = "";
        String lastactionselected = "";

        JPopupMenu createPopup() {
            JPopupMenu popup = new JPopupMenu();
            ResourceBundle resBundle = ResourceBundle.getBundle("properties.PopupMouseListenerRes", Util.locale);
            Font font = new Font("Dialog", Font.PLAIN, 12);
            //Cut
            AbstractAction action = new AbstractAction(resBundle.getString("cut")) {
                public void actionPerformed(ActionEvent ae) {
                    lastactionselected = "c";
                    savedstring = textComponent.getText();
                    textComponent.cut();
                }
            };
            cutItem = popup.add(action);
            cutItem.setFont(font);

            //Copy
            action = new AbstractAction(resBundle.getString("copy")) {
                public void actionPerformed(ActionEvent ae) {
                    lastactionselected = "";
                    textComponent.copy();
                }
            };
            copyItem = popup.add(action);
            copyItem.setFont(font);

            //Paste
            action = new AbstractAction(resBundle.getString("paste")) {
                public void actionPerformed(ActionEvent ae) {
                    lastactionselected = "p";
                    savedstring = textComponent.getText();
                    textComponent.paste();
                }
            };
            pasteItem = popup.add(action);
            pasteItem.setFont(font);

            popup.addSeparator();

            //Select All
            action = new AbstractAction(resBundle.getString("selectall")) {
                public void actionPerformed(ActionEvent ae) {
                    lastactionselected = "s";
                    savedstring = textComponent.getText();
                    textComponent.selectAll();
                }
            };
            selectAllItem = popup.add(action);
            selectAllItem.setFont(font);

            return popup;
        }


        public void mouseClicked(MouseEvent e) {
            if (e.getModifiers() == InputEvent.BUTTON3_MASK) {
                if (!(e.getSource() instanceof JTextComponent)) {
                    return;
                }

                JPopupMenu popup = createPopup();

                textComponent = (JTextComponent) e.getSource();
                // 1.3 version
                //textComponent.requestFocus();
                // 1.4 preferred method
                textComponent.requestFocus();
                //textComponent.requestDefaultFocus();
                //textComponent.requestFocusInWindow();

                boolean enabled = textComponent.isEnabled();
                boolean editable = textComponent.isEditable();
                boolean nonempty = !(textComponent.getText() == null || textComponent.getText().equals(""));
                boolean marked = textComponent.getSelectedText() != null;
                boolean pasteAvailable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null)
                        .isDataFlavorSupported(DataFlavor.stringFlavor);

                cutItem.setEnabled(enabled && editable && marked);
                copyItem.setEnabled(enabled && marked);
                pasteItem.setEnabled(enabled && editable && pasteAvailable);
                selectAllItem.setEnabled(enabled && nonempty);
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }
}
