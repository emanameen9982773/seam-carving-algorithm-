import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

//import javafx.scene.paint.Color;
import java.awt.Color;


public class DynamicProgramming {

    public static void main(String[] args) throws Exception {
        String name = SeamCarving2.SelectedImage();
        BufferedImage image = ImageIO.read(new File("images\\" + name + ".jpg"));
        BufferedImage resizedImage = resizeImage(image, 95);
        ImageIO.write(resizedImage, "jpg", new File("resized Image.jpg"));
        SeamCarving2.display(name);
    }


    public static BufferedImage resizeImage(BufferedImage image, int numOfSeams) {
        for (int i = 0; i < numOfSeams; i++) {
            int [][] energy=SeamCarving2.calculatepixelsEnergy(image);
            image = SeamCarving2.removeSeam(image, findSeam(energy));
        }
        return image;
    }


    public static int[] findSeam(int[][] energy) {
        int height = energy.length;
        int width = energy[0].length;
        double[][] dp = new double[height][width];
        int[][] path = new int[height][width];
        double mine;
        int minc;

        // Copy first row
        for (int col = 0; col < width; col++) {
            dp[0][col] = energy[0][col];
        }

        // Fill pd
        for (int row = 1; row<height; row++) {
            for (int col = 0; col < width; col++) {
                double left = (col > 0) ? dp[row - 1][col - 1] : Integer.MAX_VALUE;
                double down = dp[row - 1][col];
                double right = (col < width - 1) ? dp[row - 1][col + 1] : Integer.MAX_VALUE;

                mine=down; minc=col; 
                if(left<mine){
                    mine=left;
                    minc=col-1;
                }
                if(right<mine){
                    mine=right;
                    minc=col+1;
                }
                dp[row][col] = energy[row][col] + mine;
                path[row][col]=minc;
            }
        }

        int minIndex = 0;
        for (int x = 1; x < width; x++) {
            if (dp[height - 1][x] < dp[height - 1][minIndex]) {
                minIndex = x;
            }
        }

        int[] seam =new int[height];
        seam[height-1]=minIndex;

        for (int y = height - 2; y >= 0; y--) {
            seam[y] = path[y +1][seam[y +1]];
        }

        return seam;
    }
 

    static public int[][] computeEnergy(BufferedImage image) {
            int width = image.getWidth();
            int height = image.getHeight();
            int[][] energy = new int[height][width];
    
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
    
                    energy[y][x] = (int)Math.sqrt(dx + dy);
                }
            }
            return energy;
        }
}
