import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ResizeImages {
    public static void main(String[] args) throws Exception {
        File file = new File("images\\cartoon.jpg");
        BufferedImage img = ImageIO.read(file);
        BufferedImage newImage;

        int i = 0, counter, x2 = 0, y2 = 0;

        while (i <= 60) {
            i++;
            int min = Integer.MAX_VALUE, minVertical = -1; 

            for (int x = 0; x < img.getWidth(); x++) {
                double energy = 0;

                for (int y = 0; y < img.getHeight(); y++) {
                    double[] brightnesses = new double[8];

                    for (counter = 0; counter < 8; counter++) {
                        boolean notEdge = false;

                        // Define neighboring pixels
                        if (counter == 0 && x != 0) {
                            x2 = x - 1;
                            y2 = y;
                            notEdge = true;
                        }
                        if (counter == 1 && y != 0) {
                            x2 = x;
                            y2 = y - 1;
                            notEdge = true;
                        }
                        if (counter == 2 && y != img.getHeight() - 1) {
                            x2 = x;
                            y2 = y + 1;
                            notEdge = true;
                        }
                        if (counter == 3 && x != img.getWidth() - 1) {
                            x2 = x + 1;
                            y2 = y;
                            notEdge = true;
                        }
                        if (counter == 4 && x != 0 && y != 0) {
                            x2 = x - 1;
                            y2 = y - 1;
                            notEdge = true;
                        }
                        if (counter == 5 && x != img.getWidth() - 1 && y != 0) {
                            x2 = x + 1;
                            y2 = y - 1;
                            notEdge = true;
                        }
                        if (counter == 6 && x != img.getWidth() - 1 && y != img.getHeight() - 1) {
                            x2 = x + 1;
                            y2 = y + 1;
                            notEdge = true;
                        }
                        if (counter == 7 && x != 0 && y != img.getHeight() - 1) {
                            x2 = x - 1;
                            y2 = y + 1;
                            notEdge = true;
                        }

                        if (x2 >= 0 && x2 < img.getWidth() && y2 >= 0 && y2 < img.getHeight() && notEdge) {
                            int rgb = img.getRGB(x2, y2);
                            Color color = new Color(rgb);
                            brightnesses[counter] = 0.299 * color.getRed() + 0.587 * color.getGreen()+ 0.114 * color.getBlue();
                        }
                        else {
                            brightnesses[counter] = 0; 
                        }
                    }

                    energy += Math.sqrt(Math.pow(brightnesses[4] + 2 * brightnesses[0] + brightnesses[7]
                            - brightnesses[5] - 2 * brightnesses[3] - brightnesses[6], 2)
                            + Math.pow(brightnesses[4] + 2 * brightnesses[1] + brightnesses[5]
                                    - brightnesses[7] - 2 * brightnesses[2] - brightnesses[6], 2));
                }

                if (energy < min) {
                    min = (int) energy;
                    minVertical = x;
                }
            }


            newImage = new BufferedImage(img.getWidth() - 1, img.getHeight(), BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < img.getHeight(); y++) {
                int xNewImage = 0;
                for (int x = 0; x < img.getWidth(); x++) {
                    if (x == minVertical)
                        continue; 
                    newImage.setRGB(xNewImage, y, img.getRGB(x, y));
                    xNewImage++;
                }
            }

            img = newImage; 
        }

        ImageIO.write(img, "jpg", new File("resizedImage.jpg"));
    }
}
