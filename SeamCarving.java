import java.awt.Color;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class SeamCarving {

   static int min=Integer.MAX_VALUE;
   static LinkedList<Integer> pathOfMinSeam ;

   
   public static String SelectedImage (){
    Scanner input = new Scanner(System.in);
    System.out.print("Enter the ID of the Image you want to resize it:\n1-Cartoon\n2-Carving\n3-Center\n4-Dancers\n5-Fenster\n6-Grave\n7-Museum\n8-Square\n9-Tower\n10-Casal\n---------------------\n the ID: ");
    int idOfSelectedImage = input.nextInt();
    String name=null;
    switch (idOfSelectedImage) {
     case 1: name="cartoon";
     break;
     case 2: name= "carving";
     break;
     case 3: name= "center";
     break;
     case 4: name= "dancers";
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
     case 10: name="casal";
     break;
     default: System.out.println("invalid input :(");
    }

   input.close();
   return name;

}


   public static void display(String name){
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setBounds(170,30,1000,600);

    ImageIcon imageBefor = new ImageIcon("images\\"+name+".jpg");
    ImageIcon imageAfter = new ImageIcon("resized Image.jpg");

    JLabel label1 = new JLabel(imageBefor);
    JLabel label2 = new JLabel(imageAfter);

   JLabel text1 = new JLabel("Original Image", SwingConstants.CENTER);
        JLabel text2 = new JLabel("Resized Image", SwingConstants.CENTER);

        // Main panel with GridLayout (2 rows, 2 columns)
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 2, 3, 10)); // 2 rows, 2 columns, with gaps

        // Add components to the grid
        mainPanel.add(label1);
        mainPanel.add(label2);
        mainPanel.add(text1);
        mainPanel.add(text2);

        frame.add(mainPanel);
    frame.setVisible(true);

} 


   public static int[][] calculatepixelsEnergy(BufferedImage image){

    int x2=0,y2=0,counter;
    int pixelsEnergy[][] = new int[image.getHeight()][image.getWidth()];

    for (int x = 0; x < image.getWidth(); x++) {
        for (int y = 0; y < image.getHeight(); y++) {

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
                if (counter == 2 && y != image.getHeight() - 1) { //BENEATH
                    x2 = x;
                    y2 = y + 1;
                    notEdge = true;
                }
                if (counter == 3 && x != image.getWidth() - 1) { //RIGHT
                    x2 = x + 1;
                    y2 = y;
                    notEdge = true;
                }
                if (counter == 4 && x != 0 && y != 0) {  //RIGHT CORNER
                    x2 = x - 1;  
                    y2 = y - 1;
                    notEdge = true;
                }
                if (counter == 5 && x != image.getWidth() - 1 && y != 0) {  //LEFT CORNER
                    x2 = x + 1;
                    y2 = y - 1;
                    notEdge = true;
                }
                if (counter == 6 && x != image.getWidth() - 1 && y != image.getHeight() - 1) {  //RIGHT LOWER CORNER
                    x2 = x + 1;
                    y2 = y + 1;
                    notEdge = true;
                }
                if (counter == 7 && x != 0 && y != image.getHeight() - 1) {  //LEFT LOWER CORNER
                    x2 = x - 1;
                    y2 = y + 1;
                    notEdge = true;
                }

                if (x2 >= 0 && x2 < image.getWidth() && y2 >= 0 && y2 < image.getHeight() && notEdge) {   // to ensure we are not out of bound
                    int rgb = image.getRGB(x2, y2);
                    Color color = new Color(rgb);
                    brightnesses[counter] = 0.299 * color.getRed() + 0.587 * color.getGreen()+ 0.114 * color.getBlue();
                }
                else 
                    brightnesses[counter] = 0;   // set brightness to zero for the neighbor pixel of an edge pixels
                
            }

            pixelsEnergy [y][x]=(int) ((Math.sqrt(Math.pow(brightnesses[4] + 2 * brightnesses[0] + brightnesses[7]    // calculating pixelsEnergy
                         - brightnesses[5] - 2 * brightnesses[3] - brightnesses[6], 2)
                         + Math.pow(brightnesses[4] + 2 * brightnesses[1] + brightnesses[5]
                         - brightnesses[7] - 2 * brightnesses[2] - brightnesses[6], 2)))+0.5);
        }

    }
    return pixelsEnergy;
}


    public static void findMinSeam(int[][] pixelsEnergy, int sumOfEnergies, int column, int row, LinkedList<Integer> path){
        if(column<0 || column>=pixelsEnergy[0].length) return;
        path.add(column);
        sumOfEnergies+=pixelsEnergy[row][column];
        if(row==0){
            if(sumOfEnergies<min){
             min=sumOfEnergies;
             pathOfMinSeam=new LinkedList<>(path);
            }
            return;
        }
        findMinSeam(pixelsEnergy,sumOfEnergies,column,row-1,new LinkedList<>(path)) ;  
        //findMinSeam(pixelsEnergy,sumOfEnergies,column+1,row-1,new LinkedList<>(path)) ;      
        //findMinSeam(pixelsEnergy,sumOfEnergies,column-1,row-1,new LinkedList<>(path)) ;      
    
    }  


 public static BufferedImage removeSeam(BufferedImage image){
     
    BufferedImage newImage = new BufferedImage(image.getWidth() - 1, image.getHeight(), BufferedImage.TYPE_INT_RGB); // create new image to store the resized image
    for (int y = image.getHeight()-1; y>=0 ; y--) {
        int xNewImage = 0;
        for (int x = 0; x < image.getWidth(); x++) {
            if (x == pathOfMinSeam.getFirst())  continue;         // skip the minimum vertical seam energy
            newImage.setRGB(xNewImage, y, image.getRGB(x, y));   // pixels copying
            xNewImage++;
        }
        pathOfMinSeam.removeFirst();
    }

    return newImage;
  }
}
