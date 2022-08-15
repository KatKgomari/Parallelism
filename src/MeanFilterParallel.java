// Mean Filter Parallel Program: Mean filter for smoothing RGB color images
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
        private int startIndex;
        private int stopIndex;
        
        
        // Other Variables 
        private static int windowWidth;
        private static int deviation;
        private static int dimensions; // of the window 
        private static int height;
        private static int width;
       
        // The threads need to all work with the same input and output image
        private static BufferedImage image;
        private static File file;
        private static File file2;
        private static BufferedImage outputImage;
        
        // Variables that will be used when timing the execution
        private static long startTime = 0;
        private static long runTime = 0;
        
        // Methods used to time the execution (got methods from Michelle Kuttel, CSC2002S course convener)
        private static void tic(){
            startTime = System.currentTimeMillis();
        }
        private static void toc(){
            runTime = (System.currentTimeMillis() - startTime);
        }
                             
        
        public MeanFilterParallel(int startIndex, int stopIndex){
            this.startIndex = startIndex;    // We want to split these in half.
            this.stopIndex = stopIndex;
        }  
        
    
        // The method that will be invoked when the base condtion is satisfied in compute()	
        protected void computeSequentially(){    
            for (int i = startIndex; i < stopIndex; i++){
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
                               count++;
                               }
                               catch(Exception e){
                                   continue;
                                   } 
                            }
                    } 
                //Before we go to the next pixel, let us get the average of each array
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
    }
    
        protected void compute(){
            int diff = stopIndex - startIndex;
            if(diff <= (width/2)){
                computeSequentially();
                }
        
            else{
                MeanFilterParallel right = new MeanFilterParallel(startIndex, stopIndex/2);
                MeanFilterParallel left = new MeanFilterParallel((stopIndex/2), stopIndex);
                left.fork();
                right.compute();
                left.join();
                toc();
                try{
                    ImageIO.write(outputImage, "png", file2);
                }
                catch(IOException e){
                    System.out.println("Image could not be produced.");
                    System.exit(0);
                }
            }
        }
    
    
    public static void main(String[] args){
        
        windowWidth = Integer.parseInt(args[2]); 
        if((windowWidth < 3) || (windowWidth%2 == 0)){
            System.out.println("The width of the window must be an odd number greater than or equal to 3.");
            System.exit(0);
        }
        else{
            // Getting the number of processors 
            int noThreads = Runtime.getRuntime().availableProcessors();
            
            // Reading in the image we want to apply the filter to
            image = null;
            file = null;
            try{
                file = new File(args[0]);
                image = ImageIO.read(file);
            } 
            catch(IOException e){
                e.printStackTrace();
                System.exit(0);
            }
            
            width = image.getWidth();
            height = image.getHeight();
            
            // Creating the BufferedImage object that will our output
            file2 = null;
            outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            try{
                file2 = new File(args[1]);
            }
            catch(Exception e){
                e.printStackTrace();
                System.exit(0);
            }
            dimensions = windowWidth * windowWidth;
            deviation = (windowWidth - 1)/2;
            
            MeanFilterParallel mfp = new MeanFilterParallel(0, width);
            ForkJoinPool pool = new ForkJoinPool();
            tic();
            pool.invoke(mfp);
            System.out.println("Runtime was " + runTime/1000.0f + " seconds on " + noThreads + ". Had WindowWidth of " + windowWidth + ".");
            System.out.println("Done!");
            
        }  
    }
} 