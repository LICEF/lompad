package metatagger;

import org.w3c.dom.Element;

import javax.swing.*;
import java.awt.*;

abstract class FormComponent extends JComponent {
    JPanel jPanelContent;
    JPanel jPanelGauche;
    JPanel jPanelDroite;
    JPanel jPanelControl;

    Component space;
    FormMediator mediator;

    FormComponent() {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        jPanelContent = new JPanel();
        jPanelGauche = new JPanel();
        jPanelDroite = new JPanel();
        jPanelControl = new JPanel();

        jPanelContent.setOpaque(false);
        jPanelContent.setLayout(new BoxLayout(jPanelContent, BoxLayout.X_AXIS));

        jPanelGauche.setOpaque(false);
        jPanelGauche.setLayout(new BorderLayout(0, 0));

        jPanelDroite.setOpaque(false);
        jPanelDroite.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 2));

        jPanelControl.setOpaque(false);
        jPanelControl.setLayout(new BoxLayout(jPanelControl, BoxLayout.X_AXIS));
        jPanelDroite.add(jPanelControl);

        jPanelContent.add(jPanelGauche);
        jPanelContent.add(jPanelDroite);
        add(jPanelContent);
    }

    FormComponent(FormMediator mediator) {
        this();
        this.mediator = mediator;
    }

    abstract boolean isFilled();

    Component getSpace() {
        return space;
    }

    void setSpace(Component space) {
        this.space = space;
    }

    void updateAfterAdded() {
    }

    void graphicalUpdate() {
    }

    //XML
    String toXML(String key) {
        return "feuille\n";
    }

    void fromXML(String path, Element e) {
        System.out.println("feuille");
    }
}