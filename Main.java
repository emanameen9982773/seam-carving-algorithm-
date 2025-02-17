import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;


public class Main {

    public static void main(String[] args) throws Exception {
        File file = new File("images\\grave.jpg");
        BufferedImage img = ImageIO.read(file);


        int i = 0, counter = 1, x2=0, y2=0;
        int min = Integer.MAX_VALUE, minVertical=-1;
        
        while (i <= 30) { 
            i++;

            for (int x = 0; x < img.getWidth(); x++) {
                
                double energy=0;
                for (int y = 0; y < img.getHeight(); y++) {

                    
                    

                    {
                        double[] brightnesses = new double[8];
                        for (counter = 0; counter < 8; counter++) {
                            boolean edge = false;
                        
                            // LEFT (D)
                            if (counter == 0 && x != 0) {
                                x2 = x - 1;
                                y2 = y;
                                edge = true;
                            }
                            // ABOVE (B)
                            if (counter == 1 && y != 0) {
                                x2 = x;
                                y2 = y + 1;
                                edge = true;
                            }
                            // BENEATH (H)
                            if (counter == 2 && y != img.getHeight() - 1) {
                                x2 = x;
                                y2 = y - 1;
                                edge = true;
                            }
                            // RIGHT (F)
                            if (counter == 3 && x != img.getWidth() - 1) {
                                x2 = x + 1;  // Corrected direction for right
                                y2 = y;
                                edge = true;
                            }
                            // LEFT upper CORNER (A)
                            if (counter == 4 && x != 0 && y != 0) {
                                x2 = x - 1;
                                y2 = y + 1;
                                edge = true;
                            }
                            // RIGHT upper CORNER (C)
                            if (counter == 5 && x != img.getWidth() - 1 && y != 0) {
                                x2 = x + 1;
                                y2 = y + 1;
                                edge = true;
                            }
                            // RIGHT LOWER CORNER (I)
                            if (counter == 6 && x != img.getWidth() - 1 && y != img.getHeight() - 1) {
                                x2 = x + 1;
                                y2 = y - 1;
                                edge = true;
                            }
                            // LEFT LOWER CORNER (G)
                            if (counter == 7 && x != 0 && y != img.getHeight() - 1) {
                                x2 = x - 1;
                                y2 = y - 1;
                                edge = true;
                            }
                        
                            // Ensure that the neighbor indices (x2, y2) are within the bounds of the image
                            if (x2 >= 0 && x2 < img.getWidth() && y2 >= 0 && y2 < img.getHeight()) {
                                if (edge) {
                                    int rgb = img.getRGB(x2, y2);
                                    Color color = new Color(rgb);
                                    int red = color.getRed();
                                    int green = color.getGreen();
                                    int blue = color.getBlue();
                                    brightnesses[counter] = 0.299 * red + 0.587 * green + 0.114 * blue;
                                }
                            } else {
                                brightnesses[counter] = 0; // Assign a default value when out-of-bounds
                            }
                        }

                        energy += Math.sqrt(Math.pow(brightnesses[4] + 2 * brightnesses[0] + brightnesses[7] - brightnesses[5] - 2 * brightnesses[3] - brightnesses[6], 2) +
                        Math.pow(brightnesses[4] + 2 * brightnesses[1] + brightnesses[5] - brightnesses[7] - 2 * brightnesses[2] - brightnesses[6], 2));

                }

                if(energy<min) minVertical=x;

            }

         
         }
         BufferedImage newImage = new BufferedImage(img.getWidth()-1, img.getHeight(), BufferedImage.TYPE_INT_RGB);
         for (int y = 0; y < img.getHeight(); y++) {
            int xNewImage = 0;  // Reset for every row
            for (int x = 0; x < img.getWidth(); x++) {
                if (x == minVertical) continue; // Skip the seam column
                newImage.setRGB(xNewImage, y, img.getRGB(x, y));
                xNewImage++; // Move to the next column in new image
             }
         }
          ImageIO.write(newImage, "jpg", new File("resizedImage.jpg"));

          img= newImage;
        }
   
   
    
    
    
   // System.out.println(img.getWidth()+"\t"+newImage.getWidth());

}
}
    