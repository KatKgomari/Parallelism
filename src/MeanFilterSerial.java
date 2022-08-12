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
//import java.util.ArrayIndexOutOfBounds; 

public class MeanFilterSerial{

    public static void main(String[] args){
        
        int width; 
        int height;        
        BufferedImage image = null;
        File file = null;   
        String inputFile = args[0];
        String outputFile = args[1];     
        int windowWidth = Integer.parseInt(args[2]);
        int deviation = (windowWidth-1) /2;        
        
        // Opening our image in a try/catch block 
        try{
            file = new File(inputFile);
            image = ImageIO.read(file); // We are now filling the image object with the file.
        } 
        catch(IOException e){
            e.printStackTrace();
        } 
        
        width = image.getWidth();
        height = image.getHeight();
        
        
        File file2 = null;
        BufferedImage outputImage = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB); // So we have specifies the width, height and colourset for our new image.
        
        //try-catch block for our new image
        try{
            file2 = new File(outputFile);  // Creating file with the name specifies by the user
        }
        catch(Exception e){
          e.printStackTrace();
        }
        
        	
        int dimensions = windowWidth * windowWidth; // What am I using this for again?
             
        // Nested for loop to access the diffferent pixels (Aim: to copy these pixels onto the outputImage)
            for (int i = 0; i < width; i++){
                for(int j = 0; j < height; j++){
                    int[] redList = new int[dimensions];
                    int[] greenList = new int[dimensions];
                    int[] blueList = new int[dimensions];
                   // if ((i >= deviation) && (i <= ((height-1)-deviation)) && (j >= deviation) && (j <= ((width-1)-deviation))){ 
                        
                        int count = 0;
                        int upperM = i + deviation;
                        int upperN = j + deviation;
                        
                        for(int m = i - deviation; m <= upperM; m++){    // Do i want to have a whole new variable?
                            for(int n = j - deviation; n <= upperN; n++){   // So we are still at the same j and i point.
                            try{    
                       // Color color2 = new Color(red,green,blue);
                       //if ( (i%4==0) && (j%2==0))
                       // outputImage.setRGB(i, j, color2.getRGB());    // But I need to be setting at specific points.
                       
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
            ImageIO.write(outputImage, "png", file2); // Putting the omage into a file.         
            }
        catch(IOException e){
            e.printStackTrace();
        }
        
         
        
    
    
    
    }
} 