package metatagger;

import javax.swing.filechooser.FileFilter;
import java.io.File;

class XMLFilter extends FileFilter {

    //Accept all directories and xml files.
    public boolean accept(File f) {

        String ext = null;
        if (f.isDirectory()) {
            return true;
        }

        ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }

        return "xml".equals(ext);
    }

    //The description of this filter
    public String getDescription() {
        return "XML files";
    }
}
