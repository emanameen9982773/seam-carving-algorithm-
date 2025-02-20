import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;

import javax.imageio.ImageIO;

public class ResizeImages {

      
    public static void main(String [] args) throws Exception{
          
        String name= SeamCarving1.SelectedImage();
        BufferedImage newImage;
        BufferedImage image= ImageIO.read(new File("images\\"+name+".jpg"));
        int numOfRemovedSeam=0;

        while (numOfRemovedSeam<130) {
        SeamCarving1.min=Integer.MAX_VALUE;
        numOfRemovedSeam++;
        int energy[][]=SeamCarving1.calculatepixelsEnergy(image);
        
        for(int x=0; x<image.getWidth(); x++){
            System.out.println(x);
            SeamCarving1.findMinSeam(energy, 0, x, image.getHeight()-1, new LinkedList<Integer>());}
       
        newImage= SeamCarving1.removeSeam(image);
        image= newImage;
        }

        ImageIO.write(image, "jpg", new File("resized Image.jpg"));
        SeamCarving1.display(name);
 
    }

}

