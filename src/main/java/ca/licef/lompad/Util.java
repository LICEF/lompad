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
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import licef.IOUtil;

class Util {

    //For UI Localization
    public static Locale locale = Locale.FRENCH;

    //For Metatagger Localization
    static ResourceBundle resBundleLabel;
    static ResourceBundle resBundleTag;
    static ResourceBundle resBundlePosTag;
    static ResourceBundle resBundleVocabulary;
    static ResourceBundle resBundleXMLVocabulary;
    static ResourceBundle resBundlePosVocabulary;
    static ResourceBundle resBundleExternalVocabularySource;

    static String externalProfile;
    static ResourceBundle resBundleProfileVocabulary;
    static ResourceBundle resBundleProfileXMLVocabulary;
    static ResourceBundle resBundleProfilePosVocabulary;
    static ResourceBundle resBundleProfileVocabularySource;

    static {
        ToolTipManager.sharedInstance().setDismissDelay( 8000 ); // 8 seconds.

        try {
            resBundleLabel = ResourceBundle.getBundle("properties.LomLabel", locale);
            resBundleTag = ResourceBundle.getBundle("properties.LomTag");
            resBundlePosTag = ResourceBundle.getBundle("properties.LomPosTag");
            resBundleVocabulary = ResourceBundle.getBundle("properties.LomVocabulary", locale);
            resBundleXMLVocabulary = ResourceBundle.getBundle("properties.LomVocabulary", new Locale(""));
            resBundlePosVocabulary = ResourceBundle.getBundle("properties.LomPosVocabulary");
            resBundleExternalVocabularySource = ResourceBundle.getBundle("properties.ExternalVocabularySource");
        } catch (Exception e) {
        }
    }

    static void setExternalVocabulary(String profile) {
        try {
            externalProfile = profile;
            resBundleProfileVocabulary = ResourceBundle.getBundle("properties." + profile + "_Vocabulary", locale);
            resBundleProfileXMLVocabulary = ResourceBundle.getBundle("properties." + profile + "_Vocabulary", new Locale(""));
            resBundleProfilePosVocabulary = ResourceBundle.getBundle("properties." + profile + "_PosVocabulary");
            resBundleProfileVocabularySource = ResourceBundle.getBundle("properties." + profile + "_VocabularySource");
        } catch (Exception e) { //no complete external vocabularies for this profile
            externalProfile = null;
            resBundleProfileVocabulary = null;
            resBundleProfileXMLVocabulary = null;
            resBundleProfilePosVocabulary = null;
            resBundleProfileVocabularySource = null;
        }
    }

    static String getBundleValue(ResourceBundle resBundle, String key) {
        String res = null;
        try {
            res = resBundle.getString(key);
        } catch (Exception e) {
        }
        return res;
    }

    static int getPosValue(ResourceBundle resBundle, String key) throws IllegalTagException {
        int pos = -1;
        String s = getBundleValue(resBundle, key);
        if (s != null)
            pos = Integer.parseInt(s);
        else
            throw new IllegalTagException();
        return pos;
    }

    static void setBundleLocale(Locale locale) {
        resBundleLabel = ResourceBundle.getBundle("properties.LomLabel", locale);
        resBundleVocabulary = ResourceBundle.getBundle("properties.LomVocabulary", locale);
        if (externalProfile != null)
            resBundleProfileVocabulary = ResourceBundle.getBundle("properties." + externalProfile + "_Vocabulary", locale);
    }

    static String getLabel(String key) {
        return getBundleValue(resBundleLabel, key);
    }

    static String getTag(String key) {
        return getBundleValue(resBundleTag, key);
    }

    static int getPosTag(String key) throws IllegalTagException {
        StringBuffer altKey = new StringBuffer();
        Pattern pattern = Pattern.compile( "(.+?):(.+?)(/|$)" );
        Matcher matcher = pattern.matcher( key );
        while( matcher.find() ) {
            matcher.appendReplacement( altKey, matcher.group( 2 ) + matcher.group( 3 ) );
        }
        matcher.appendTail( altKey );
        return getPosValue(resBundlePosTag, altKey.toString());
    }

