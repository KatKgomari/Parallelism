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

public class MeanFilterSerial{

    public static void main(String[] args){
       
        // Remember that you have the args array that has the information passed on the command line.
        
        int width; 
        int height;        
        BufferedImage image = null;
        File file = null;   
        String inputFile = args[0];
        //String outputFile = args[1];    // I need to make an image from the results i get 
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
        
        // Nested for loop to access the diffferent pixels 
        for (int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                Color color = new Color(image.getRGB(i,j));
                System.out.println("blue: "+ color.getBlue() + "red: "+ color.getRed() + "green: "+ color.getGreen());            
            }
        }
                 
       // Color color = new Color(      Do this one in a for loop
        
        
         
        
    
    
    
    }
} 