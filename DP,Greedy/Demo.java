import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Demo {
    
    public static void main (String []args)throws Exception{

        String name= SeamCarving2.SelectedImage();
        
        BufferedImage image= ImageIO.read(new File("images\\"+name+".jpg"));

        BufferedImage newImage=DynamicProgramming.resizeImage(image,95);

        ImageIO.write(newImage, "jpg", new File("resized Image.jpg"));
        SeamCarving2.display(name);
    }
}
