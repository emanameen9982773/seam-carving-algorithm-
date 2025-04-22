import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Greedy {
    
    public static void main(String[] args) {
        try {
            String name=SeamCarving2.SelectedImage();
            BufferedImage image = ImageIO.read(new File("images\\"+name+".jpg"));
            int newWidth = image.getWidth() - 95; // Reducing the width by 50 pixels as an example
            BufferedImage resizedImage = seamCarving(image, newWidth);
            ImageIO.write(resizedImage,"jpg", new File("resized Image.jpg"));
            SeamCarving2.display(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Reduces the width of the image using seam carving.
    // Keeps removing the lowest-energy seam until the image reaches the desired width.
    public static BufferedImage seamCarving(BufferedImage image, int newWidth) {
        while (image.getWidth() > newWidth) {
            int[][] energy = computeEnergy(image);
            int[] seam = findGreedySeam(energy);
            image = removeSeam(image, seam);
        }
        return image;
    }

    // Calculates the energy of each pixel in the image.
    // The energy is determined based on brightness differences with neighboring pixels.
    public static int[][] computeEnergy(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] energy = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int a = getBrightness(image, x - 1, y - 1);
                int b = getBrightness(image, x, y - 1);
                int c = getBrightness(image, x + 1, y - 1);
                int d = getBrightness(image, x - 1, y);
                int f = getBrightness(image, x + 1, y);
                int g = getBrightness(image, x - 1, y + 1);
                int h = getBrightness(image, x, y + 1);
                int i = getBrightness(image, x + 1, y + 1);

                int xEnergy = a + 2 * d + g - c - 2 * f - i;
                int yEnergy = a + 2 * b + c - g - 2 * h - i;
                energy[y][x] = (int) Math.sqrt(xEnergy * xEnergy + yEnergy * yEnergy);
            }
        }
        return energy;
    }

    // Retrieves the brightness value of a pixel.
    // If the pixel is outside the image boundaries, it returns 0.
    private static int getBrightness(BufferedImage image, int x, int y) {
        if (x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight()) {
            return 0;
        }
        int rgb = image.getRGB(x, y);
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        return (r + g + b) / 3;
    }

    // Finds the lowest-energy vertical seam using a greedy approach.
    // Starts from the middle of the image and moves downward, choosing the lowest-energy pixel in each row.
    private static int[] findGreedySeam(int[][] energy) {
        int height = energy.length;
        int width = energy[0].length;
        int[] seam = new int[height];
        
        int minEnergy = Integer.MAX_VALUE;
        int minX = width / 2;
        
        for (int x = width / 4; x < 3 * width / 4; x++) {
            if (energy[0][x] < minEnergy) {
                minEnergy = energy[0][x];
                minX = x;
            }
        }
        
        seam[0] = minX;
        
        for (int y = 1; y < height; y++) {
            int prevX = seam[y - 1];
            int bestX = prevX;
            int bestEnergy = energy[y][prevX];

            for (int dx = -1; dx <= 1; dx++) {
                int newX = prevX + dx;
                if (newX >= 0 && newX < width && energy[y][newX] < bestEnergy) {
                    bestEnergy = energy[y][newX];
                    bestX = newX;
                }
            }
            seam[y] = bestX;
        }
        return seam;
    }

    // Removes the specified seam from the image.
    // Creates a new image with one less column and shifts pixels accordingly.
    private static BufferedImage removeSeam(BufferedImage image, int[] seam) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage newImage = new BufferedImage(width - 1, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            int index = seam[y];
            for (int x = 0, newX = 0; x < width; x++) {
                if (x != index) {
                    newImage.setRGB(newX++, y, image.getRGB(x, y));
                }
            }
        }
        return newImage;
    }
}
