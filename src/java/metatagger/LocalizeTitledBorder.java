package metatagger;

import javax.swing.border.Border;

class LocalizeTitledBorder extends IconTitledBorder {
    public LocalizeTitledBorder(String title) {
        super(title);
    }

    public LocalizeTitledBorder(Border border, String title) {
        super(border, title);
    }

    public String getTitle() {
        if (super.getTitle().equals("")) return "";
        return Util.getLabel(super.getTitle());
    }
}