    static String getVocabulary(String key) {
        if (key.startsWith("x")) {
            String profileLabel = externalProfile;
            int indexOfUnderscore = externalProfile.indexOf( "_" );
            if( indexOfUnderscore != -1 ) {
                profileLabel = externalProfile.substring( 0, indexOfUnderscore ) + " " + 
                    externalProfile.substring( indexOfUnderscore + 1 ).replaceAll( "p", "." );
            }
            return profileLabel + ": " + getBundleValue(resBundleProfileVocabulary, key);
        }
        else
            return getBundleValue(resBundleVocabulary, key);
    }

    static String getXMLVocabulary(String key) {
        if (key.startsWith("x"))
            return getBundleValue(resBundleProfileXMLVocabulary, key);
        else
            return getBundleValue(resBundleXMLVocabulary, key);
    }

    static int getPosVocabulary(String key, boolean isProfileKey) throws IllegalTagException {
        if (isProfileKey)
            return getPosValue(resBundleProfilePosVocabulary, key);
        else
            return getPosValue(resBundlePosVocabulary, key);
    }

    static String getProfileVocabularySource(String key) {
        return getBundleValue(resBundleProfileVocabularySource, key);
    }

    static String getExternalProfileFromVocabularySource(String key) {
        return getBundleValue(resBundleExternalVocabularySource, key);
    }

