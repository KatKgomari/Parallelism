// Median Filter Serial Program: Median filter for smoothing RGB color images
// Author: Katlego Kgomari 
// Date: 7 August 2022

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import javax.swing.*;   
import java.io.*;
import javax.imageio.*;
import java.util.Arrays;
 

public class MedianFilterSerial{
    
    // Variables and methods to keep track of runTime()
    private static long startTime;
    private static long runTime;
    
    private static void tic(){
        startTime = System.currentTimeMillis();
    }
    
    private static void toc(){
        runTime = (System.currentTimeMillis() - startTime);
    }
    
    
    public static void main(String[] args){
        int windowWidth = Integer.parseInt(args[2]);
        if((windowWidth < 3)||(windowWidth%2 == 0)){
            System.out.println("The width of the window must be an odd number greater than or equal to 3.");
            System.exit(0);
        } 
        else{
            // Getting the number of processors
            int noThreads = Runtime.getRuntime().availableProcessors();
            BufferedImage image = null;
            File file = null;   
            String inputFile = args[0];
            String outputFile = args[1];     
            int deviation = (windowWidth-1) /2;        
        
            // Opening our image in a try/catch block 
            try{
                file = new File(inputFile);
                image = ImageIO.read(file); 
            } 
            catch(IOException e){
                e.printStackTrace();
            } 
        
            int width = image.getWidth();
            int height = image.getHeight();
        
        
            File file2 = null;
            BufferedImage outputImage = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB); 
        
            //try-catch block for our new image
            try{
                file2 = new File(outputFile);  // Creating file with the name specifies by the user
            }
            catch(Exception e){
                e.printStackTrace();
            }
        
        	
            int dimensions = windowWidth * windowWidth; 
            tic(); 
            // Nested for loop to access the each pixel
            for (int i = 0; i < width; i++){
                for(int j = 0; j < height; j++){
                    int[] redList = new int[dimensions];
                    int[] greenList = new int[dimensions];
                    int[] blueList = new int[dimensions]; 
                        
                    int count = 0;
                    int upperM = i + deviation;
                    int upperN = j + deviation;
                        
                    for(int m = i - deviation; m <= upperM; m++){    
                        for(int n = j - deviation; n <= upperN; n++){   // We are still at the same j and i point.
                            try{ 
                               Color color = new Color(image.getRGB(m,n));
                               redList[count] = color.getRed();
                               blueList[count] = color.getBlue();
                               greenList[count] = color.getGreen();                    
                               count++;
                            }   
                            catch(Exception e){
                                continue;
                            } 
                        }
                    } 
                //Before we go to the next pixel, let us get the midian of each array
            
                Arrays.sort(redList);
                Arrays.sort(greenList);
                Arrays.sort(blueList);
                
                int medianRed = redList[count/ 2];
                int medianGreen = greenList[count/2];
                int medianBlue = blueList[count/2];
                          
                Color color2 = new Color(medianRed, medianGreen, medianBlue);
                outputImage.setRGB(i,j, color2.getRGB());               
                }
                
            }
            toc();
            try{
            ImageIO.write(outputImage, "png", file2); // Putting the image into a file.         
            }
            catch(IOException e){
                e.printStackTrace();
            }
            System.out.println("Runtime was " +runTime/1000.0f + " seconds on " +noThreads+ " processors. Image dimensions: " +width+ " x " +height+ ". Filter WindowWidth: " +windowWidth+ ".");
            System.out.println("Done!");    
        }
    }
} 