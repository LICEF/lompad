package metatagger;

import org.w3c.dom.Element;

import javax.swing.*;
import java.awt.*;

class FormatComponent extends FormComponent {
    JTextField jTextField;
    JButton jButtonWizard;
    Component horizontalStrut;

    public FormatComponent(FormMediator mediator) {
        super(mediator);

        jTextField = new JTextField();
        jPanelGauche.add(jTextField);

        jButtonWizard = new JButton(Util.wizardIcon);
        jButtonWizard.setFont(new Font("Dialog", Font.PLAIN, 12));
        jButtonWizard.setFocusPainted(false);
        jButtonWizard.setBorderPainted(false);
        jButtonWizard.setPreferredSize(new Dimension(23, 23));
        horizontalStrut = Box.createHorizontalStrut(5);
        jPanelDroite.add(horizontalStrut);
        jPanelDroite.add(jButtonWizard);

        SymAction lSymAction = new SymAction();
        jButtonWizard.addActionListener(lSymAction);

        SymMouse aSymMouse = new SymMouse();
        jButtonWizard.addMouseListener(aSymMouse);
    }

    boolean isFilled() {
        return !jTextField.getText().equals("");
    }

    public void setEnabled(boolean b) {
        jTextField.setEditable(b);
        jTextField.setBackground(Color.white);

        horizontalStrut.setVisible(b);
        jButtonWizard.setVisible(b);
    }

    void updateAfterAdded() {
        jTextField.requestFocus();
    }

    class SymAction implements java.awt.event.ActionListener {
        public void actionPerformed(java.awt.event.ActionEvent event) {
            Object object = event.getSource();
            if (object == jButtonWizard)
                jButtonWizard_actionPerformed(event);
        }
    }

    void jButtonWizard_actionPerformed(java.awt.event.ActionEvent event) {
        JDialogFormatSelector jDialog =
                new JDialogFormatSelector(Util.getTopJFrame(this));
        jDialog.setVisible(true);
        if (!jDialog.bCancel)
            jTextField.setText(jDialog.format);

        jDialog.dispose();
    }

    class SymMouse extends java.awt.event.MouseAdapter {
        public void mouseEntered(java.awt.event.MouseEvent event) {
            Object object = event.getSource();
            if (object instanceof JButton)
                jButton_mouseInOut((JButton) object, true);
        }

        public void mouseExited(java.awt.event.MouseEvent event) {
            Object object = event.getSource();
            if (object instanceof JButton)
                jButton_mouseInOut((JButton) object, false);
        }
    }

    void jButton_mouseInOut(JButton jButton, boolean in) {
        jButton.setBorderPainted(in);
    }

    //XML
    String toXML(String key) {
        String xml = null;
        if (!jTextField.getText().equals(""))
            xml = Util.convertSpecialCharactersForXML(jTextField.getText()) + "\n";
        return xml;
    }

    void fromXML(String path, Element e) {
        if (e.getFirstChild() != null) {
            jTextField.setText(e.getFirstChild().getNodeValue().trim());
            jTextField.setCaretPosition(0);
        }
    }
}