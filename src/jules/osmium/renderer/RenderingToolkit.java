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
				
				if(hasAdjacentSolidPixel(image, x, y)) {
					output.setRGB(x, y, Color.red.getRGB());
				}

			}
		}

		return output;
	}

	private static boolean hasAdjacentSolidPixel(BufferedImage img, int x, int y) {
		Color colors[] = {
				new Color(img.getRGB(x - 1, y), true),
				new Color(img.getRGB(x + 1, y), true),
				new Color(img.getRGB(x, y + 1), true),
				new Color(img.getRGB(x, y - 1), true)
		};
		
		for(Color color : colors) {
			if(color.getAlpha() != 0) {
				return true;
			}
		}
		return false;
	}

}
