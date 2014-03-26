package ca.licef.lompad;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: amiara
 * Date: 20-Apr-2006
 */
public class ExternalVocabularyComponent extends FormComponent{

    JComboBox jComboBoxVocabulary;
    String source;
    String value;

    public ExternalVocabularyComponent() {
        jComboBoxVocabulary = new JComboBox();
        jComboBoxVocabulary.setFont(new Font("Dialog", Font.PLAIN, 11));
        jPanelGauche.add(jComboBoxVocabulary);
    }

    boolean isFilled() {
        return jComboBoxVocabulary.getSelectedItem() != null;
    }

    //XML
    String toXML(String key) {
        String xml = null;
        if (isFilled())
            xml = "<source>" + source + "</source>\n" +
                    "<value>" + value + "</value>\n";
        return xml;
    }

    void fromXML(String path, Element e, boolean firstField) {
        NodeList listSrc = e.getElementsByTagNameNS(Util.lomNSURI,"source");
        Element childSrc = (Element) listSrc.item(0);
        source = childSrc.getFirstChild().getNodeValue();

        NodeList list = e.getElementsByTagNameNS(Util.lomNSURI,"value");
        Element child = (Element) list.item(0);
        value = child.getFirstChild().getNodeValue().trim();

        String profile = Util.getExternalProfileFromVocabularySource(source);
        String src = source;
        if (profile != null) //an application known profile ?
            src = profile;
        String item = src + ": " + value;

        if (firstField)
            jComboBoxVocabulary.addItem(null);
        jComboBoxVocabulary.addItem(item);
        jComboBoxVocabulary.setSelectedIndex(firstField?1:0);
    }

    //HTML
    String toHTML(String key) {
        String html = null;
        if (isFilled()) {
            String profile = Util.getExternalProfileFromVocabularySource(source);
            String src = source;
            if (profile != null) //an application known profile ?
                src = profile;

            html = src + ": " + value + "<br>";
        }
        return html;
    }
}
