import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class ResizeImages {
    public static void main(String[] args) throws Exception {
       String name = SelectedImage();
       if(name==null) System.exit(0);
        File file = new File("images\\"+name+".jpg");   // origenal image
        BufferedImage img = ImageIO.read(file);
        BufferedImage newImage;

        int i = 0, counter, x2 = 0, y2 = 0;

        while (i <= 80) {  //remove 80 vertical
            i++;
            int min = Integer.MAX_VALUE;  // initialize and update the varible min to a very lage value 
            int minVertical = -1;         // update value of the x coordinate of the vertichel having the minimum energy

            for (int x = 0; x < img.getWidth(); x++) {
                double energy = 0;

                for (int y = 0; y < img.getHeight(); y++) {
                    double[] brightnesses = new double[8];    // array to store the brightnesses of neighboring pixels

                    for (counter = 0; counter < 8; counter++) {
                        boolean notEdge = false;

                        // Define neighboring pixels
                        if (counter == 0 && x != 0) {   //LEFT
                            x2 = x - 1;
                            y2 = y;
                            notEdge = true;
                        }
                        if (counter == 1 && y != 0) {  // ABOVE
                            x2 = x;
                            y2 = y - 1;
                            notEdge = true;
                        }
                        if (counter == 2 && y != img.getHeight() - 1) { //BENEATH
                            x2 = x;
                            y2 = y + 1;
                            notEdge = true;
                        }
                        if (counter == 3 && x != img.getWidth() - 1) { //RIGHT
                            x2 = x + 1;
                            y2 = y;
                            notEdge = true;
                        }
                        if (counter == 4 && x != 0 && y != 0) {  //RIGHT CORNER
                            x2 = x - 1;  
                            y2 = y - 1;
                            notEdge = true;
                        }
                        if (counter == 5 && x != img.getWidth() - 1 && y != 0) {  //LEFT CORNER
                            x2 = x + 1;
                            y2 = y - 1;
                            notEdge = true;
                        }
                        if (counter == 6 && x != img.getWidth() - 1 && y != img.getHeight() - 1) {  //RIGHT LOWER CORNER
                            x2 = x + 1;
                            y2 = y + 1;
                            notEdge = true;
                        }
                        if (counter == 7 && x != 0 && y != img.getHeight() - 1) {  //LEFT LOWER CORNER
                            x2 = x - 1;
                            y2 = y + 1;
                            notEdge = true;
                        }

                        if (x2 >= 0 && x2 < img.getWidth() && y2 >= 0 && y2 < img.getHeight() && notEdge) {   // to ensure we are not out of bound
                            int rgb = img.getRGB(x2, y2);
                            Color color = new Color(rgb);
                            brightnesses[counter] = 0.299 * color.getRed() + 0.587 * color.getGreen()+ 0.114 * color.getBlue();
                        }
                        else {
                            brightnesses[counter] = 0;   // set brightness to zero for the neighbor pixel of an edge pixels
                        }
                    }

                    energy += Math.sqrt(Math.pow(brightnesses[4] + 2 * brightnesses[0] + brightnesses[7]    // calculating energy
                            - brightnesses[5] - 2 * brightnesses[3] - brightnesses[6], 2)
                            + Math.pow(brightnesses[4] + 2 * brightnesses[1] + brightnesses[5]
                                    - brightnesses[7] - 2 * brightnesses[2] - brightnesses[6], 2));
                }

                if (energy < min) {   // to find minimum verticle seam energy
                    min = (int) energy; 
                    minVertical = x;  // store value of x coordinate of minimum verticle seam energy
                }
            }


            newImage = new BufferedImage(img.getWidth() - 1, img.getHeight(), BufferedImage.TYPE_INT_RGB); // create new image to store the resized image
            for (int y = 0; y < img.getHeight(); y++) {
                int xNewImage = 0;
                for (int x = 0; x < img.getWidth(); x++) {
                    if (x == minVertical)    // skip the minimum vertical seam energy
                        continue; 
                    newImage.setRGB(xNewImage, y, img.getRGB(x, y));   // pixels copying
                    xNewImage++;
                }
            }

            img = newImage; 
        }

        ImageIO.write(img, "jpg", new File("resizedImage.jpg"));   // write the image into a jpg file
        display(name);

    }


  public static String SelectedImage (){
  Scanner input = new Scanner(System.in);
  System.out.print("Enter the ID of the Image you want to resize it:\n1-Cartoon\n2-Carving\n3-Center\n4-Dancers\n5-Fenster\n6-Grave\n7-Museum\n8-Square\n9-Tower\n---------------------\n the ID: ");
  int idOfSelectedImage = input.nextInt();
  String name=null;
  switch (idOfSelectedImage) {
    case 1: name="cartoon";
    break;
    case 2: name= "carving";
    break;
    case 3: name= "center";
    break;
    case 4: name= "dancer";
    break;
    case 5: name= "fenster";
    break;
    case 6: name= "grave";
    break;
    case 7: name= "museum";
    break;
    case 8: name= "square";
    break;
    case 9: name= "tower";
    break;
    default: System.out.println("invalid input :(");
  }

return name;

}

public static void display(String name){
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
    frame.setBounds(200,30,1000,900);

    ImageIcon imageBefor = new ImageIcon("images\\"+name+".jpg");
    ImageIcon imageAfter = new ImageIcon("resizedImage.jpg");

    JLabel label1 = new JLabel(imageBefor);
    JLabel label2 = new JLabel(imageAfter);

    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    panel.add(label1);
    panel.add(label2);

    frame.add(panel);

    frame.setVisible(true);



} 
}
