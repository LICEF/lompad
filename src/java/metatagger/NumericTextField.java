package metatagger;

import javax.swing.*;
import java.awt.event.KeyEvent;

class NumericTextField extends JTextField {
    int length = -1;
    String minMask;
    String maxMask;
    int minValue;
    int maxValue;

    public NumericTextField() {
        this(-1, null, null);
    }

    public NumericTextField(int length, String minMask, String maxMask) {
        this.length = length;
        this.minMask = minMask;
        this.maxMask = maxMask;
        if (minMask != null)
            minValue = Integer.parseInt(minMask);
        if (maxMask != null)
            maxValue = Integer.parseInt(maxMask);

        this.addKeyListener(new SymKey());
    }

    boolean isFilled() {
        return (length != -1) ?
                getText().length() == length :
                !getText().equals("");
    }

    boolean isPermitted(char c) {
        //max size
        if (length != -1 && getText().length() == length)
            return false;

        //min mask
        if (minMask != null) {
            if (getText().length() == 0) {
                if (c < minMask.charAt(0))
                    return false;
            } else {
                if (getText().length() < minMask.length()) {
                    int value = Integer.parseInt(getText() + c);
                    if (value < minValue)
                        return false;
                }
            }
        }

        //max mask
        if (maxMask != null) {
            if (getText().length() == 0) {
                if (c > maxMask.charAt(0))
                    return false;
            } else {
                int value = Integer.parseInt(getText() + c);
                if (value > maxValue)
                    return false;
            }
        }

        return true;
    }

    public void paste() {
        //paste not available
    }

    class SymKey extends java.awt.event.KeyAdapter {
        public void keyTyped(java.awt.event.KeyEvent event) {
            Object object = event.getSource();
            if (object == NumericTextField.this)
                NumericTextField_keyTyped(event);
        }
    }

    void NumericTextField_keyTyped(java.awt.event.KeyEvent event) {
        char c = event.getKeyChar();
        boolean bConsume = false;
        if (!((c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE) ||
                (c == KeyEvent.VK_ENTER) || (c == KeyEvent.VK_TAB) ||
                Character.isDigit(c)))
            bConsume = true;

        if (Character.isDigit(c) && !isPermitted(c))
            bConsume = true;

        if (bConsume)
            event.consume();
    }
}

