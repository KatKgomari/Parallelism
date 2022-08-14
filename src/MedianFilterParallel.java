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
import java.util.Arrays; 

public class MedianFilterParallel extends RecursiveAction{   
        
        // Instance Variables
        
        // I access all of these in my main method
        private int startIndex;
        private int stopIndex;
        
        
         
        private static int windowWidth;
        private static int deviation;
        private static int dimensions; // of the window 
        
       // private static int width;
       private static int height;
       
        // I need the threads to all work with the same image
        private static BufferedImage image;
        private static File file;
        private static File file2;
        private static BufferedImage outputImage;
                             
        
        public MedianFilterParallel(int startIndex, int stopIndex){
            this.startIndex = startIndex;    // We want to split these in half.
            this.stopIndex = stopIndex;
        }  
        
        
        
        // I need every thread to be able to do the below so I think this is wheremy comouter will be.
        // Is compute the method that we call by saying "invoke"
        	
        protected void computeSequentially(){    // I think that this methode needs to have its own complicated parameters
        // Maybe a start and a stop. We know we will be dividing distances. In twos to be specific.                 
        // Nested for loop to access the diffferent pixels (Aim: to copy these pixels onto the outputImage)
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
                               //System.out.println("count: " + count +" red: " + redList[count] + " green: " + greenList[count] + " blue: " + blueList[count]);                    
                               count++;
                               }
                               
                               
                       catch(Exception e){
                          continue;
                      } 
                    }
                    
                } 
                //Before we go to the next pixel, let us get the median of the array
                Arrays.sort(redList);
                Arrays.sort(greenList);
                Arrays.sort(blueList);
                

                int medianRed = redList[count/2];
                int medianGreen = greenList[count/2];
                int medianBlue = blueList[count/2];
                          
                Color color2 = new Color(medianRed, medianGreen, medianBlue);
                outputImage.setRGB(i,j, color2.getRGB());               
                }
                
            }
        
    }
    
    protected void compute(){
        int diff = stopIndex - startIndex;
        if(diff < 550 ){
            computeSequentially();
        }
        
        else{
            MedianFilterParallel right = new MedianFilterParallel(startIndex, stopIndex/2);
            MedianFilterParallel left = new MedianFilterParallel((stopIndex/2), stopIndex);
            left.fork();
            right.compute();
            left.join();
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
        // Is this where I would initilaize the fork-join pool?
        windowWidth = Integer.parseInt(args[2]); 
        if(windowWidth < 3){
            System.out.println("WindowWidth must greater than or equal to 3.");
            System.exit(0);
        }
        else{
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
            
            int width = image.getWidth();
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
            
            MedianFilterParallel mfp = new MedianFilterParallel(0, width);
            ForkJoinPool pool = new ForkJoinPool();
            pool.invoke(mfp);
            System.out.println("Done!");
            
        }  
    }
    
} 