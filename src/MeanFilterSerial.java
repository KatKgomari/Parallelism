// Mean Filter Serial Program: Mean filter for smoothing RGB color images 
// Author: Katlego Kgomari 
// Date: 7 August 2022

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import javax.swing.*;   
import java.io.*;
import javax.imageio.*;
 

public class MeanFilterSerial{

    // Variables and methods used to keep track of execution time (got theses from Michelle Kuttel, CSC2002S course convener)
    
    private static long startTime = 0;
    private static long runTime = 0;
    
    private static void tic(){
        startTime = System.currentTimeMillis();
    }
    
    private static void toc(){
        runTime = (System.currentTimeMillis() - startTime);
    }

    public static void main(String[] args){
        int windowWidth = Integer.parseInt(args[2]);
        
        if((windowWidth < 3) || (windowWidth%2 == 0)){
            System.out.println("The width of the window must be an odd number greater than or equal to 3. ");
            System.exit(0); 
        }
        else{
        // Getting the number of processors
        int noThreads = Runtime.getRuntime().availableProcessors();
        int width; 
        int height;        
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
        
        width = image.getWidth();
        height = image.getHeight();
        
        
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
                        for(int n = j - deviation; n <= upperN; n++){   // So we are still at the same j and i point.
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
                
                //Before we go to the next pixel, let us get the average of the array
                    int meanRed = 0;
                    int meanGreen = 0;
                    int meanBlue = 0;
                
                    for(int k = 0; k < redList.length; k++){
                        meanRed += redList[k];
                        meanGreen += greenList[k];
                        meanBlue += blueList[k];
                    }
                    
                    meanRed = (int)meanRed/(count + 1);
                    meanGreen = (int)meanGreen/(count + 1);
                    meanBlue = (int)meanBlue/(count+1);
                          
                    Color color2 = new Color(meanRed, meanGreen, meanBlue);
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
        System.out.println("RunTime was " +runTime/1000.0f + " seconds on " +noThreads+ " processors. Had WindowWidth of " +windowWidth + ".");
        System.out.println("Done!");
        }
    }
} 