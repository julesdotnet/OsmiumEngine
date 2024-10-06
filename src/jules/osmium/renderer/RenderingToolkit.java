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
				// Collect colors from a 3x3 kernel
				int sumRed = 0, sumGreen = 0, sumBlue = 0, sumAlpha = 0;
				int count = 0;

				for (int ky = -1; ky <= 1; ky++) {
					for (int kx = -1; kx <= 1; kx++) {
						int neighborColor = image.getRGB(x + kx, y + ky);
						Color neighbor = new Color(neighborColor, true);

						sumRed += neighbor.getRed();
						sumGreen += neighbor.getGreen();
						sumBlue += neighbor.getBlue();
						sumAlpha += neighbor.getAlpha();
						count++;
					}
				}

				// Calculate the average color
				int avgRed = sumRed / count;
				int avgGreen = sumGreen / count;
				int avgBlue = sumBlue / count;
				int avgAlpha = sumAlpha / count;

				Color newColor = new Color(avgRed, avgGreen, avgBlue, avgAlpha);
				output.setRGB(x, y, newColor.getRGB());
			}
		}

		return output;
	}

}