    public static String convertSpecialCharactersForXML(String str) {
        if (str == null)
            return ("");
        String res = "";
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '&')
                res += "&amp;";
            else if (c == '<')
                res += "&lt;";
            else if (c == '>')
                res += "&gt;";
            else if (c == '"')
                res += "&quot;";
            else
                res += c;
        }
        return res;
    }

    static Image imageApp;
    static Image imageAbout;
    static ImageIcon imageIconCollapse;
    static ImageIcon imageIconExpand;
    static ImageIcon plusIcon;
    static ImageIcon minusIcon;
    static ImageIcon wizardIcon;
    static ImageIcon cancoreIcon;
    static ImageIcon normeticIcon;
    static ImageIcon normeticDisabledIcon;
    static ImageIcon redIcon;
    static ImageIcon yellowIcon;
    static ImageIcon greenIcon;
    static ImageIcon greenRedIcon;
    static ImageIcon yellowRedIcon;
    static ImageIcon greenYellowRedIcon;
    static ImageIcon folderIcon;
    static ImageIcon fileIcon;

    static void initImageIcon(Class cl) {
        imageApp = getImage(cl, "app.gif");
        imageAbout = getImage(cl, "about.gif");
        imageIconExpand = new ImageIcon(getImage(cl, "handleExpand.gif"));
        imageIconCollapse = new ImageIcon(getImage(cl, "handleCollapse.gif"));
        plusIcon = new ImageIcon(getImage(cl, "plus.gif"));
        minusIcon = new ImageIcon(getImage(cl, "minus.gif"));
        wizardIcon = new ImageIcon(getImage(cl, "wizard.gif"));
        cancoreIcon = new ImageIcon(getImage(cl, "cancore.gif"));
        normeticIcon = new ImageIcon(getImage(cl, "normetic.gif"));
        normeticDisabledIcon = new ImageIcon(getImage(cl, "normeticDisabled.gif"));
        redIcon = new ImageIcon(getImage(cl, "red.gif"));
        yellowIcon = new ImageIcon(getImage(cl, "yellow.gif"));
        greenIcon = new ImageIcon(getImage(cl, "green.gif"));
        greenRedIcon = new ImageIcon(getImage(cl, "greenRed.gif"));
        yellowRedIcon = new ImageIcon(getImage(cl, "yellowRed.gif"));
        greenYellowRedIcon = new ImageIcon(getImage(cl, "greenYellowRed.gif"));
        folderIcon = new ImageIcon(getImage(cl, "folder.gif"));
        fileIcon = new ImageIcon(getImage(cl, "file.gif"));
    }

    public static Image getImage(Class cl, String name) {
        Image image = null;
        try {
            BufferedInputStream in = new BufferedInputStream(cl.getResourceAsStream("/images/" + name));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            IOUtil.copy(in, out);
            image = Toolkit.getDefaultToolkit().createImage(out.toByteArray());
        } catch (Exception e) {
            return null;
        }
        return image;
    }

    public static void copy(Reader in, Writer out) throws IOException {
        synchronized (in) {
            synchronized (out) {
                char[] buffer = new char[1024];
                while (true) {
                    int charRead = in.read(buffer);
                    if (charRead == -1) break;
                    out.write(buffer, 0, charRead);
                }
            }
        }
    }


    /**
     * Read a file from an URL and return its content as an array of lines.
     */
    public static Object[] readFile(String url) {
        Object[] res = null;
        ArrayList list = new ArrayList();
        String nextLine = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader((new URL(url)).openStream()));
            while ((nextLine = in.readLine()) != null)
                list.add(nextLine);
            in.close();
            res = list.toArray();
        } catch (IOException e) {
            System.out.println("e = " + e);
        }
        return res;
    }

    /**
     * Read a file found in the classpath and return its content as an array of lines.
     */
    public static Object[] readFile(Class cl, String filename) {
        Object[] res = null;
        ArrayList list = new ArrayList();
        String nextLine = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(cl.getResourceAsStream("/res/" + filename)));
            while ((nextLine = in.readLine()) != null)
                list.add(nextLine);
            in.close();
            res = list.toArray();
            return res;
        } catch (IOException e) {
            return null;
        }
    }

    /*
     * Retourne le JFrame conteneur le plus haut dans la hierarchie.
     * Dans le cas d'une applet, on a ainsi un frame pour ouvrir
     * des dialogs de type modal, qui restent par dessus le browser
     */
    public static JFrame getTopJFrame(Container c) {
        if (c instanceof JFrame) return (JFrame) c;
        Container theFrame = c;
        do {
            theFrame = theFrame.getParent();
        } while ((theFrame != null) && !(theFrame instanceof JFrame));
        if (theFrame == null) theFrame = new JFrame();
        return (JFrame) theFrame;
    }

    static void launchFile(String filePath) {
        String osName = (System.getProperty("os.name")).toLowerCase();
        String commandLine = null;
        if (osName.startsWith("windows")) {
            commandLine = (osName.startsWith("windows 9") || osName.startsWith("windows me")) ?
                    "start " + filePath :
                    "cmd /c start \"nom\" " + "\"" + filePath + "\"";
        }
        else if (osName.startsWith("mac"))
            commandLine = "/usr/bin/open " + filePath;
        else if (osName.startsWith("linux"))
            commandLine = "/usr/bin/xdg-open " +filePath;

        try {
            Runtime.getRuntime().exec(commandLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return the same string with padded 0s if needed in function of the passed length.
     */
    public static String completeDigit(String s, int length) {
        if (s.length() == length)
            return s;
        else {
            String prefix = "";
            for (int i = 0; i < length - s.length(); i++)
                prefix += "0";
            return prefix + s;
        }
    }

    public static boolean isShowHiddenDirectoryOptionAvailable() {
        String osName = (System.getProperty("os.name")).toLowerCase();
        return( osName.startsWith( "mac" ) || osName.startsWith( "linux" ) );
    }

    public static String getDataFolder() {
        String dataFolder = null;
        String osName = (System.getProperty("os.name")).toLowerCase();
        if( osName.startsWith( "windows" )  )
            dataFolder = System.getenv().get( "LOCALAPPDATA" ) + "/lompad";
        else if( osName.startsWith( "mac" ) || osName.startsWith( "linux" ) )
            dataFolder = System.getProperty( "user.home" ) + "/.lompad";
        if( dataFolder != null )
            IOUtil.createDirectory( dataFolder );
        return( dataFolder );
    }

    public static String getClassificationFolder() {
        String classifFolder = getDataFolder();
        if( classifFolder == null )
            return( null );
        classifFolder += "/classif";
        IOUtil.createDirectory( classifFolder );
        return( classifFolder );
    }

}
