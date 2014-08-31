package sma.pacman.util;

import java.awt.*;

public abstract class ColorUtils {
    public static String toHexString(Color color) {
        return String.format("#%06x", (color.getRGB() & 0x00ffffff));
    }
}
