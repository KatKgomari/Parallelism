// Median Filter Parallel Program: Median filter for smoothing RGB color images
// Author: Katlego Kgomari 
// Date: 7 August 2022

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import javax.swing.*;   
import java.io.*;
import javax.imageio.*;
import java.util.concurrent.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.Arrays; 

public class MedianFilterParallel extends RecursiveAction{   
        
        // Instance Variables
        private int startIndex;
        private int stopIndex;
        private int split;
    
        // Other Variables 
        private static int windowWidth;
        private static int deviation;
        private static int dimensions; // of the window 
        private static int height;
        private static int width;
        
        // I need the threads to all work with the same image(s)
        private static BufferedImage image;
        private static File file;
        private static File file2;
        private static BufferedImage outputImage;
        private static final int CUT_OFF = 75;
        
        // Variables and methods to keep tarck of running time(got these from Michelle Kuttel, CSC2002S course convener)
        private static long startTime;
        private static long runTime;
        
        static final ForkJoinPool pool = new ForkJoinPool();
        
        private static void tic(){
            startTime = System.currentTimeMillis();
        }              
        
        private static void toc(){
            runTime = (System.currentTimeMillis() - startTime);
        }
        
        
        public MedianFilterParallel(int startIndex, int stopIndex){
            this.startIndex = startIndex;    // We want to split these in half.
            this.stopIndex = stopIndex;
            split = (startIndex+stopIndex)/2;
        }  
        
        
        // Method that will be invoked once base case in compute is satisied.	
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
                //Before we go to the next pixel, let us get the median of each array
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
            if(diff <= CUT_OFF){
                computeSequentially();
            }
        
            else{
                MedianFilterParallel left = new MedianFilterParallel(startIndex, split);
                MedianFilterParallel right = new MedianFilterParallel(split, stopIndex);
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
        if(windowWidth < 3){
            System.out.println("WindowWidth must greater than or equal to 3.");
            System.exit(0);
        }
        else{
            // Getting the number of processors that are available
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
            
            MedianFilterParallel mfp = new MedianFilterParallel(0, width);
            tic();
            pool.invoke(mfp);
            System.out.println("Runtime was " + runTime/1000.0f + " seconds on " + noThreads + " processors. Image dimensions: " +width+ " x " +height+". Filter WindowWidth: " + windowWidth + ".");
            System.out.println("Done!");
            
        }  
    }    
} 