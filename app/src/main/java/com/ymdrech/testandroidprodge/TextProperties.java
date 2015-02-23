package com.ymdrech.testandroidprodge;

/**
 * Created by e4t on 2/11/2015.
 */
public class TextProperties {

    private boolean isItalic;
    private boolean isBold;
    private int foregroundColour;
    private String font;
    private float kerning;
    private int backgroundColour;
    private float opacity;
    private int size;

    public boolean isItalic() {
        return isItalic;
    }

    public void setItalic(boolean isItalic) {
        this.isItalic = isItalic;
    }

    public boolean isBold() {
        return isBold;
    }

    public void setBold(boolean isBold) {
        this.isBold = isBold;
    }

    public int getForegroundColour() {
        return foregroundColour;
    }

    public void setForegroundColour(int foregroundColour) {
        this.foregroundColour = foregroundColour;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public float getKerning() {
        return kerning;
    }

    public void setKerning(float kerning) {
        this.kerning = kerning;
    }

    public int getBackgroundColour() {
        return backgroundColour;
    }

    public void setBackgroundColour(int backgroundColour) {
        this.backgroundColour = backgroundColour;
    }

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
