package metatagger;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: amiara
 * Date: 2004-06-23
 */
class JDialogQuestion extends JDialog {

    public static int CANCEL = 0;
    public static int NO = 1;
    public static int YES = 2;

    public int res;
    private JButton yes;
    private JButton no;
    private JButton cancel;

    public JDialogQuestion(JFrame parent, String title, String text) {
        super(parent, title, true);
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        JPanel jPanelNorth = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        JLabel jLabelText = new JLabel(text);
        jLabelText.setFont(new Font("Dialog", Font.PLAIN, 12));
        jPanelNorth.add(jLabelText);
        cp.add(BorderLayout.NORTH, jPanelNorth);
        JPanel jPanelSouth = new JPanel(new FlowLayout());
        yes = new JButton("yes");
        yes.setFont(jLabelText.getFont());
        no = new JButton("no");
        no.setFont(jLabelText.getFont());
        cancel = new JButton("cancel");
        cancel.setFont(jLabelText.getFont());

        jPanelSouth.add(yes);
        jPanelSouth.add(no);
        jPanelSouth.add(cancel);
        cp.add(BorderLayout.SOUTH, jPanelSouth);
        setSize(350, 125);

        SymAction lSymAction = new SymAction();
        yes.addActionListener(lSymAction);
        no.addActionListener(lSymAction);
        cancel.addActionListener(lSymAction);

        //Localization
        ResourceBundle resBundle = ResourceBundle.getBundle("properties.JDialogQuestionRes", Util.locale);
        setTitle(" " + resBundle.getString(getTitle()));
        jLabelText.setText(resBundle.getString(jLabelText.getText()));
        yes.setText(resBundle.getString(yes.getText()));
        no.setText(resBundle.getString(no.getText()));
        cancel.setText(resBundle.getString(cancel.getText()));
    }

    public void show() {
        Rectangle bounds = getParent().getBounds();
        Rectangle abounds = getBounds();
        setLocation(bounds.x + (bounds.width - abounds.width) / 2,
                bounds.y + (bounds.height - abounds.height) / 2);
        super.show();
    }

    class SymAction implements java.awt.event.ActionListener {
        public void actionPerformed(java.awt.event.ActionEvent event) {
            Object object = event.getSource();
            if (object == yes)
                res = YES;
            else if (object == no)
                res = NO;
            else if (object == no)
                res = CANCEL;
            setVisible(false);
        }
    }

}

