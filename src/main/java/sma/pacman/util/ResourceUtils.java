package sma.pacman.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class ResourceUtils {

    private static Map<String, BufferedImage> imageResourceMap = new HashMap<String, BufferedImage>();

    public static BufferedImage getImage(String name) throws IOException {
        if(!imageResourceMap.containsKey(name)) {
            BufferedImage image = ImageIO.read(ResourceUtils.class.getResourceAsStream(name));
            imageResourceMap.put(name, image);
        }

        return imageResourceMap.get(name);
    }

}
