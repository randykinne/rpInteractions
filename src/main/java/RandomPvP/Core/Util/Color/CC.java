package RandomPvP.Core.Util.Color;

import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.lang.Validate;
import com.google.common.collect.Maps;

/**
 * All supported color values for chat
 */
public enum CC {
    /**
     * Represents black
     */
    BLK('0', 0x00),
    /**
     * Represents dark blue
     */
    DBLU('1', 0x1),
    /**
     * Represents dark green
     */
    DGRN('2', 0x2),
    /**
     * Represents dark blue (aqua)
     */
    DAQU('3', 0x3),
    /**
     * Represents dark red
     */
    DRED('4', 0x4),
    /**
     * Represents dark purple
     */
    DPUR('5', 0x5),
    /**
     * Represents gold
     */
    GLD('6', 0x6),
    /**
     * Represents gray
     */
    GRY('7', 0x7),
    /**
     * Represents dark gray
     */
    DGRY('8', 0x8),
    /**
     * Represents blue
     */
    BLU('9', 0x9),
    /**
     * Represents green
     */
    GRN('a', 0xA),
    /**
     * Represents aqua
     */
    AQU('b', 0xB),
    /**
     * Represents red
     */
    RED('c', 0xC),
    /**
     * Represents light purple
     */
    PNK('d', 0xD),
    /**
     * Represents yellow
     */
    YLW('e', 0xE),
    /**
     * Represents white
     */
    WHT('f', 0xF),
    /**
     * Represents magical characters that change around randomly
     */
    SCR('k', 0x10, true),
    /**
     * Makes the text bold.
     */
    BLD('l', 0x11, true),
    /**
     * Makes a line appear through the text.
     */
    SRI('m', 0x12, true),
    /**
     * Makes the text appear underlined.
     */
    UND('n', 0x13, true),
    /**
     * Makes the text italic.
     */
    ITA('o', 0x14, true),
    /**
     * Resets all previous chat colors or formats.
     */
    RST('r', 0x15);
    /**
     * The special character which prefixes all chat colour codes. Use this if
     * you need to dynamically convert colour codes from your custom format.
     */
    public static final char COLOR_CHAR = '\u00A7';
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf(COLOR_CHAR) + "[0-9A-FK-OR]");
    private final int intCode;
    private final char code;
    private final boolean isFormat;
    private final String toString;
    private final static Map<Integer, CC> BY_ID = Maps.newHashMap();
    private final static Map<Character, CC> BY_CHAR = Maps.newHashMap();

    private CC(char code, int intCode) {
        this(code, intCode, false);
    }

    private CC(char code, int intCode, boolean isFormat) {
        this.code = code;
        this.intCode = intCode;
        this.isFormat = isFormat;
        this.toString = new String(new char[]{COLOR_CHAR, code});
    }

    /**
     * Gets the char value associated with this color
     *
     * @return A char value of this color code
     */
    public char getChar() {
        return code;
    }

    @Override
    public String toString() {
        return toString;
    }

    /**
     * Checks if this code is a format code as opposed to a color code.
     */
    public boolean isFormat() {
        return isFormat;
    }

    /**
     * Checks if this code is a color code as opposed to a format code.
     */
    public boolean isColor() {
        return !isFormat && this != RST;
    }

    /**
     * Gets the color represented by the specified color code
     *
     * @param code Code to check
     * @return Associative {@link org.bukkit.ChatColor} with the given code,
     * or null if it doesn't exist
     */
    public static CC getByChar(char code) {
        return BY_CHAR.get(code);
    }

    /**
     * Gets the color represented by the specified color code
     *
     * @param code Code to check
     * @return Associative {@link org.bukkit.ChatColor} with the given code,
     * or null if it doesn't exist
     */
    public static CC getByChar(String code) {
        Validate.notNull(code, "Code cannot be null");
        Validate.isTrue(code.length() > 0, "Code must have at least one char");
        return BY_CHAR.get(code.charAt(0));
    }

    /**
     * Strips the given message of all color codes
     *
     * @param input String to strip of color
     * @return A copy of the input string, without any coloring
     */
    public static String stripColor(final String input) {
        if (input == null) {
            return null;
        }
        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }

    /**
     * Translates a string using an alternate color code character into a
     * string that uses the internal ChatColor.COLOR_CODE color code
     * character. The alternate color code character will only be replaced if
     * it is immediately followed by 0-9, A-F, a-f, K-O, k-o, R or r.
     *
     * @param altColorChar    The alternate color code character to replace. Ex: &
     * @param textToTranslate Text containing the alternate color code character.
     * @return Text containing the ChatColor.COLOR_CODE color code character.
     */
    public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
        char[] b = textToTranslate.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
                b[i] = CC.COLOR_CHAR;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }
        return new String(b);
    }

    /**
     * Gets the ChatColors used at the end of the given input string.
     *
     * @param input Input string to retrieve the colors from.
     * @return Any remaining ChatColors to pass onto the next line.
     */
    public static String getLastColors(String input) {
        String result = "";
        int length = input.length();
        // Search backwards from the end as it is faster
        for (int index = length - 1; index > -1; index--) {
            char section = input.charAt(index);
            if (section == COLOR_CHAR && index < length - 1) {
                char c = input.charAt(index + 1);
                CC color = getByChar(c);
                if (color != null) {
                    result = color.toString() + result;
                    // Once we find a color or reset we can stop searching
                    if (color.isColor() || color.equals(RST)) {
                        break;
                    }
                }
            }
        }
        return result;
    }

    static {
        for (CC color : values()) {
            BY_ID.put(color.intCode, color);
            BY_CHAR.put(color.code, color);
        }
    }
}

