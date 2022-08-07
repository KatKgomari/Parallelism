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

    public static void main(String[] args){
        // Remember that you have the args array that has the information passed on the command line.
        int width = 500;   // Am I actually supossed to be the one providing these dimsnsions? 
        int height = 500;        
        BufferedImage image = null;
        File file = null;   // I don't know if I will actually need this.
        String inputFile = args[0];
        // Opening our image in a try/catch block 
        try{
            file = new File(inputFile);
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);   // Creating an object of the BufferedImage
            image = ImageIO.read(file); // Now that we have all the details, we are now filling the image object with the file.
        } 
        catch(IOException e){
            e.printStackTrace();
        } 
        
        
         
        
    
    
    
    }
} 