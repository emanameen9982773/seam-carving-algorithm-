import java.io.File;
import javax.imageio.ImageIO;

public class ResizeImages {

      
    public static void main(String [] args) throws Exception{

        @SuppressWarnings("unused")
        int energy[][]=SeamCarving.calculatepixelsEnergy(ImageIO.read(new File("images\\"+SeamCarving.SelectedImage()+".jpg")));
 
    }

}

