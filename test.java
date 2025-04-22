

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.awt.Color;
public class test {
    
 
    
        private BufferedImage image;
    
        public test(BufferedImage image) {
            this.image = image;
        }
    
        // Calculate energy using gradient magnitude
        private double[][] computeEnergy() {
            int width = image.getWidth();
            int height = image.getHeight();
            double[][] energy = new double[height][width];
    
            for (int y = 1; y < height - 1; y++) {
                for (int x = 1; x < width - 1; x++) {
                    Color left = new Color(image.getRGB(x - 1, y));
                    Color right = new Color(image.getRGB(x + 1, y));
                    Color up = new Color(image.getRGB(x, y - 1));
                    Color down = new Color(image.getRGB(x, y + 1));
    
                    double dx = Math.pow(right.getRed() - left.getRed(), 2)
                              + Math.pow(right.getGreen() - left.getGreen(), 2)
                              + Math.pow(right.getBlue() - left.getBlue(), 2);
    
                    double dy = Math.pow(down.getRed() - up.getRed(), 2)
                              + Math.pow(down.getGreen() - up.getGreen(), 2)
                              + Math.pow(down.getBlue() - up.getBlue(), 2);
    
                    energy[y][x] = Math.sqrt(dx + dy);
                }
            }
            return energy;
        }
    
        // Find vertical seam using dynamic programming
        public int[] findVerticalSeam() {
            double[][] energy = computeEnergy();
            int height = energy.length;
            int width = energy[0].length;
            double[][] dp = new double[height][width];
            int[][] path = new int[height][width];
    
            // Initialize first row
            System.arraycopy(energy[0], 0, dp[0], 0, width);
    
            // Build DP table
            for (int y = 1; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    double minPrev = dp[y - 1][x];
                    path[y][x] = x;
    
                    if (x > 0 && dp[y - 1][x - 1] < minPrev) {
                        minPrev = dp[y - 1][x - 1];
                        path[y][x] = x - 1;
                    }
                    if (x < width - 1 && dp[y - 1][x + 1] < minPrev) {
                        minPrev = dp[y - 1][x + 1];
                        path[y][x] = x + 1;
                    }
    
                    dp[y][x] = energy[y][x] + minPrev;
                }
            }
    
            // Find minimum in bottom row
            double minEnergy = dp[height - 1][0];
            int minIndex = 0;
            for (int x = 1; x < width; x++) {
                if (dp[height - 1][x] < minEnergy) {
                    minEnergy = dp[height - 1][x];
                    minIndex = x;
                }
            }
    
            // Backtrack path
            int[] seam = new int[height];
            seam[height - 1] = minIndex;
            for (int y = height - 2; y >= 0; y--) {
                seam[y] = path[y + 1][seam[y + 1]];
            }
    
            return seam;
        }
    
        // Optional: remove seam from image
        public BufferedImage removeVerticalSeam(int[] seam) {
            int width = image.getWidth();
            int height = image.getHeight();
            BufferedImage newImage = new BufferedImage(width - 1, height, image.getType());
    
            for (int y = 0; y < height; y++) {
                int newX = 0;
                for (int x = 0; x < width; x++) {
                    if (x != seam[y]) {
                        newImage.setRGB(newX++, y, image.getRGB(x, y));
                    }
                }
            }
            return newImage;
        }


        public static void main(String[] args) throws IOException{
            BufferedImage image= ImageIO.read(new File("images\\grave.jpg"));

            for (int i = 0; i < 100; i++) {
                 test newim = new test(image);
                 image=newim.removeVerticalSeam(newim.findVerticalSeam());

        }
        ImageIO.write(image, "jpg", new File("resized Image.jpg"));
        SeamCarving2.display("grave");

    }}

    
    

