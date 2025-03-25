import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class DynamicProgramming {

    static private int[][] energy;
    static private int[][] dp;
    static private LinkedList<Integer> minPath;


    private static void calculate_dp(BufferedImage image) {

        energy = SeamCarving2.calculatepixelsEnergy(image);
        dp = new int[image.getHeight()][image.getWidth()];

        // copying the last row
        for (int i = 0; i < image.getWidth(); i++)
            dp[image.getHeight() - 1][i] = energy[image.getHeight() - 1][i];

        for (int row = image.getHeight() - 2; row >= 0; row--) {
            for (int col = 0; col < image.getWidth(); col++) {
                int left, mid, right;

                if (col == 0)
                    left = Integer.MAX_VALUE;
                else
                    left = dp[row + 1][col - 1];

                if (col == image.getWidth() - 1)
                    right = Integer.MAX_VALUE;
                else
                    right = dp[row + 1][col + 1];

                mid = dp[row + 1][col];

                dp[row][col] = energy[row][col] + Math.min(Math.min(left, right), mid);

            }

        }


    }


    private static void findMinSeamPath() {
        minPath = new LinkedList<>();  // Initialize minPath for each seam path calculation
        
        int min = dp[0][0];
        int minCol = 0;
    
        // Find the column with the minimal energy in the first row
        for (int i = 1; i < dp[0].length; i++) {
            if (dp[0][i] < min) {
                minCol = i;
                min = dp[0][i];
            }
        }
    
        minPath.add(minCol);  // Add the starting column to the path
        for (int row = 1; row < dp.length; row++) {
            int mid = dp[row][minCol];
        
            int left = (minCol > 0) ? dp[row][minCol - 1] : Integer.MAX_VALUE;
            int right = (minCol < dp[0].length - 1) ? dp[row][minCol + 1] : Integer.MAX_VALUE;
        
            if (left < mid && left <= right) {
                min = left;
                minCol--;
            } else if (right < mid && right < left) {
                min = right;
                minCol++;
            } else {
                min = mid;
            }
        
            minPath.add(minCol);  // Add the current column to the path
        }
    
        // Ensure minPath has been populated
        if (minPath.isEmpty()) {
            System.out.println("Error: minPath is empty.");
        }
    }
    
    
    

    public static BufferedImage resizeImage(BufferedImage image, int numOfSeam){

        for (int i = 0; i < numOfSeam; i++) {
            calculate_dp(image);
            findMinSeamPath();
            
            // Ensure minPath is populated
            if (minPath.isEmpty()) {
                System.out.println("Error: minPath is empty before removing the seam.");
                break; // Exit if minPath is empty
            }
    
            image = SeamCarving2.removeSeam(image, minPath);
        }
    
        return image;
    }
    
}
