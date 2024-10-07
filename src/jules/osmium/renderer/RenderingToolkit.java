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
                    continue; // Skip fully transparent pixels
                }
                
                if (isEdgePixel(image, x, y)) {
                    Color blendedColor = blendWithNeighbors(image, x, y);
                    output.setRGB(x, y, blendedColor.getRGB());
                } else {
                    output.setRGB(x, y, rgb); // Copy pixel as is
                }
            }
        }

        return output;
    }

    // Improved edge detection: considers color differences as well as transparency
    private static boolean isEdgePixel(BufferedImage img, int x, int y) {
        Color center = new Color(img.getRGB(x, y), true);
        Color[] neighbors = {
            new Color(img.getRGB(x - 1, y), true),
            new Color(img.getRGB(x + 1, y), true),
            new Color(img.getRGB(x, y - 1), true),
            new Color(img.getRGB(x, y + 1), true)
        };

        for (Color neighbor : neighbors) {
            if (colorDifference(center, neighbor) > 50) { // Threshold for detecting significant color change
                return true;
            }
        }
        return false;
    }

    // Function to calculate color difference based on RGB
    private static int colorDifference(Color c1, Color c2) {
        int redDiff = c1.getRed() - c2.getRed();
        int greenDiff = c1.getGreen() - c2.getGreen();
        int blueDiff = c1.getBlue() - c2.getBlue();
        int alphaDiff = c1.getAlpha() - c2.getAlpha();
        return (int) Math.sqrt(redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff + alphaDiff * alphaDiff);
    }

    // Function to blend a pixel with its neighbors
    private static Color blendWithNeighbors(BufferedImage img, int x, int y) {
        Color center = new Color(img.getRGB(x, y), true);
        Color[] neighbors = {
            new Color(img.getRGB(x - 1, y), true),
            new Color(img.getRGB(x + 1, y), true),
            new Color(img.getRGB(x, y - 1), true),
            new Color(img.getRGB(x, y + 1), true)
        };

        // Calculate average RGB and alpha values
        int totalRed = center.getRed();
        int totalGreen = center.getGreen();
        int totalBlue = center.getBlue();
        int totalAlpha = center.getAlpha();

        for (Color neighbor : neighbors) {
            totalRed += neighbor.getRed();
            totalGreen += neighbor.getGreen();
            totalBlue += neighbor.getBlue();
            totalAlpha += neighbor.getAlpha();
        }

        int numPixels = neighbors.length + 1;
        int avgRed = totalRed / numPixels;
        int avgGreen = totalGreen / numPixels;
        int avgBlue = totalBlue / numPixels;
        int avgAlpha = totalAlpha / numPixels;

        return new Color(avgRed, avgGreen, avgBlue, avgAlpha);
    }
}
