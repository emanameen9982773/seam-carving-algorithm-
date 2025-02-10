import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import javafx.scene.image.*;

public class Main {

    public static void main(String [] args)throws Exception{
                    BufferedImage img = ImageIO.read(new File("images\\cartoon.jpg"));

                   int i=0, counter=1, x2,y2;
                   double brightness=0, energy;
                   while ( i<=100) {

                    for(int x=0 ; x <img.getWidth(); x++){
                        for(int y=0 ; y <img.getHeight(); y++){
                            
                          if(x==0){}

                        else{

                            while (counter<=8) {
                                if (counter==1) {x2=x-1; y2=y;} // LEFT
                                if (counter==2) {x2=x; y2=y+1;} // ABOVE
                                if (counter==3) {x2=x; y2=y-1;} // BENEATH
                                if (counter==4) {x2=x-1; y2=y;} // RIGHT
                                if (counter==5) {x2=x-1; y2=y+1;} //RIGHT CORNER
                                if (counter==6) {x2=x+1; y2=y+1;}  //LEFT CORNER
                                if (counter==7) {x2=x+1; y2=y-1;}  // RIGHT LOWER CORNER
                                if (counter==8) {x2=x-1; y2=y-1;} // LEFT LOWER CORNER
                             
                                
                               
                            
                            int rgb = img.getRGB(x, y);
                            Color color = new Color(rgb);

                            int red = color.getRed();
                            int green = color.getGreen();
                            int blue = color.getBlue();

                             brightness =+ 0.299 * red + 0.587 * green + 0.114 * blue;}}
                          


                    }
                        
                    }

        System.out.println("hi");
    }
}
}