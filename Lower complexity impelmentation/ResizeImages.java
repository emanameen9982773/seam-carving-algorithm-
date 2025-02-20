import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
public class ResizeImages {

      
    public static void main(String [] args) throws Exception{
          
        String name= SeamCarving.SelectedImage();    //get name of image to resize 
        BufferedImage newImage;
        BufferedImage image= ImageIO.read(new File("images\\"+name+".jpg"));   // read the image from the file
        int numOfRemovedSeam=0; 
        if(name=="cartoon") numOfRemovedSeam=30;
        while (numOfRemovedSeam<120) {   
        SeamCarving.min=Integer.MAX_VALUE;   //reset the value of min 
        numOfRemovedSeam++;
        int energy[][]=SeamCarving.calculatepixelsEnergy(image);  //see discreption of this method in class SeamCarving
        
        for(int x=0; x<image.getWidth(); x++){
            SeamCarving.findMinSeam(energy, 0, x, image.getHeight()-1);}
       
        newImage= SeamCarving.removeSeam(image);
        image= newImage;
        }

        ImageIO.write(image, "jpg", new File("resized Image.jpg"));
        SeamCarving.display(name);
 
    }

}

