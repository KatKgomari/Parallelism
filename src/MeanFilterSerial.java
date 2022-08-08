// Mean Filter Serial Program: Mean filter for smoothing RGB color images
// My sliding window 
// Author: Katlego Kgomari 
// Date: 7 August 2022

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import javax.swing.*;   //Will I need a JFrame?
import java.io.*;
import javax.imageio.*;

public class MeanFilterSerial{

    public static void main(String[] args){
       
        // Remember that you have the args array that has the information passed on the command line.
        
        int width; 
        int height;        
        BufferedImage image = null;
        File file = null;   
        String inputFile = args[0];
        String outputFile = args[1];    // I need to make an image from the results i get 
        //int windowWidth = Integer.parseInt(args[2]);  // How big my square will be
        
        
        // Opening our image in a try/catch block 
        try{
            file = new File(inputFile);
            //image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);   // Creating an object of the BufferedImage
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
            file2 = new File(outputFile);  // Creating filewith the name specifies by the user
        
        
        // Nested for loop to access the diffferent pixels (Aim: to copy these pixels onto the outputImage)
            for (int i = 0; i < width; i++){
                for(int j = 0; j < height; j++){
                    Color color = new Color(image.getRGB(i,j));
                    //System.out.println("blue: "+ color.getBlue() + "red: "+ color.getRed() + "green: "+ color.getGreen());
                    int red = color.getRed();
                    int green = color.getGreen();
                    int blue = color.getBlue();
                    Color color2 = new Color(red,green,blue);
                    outputImage.setRGB(i, j, color2.getRGB());    // But I need to be setting at specific points.            
            }
        }
            ImageIO.write(outputImage, "png", file2); // Putting the omage into a file.         
       
        }catch(IOException e){
            e.printStackTrace();
        }
        
         
        
    
    
    
    }
} 