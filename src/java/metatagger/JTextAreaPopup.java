package metatagger;

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
public class JTextAreaPopup extends JTextArea {

    PopupMenuMouseListener pop = new PopupMenuMouseListener();
    public JTextAreaPopup() {
        super();
        addMouseListener(pop);
    }

    public JTextAreaPopup(String text) {
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

