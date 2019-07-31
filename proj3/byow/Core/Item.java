package byow.Core;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

public class Item {

    private final char character; // Do not rename character or the autograder will break.
    private final Color textColor;
    private final Color backgroundColor;
    private final String description;
    private final String filepath;

    /**
     * Full constructor for Item objects.
     * @param character The character displayed on the screen.
     * @param textColor The color of the character itself.
     * @param backgroundColor The color drawn behind the character.
     * @param description The description of the item, shown in the GUI on hovering over the item.
     * @param filepath Full path to image to be used for this item. Must be correct size (16x16)
     */
    public Item(char character, Color textColor, Color backgroundColor, String description, String filepath) {
        this.character = character;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        this.description = description;
        this.filepath = filepath;
    }

    /**
     * Constructor without filepath. In this case, filepath will be null, so when drawing, we
     * will not even try to draw an image, and will instead use the provided character and colors.
     * @param character The character displayed on the screen.
     * @param textColor The color of the character itself.
     * @param backgroundColor The color drawn behind the character.
     * @param description The description of the item, shown in the GUI on hovering over the item.
     */
    public Item(char character, Color textColor, Color backgroundColor, String description) {
        this.character = character;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        this.description = description;
        this.filepath = null;
    }

    /** Character representation of the item. Used for drawing in text mode.
     * @return character representation
     */
    public char character() {
        return character;
    }

    /**
     * Description of the item. Useful for displaying mouseover text or
     * testing that two items represent the same type of thing.
     * @return description of the item
     */
    public String description() {
        return description;
    }

    /**
     * Draws the item to the screen at location x, y. If a valid filepath is provided,
     * we draw the image located at that filepath to the screen. Otherwise, we fall
     * back to the character and color representation for the tile.
     *
     * Note that the image provided must be of the right size (16x16). It will not be
     * automatically resized or truncated.
     * @param x x coordinate
     * @param y y coordinate
     */
    public void drawItem(double x, double y) {
        double size = 1.0;
        if (filepath != null) {
            try {
                StdDraw.picture(x + size, y + size, filepath);
                return;
            } catch (IllegalArgumentException e) {
                // Exception happens because the file can't be found. In this case, fail silently
                // and just use the character and background color for the tile.
            }
        }

        StdDraw.setPenColor(backgroundColor);
        StdDraw.filledSquare(x + size, y + size, size);
        StdDraw.setPenColor(textColor);
        StdDraw.text(x + size, y + size, Character.toString(character()));
    }

    /**
     * simple equal method
     * @param i
     * @return
     */
    public boolean equal(Item i) {
        if (this.description == i.description) {
            return true;
        }
        return false;
    }
}
