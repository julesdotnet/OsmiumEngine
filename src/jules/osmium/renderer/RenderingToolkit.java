package jules.osmium.renderer;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class RenderingToolkit {
	public static BufferedImage antiAliase(BufferedImage image) {
	    if (image == null) {
	        return null;
	    }

	    int width = image.getWidth();
	    int height = image.getHeight();
	    BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

	    for (int y = 1; y < height - 1; y++) {
	        for (int x = 1; x < width - 1; x++) {
	            int rgb = image.getRGB(x, y);
	            Color color = new Color(rgb, true);

	            if (color.getAlpha() == 0) {
	                continue;
	            }

	            int offsetUntilEnd = 0;

	            for (int i = 0; i < width; i++) {
	                int checkX = x + i;

	                if (checkX >= width) {
	                    break;
	                }

	                Color nextColor = new Color(image.getRGB(checkX, y), true);

	                if (nextColor.getAlpha() != 0) {
	                    int neighborYUp = y - 1;
	                    int neighborYDown = y + 1;

	                    boolean upperNeighborValid = neighborYUp >= 0 && new Color(image.getRGB(checkX, neighborYUp), true).getAlpha() != 0;
	                    boolean lowerNeighborValid = neighborYDown < height && new Color(image.getRGB(checkX, neighborYDown), true).getAlpha() != 0;

	                    if (upperNeighborValid || lowerNeighborValid) {
	                        offsetUntilEnd = i;
	                    }
	                } else {
	                    break;
	                }
	            }

	            for (int i = 0; i <= offsetUntilEnd; i++) {
	                int weakenedRed = Math.max(0, color.getRed() - (color.getRed() * i) / (offsetUntilEnd + 1));
	                int weakenedGreen = Math.max(0, color.getGreen() - (color.getGreen() * i) / (offsetUntilEnd + 1));
	                int weakenedBlue = Math.max(0, color.getBlue() - (color.getBlue() * i) / (offsetUntilEnd + 1));
	                int weakenedAlpha = Math.max(0, color.getAlpha() - (color.getAlpha() * i) / (offsetUntilEnd + 1));

	                output.setRGB(x + i, y, new Color(weakenedRed, weakenedGreen, weakenedBlue, weakenedAlpha).getRGB());
	            }
	        }
	    }

	    return output;
	}
}
