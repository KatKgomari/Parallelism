// Mean Filter Serial Program: Mean filter for smoothing RGB color images
// My sliding window 
// Author: Katlego Kgomari 
// Date: 7 August 2022

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import javax.swing.*;   
import java.io.*;
import javax.imageio.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction; 

public class MeanFilterParallel extends RecursiveAction{   
        
        // Instance Variables
        private String inputFile;   
        private String outputFile;    
        private int windowWidth;
        private int deviation;
        private int dimensions; // of the window
        
        private int width;
        private int height;
        private BufferedImage image;
        private File file;
        private File2 file;
        private BufferedImage outputImage;
                             
        
        public MeanFilterParallel(String inputFile, String outputFile, int windowWidth){
            this.inputFile = inputFile;
            this.outputFile = outputFile;
            this.windowWidth = windowWidth;
            deviation = (windowWidth-1)/2; 
            dimensions = windowWidth * windowWidth;
        }
        
        public void getImage(){
        // Opening our image in a try/catch block 
            BufferedImage image = null;
            File file = null;
            
            try{
                file = new File(inputFile);
                image = ImageIO.read(file); // We are now filling the image object with the file.
                } 
            catch(IOException e){
                e.printStackTrace();
                System.exit(0);
                } 
        
                int width = image.getWidth();
                int height = image.getHeight();
            }
        
        
        
        public void createFilteredImage(){ 
            File file2 = null;
            BufferedImage outputImage = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB); // specifies width, height and colourset for our new image.
        
            //try-catch block for our new image
            try{
                file2 = new File(outputFile);  // Creating file with the name specifies by the user
            }
            catch(Exception e){
                e.printStackTrace();
                System.exit(0);
            }
        
        } 
        
        
        
        // I need every thread to be able to do the below so I think this is wheremy comouter will be.	
                 
        // Nested for loop to access the diffferent pixels (Aim: to copy these pixels onto the outputImage)
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
                       
                       
                       // We want the color specs, so lets use the color object.
                               Color color = new Color(image.getRGB(m,n));
                               redList[count] = color.getRed();
                               blueList[count] = color.getBlue();
                               greenList[count] = color.getGreen();
                               System.out.println("count: " + count +" red: " + redList[count] + " green: " + greenList[count] + " blue: " + blueList[count]);                    
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
            try{
            ImageIO.write(outputImage, "png", file2); // Putting the image into a file.         
            }
        catch(IOException e){
            e.printStackTrace();
        }
        
    
    
    
    public static void main(String[] args){
        // Is this where I would initilaize the fork-join pool?
        int windowWidth = Integer.parseInt(args[2]); 
        if(windowWidth < 3){
            System.out.println("WindowWidth must greater than or equal to 3.")
            System.exit(0);
        }
        else{   
            MeanFilterParallel mfp = new MeanFilterParallel(args[0]; args[1], windowWidth);
        }
        
        mfp.getImage();
        mfp.createFilteredImage();
          
    }
} 