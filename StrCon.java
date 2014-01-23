import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/*
This is the BitConverter class.
It is responsible for Image-String conversions/encryptions.

Summary of significant methods:
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
strEncode(String text, BufferedImage img)
 - converts/encrypts input string into image
 
strDecode(BufferedImage img)
 - converts/decyrpts image into string
*/

public class StrCon
{
    //  hides the input text, taken from a .txt file, in a given Image
    public static BufferedImage strEncode (File text, BufferedImage img) throws Exception
    {
	BufferedReader br = new BufferedReader (new FileReader (text));
	int[] bits = stringToBits (br.readLine ()); // calls the stringToBits function
	return bitsToImage (bits, img); // returns encoded image
    }


    public static String strDecode (BufferedImage img)
    {
	return imageToString(img); // returns encoded image
    }


    //converts string into an array of bits(1s and 0s)
    public static int[] stringToBits (String text)
    {
	int[] bits = new int [text.length () * 8];
	int val = 0, index = 0, charBits[] = new int [8];
	for (int i = 0, cBIndex = 0 ; i < text.length () * 8 ; i++, cBIndex++)
	{
	    if (i % 8 == 0)
	    {
		val = text.charAt (index); // gets ASCII value of the char
		cBIndex = 0;
		index++;
		charBits = convertToBits (val); //converts ASCII of the character into a bits array
	    }
	    bits [i] = charBits [cBIndex];
	}
	return bits;
    }


    //changes the bits array into a string
    public static String bitsToString (int[] bits)
    {
	String text = "";
	char character;
	int charBits[] = new int [8], x;
	for (int i = 0, cBIndex = 0 ; i < bits.length ; i++, cBIndex++)
	{
	    charBits [cBIndex] = bits [i];
	    if (cBIndex == 7)
	    {
		cBIndex = -1;
		x = convertToInt (charBits); // converts bits to integer
		character = (char) (x);
		text += character;
	    }
	}
	return text;
    }


    //converts the Image into an array of bits (1s and 0s) to be converted into a string
    public static String imageToString (BufferedImage SImage)
    {
	//creates all necessary variables to convert Image into bits
	int bits[] = new int [8], textLength = 0, counter = 0;
	boolean lengthFound = false;
	String text = "";
	int[] redBits = new int [8], greenBits = new int [8], blueBits = new int [8];
	int height = SImage.getHeight (), width = SImage.getWidth (), R, G, B, cl;
	
	//starts the decoding loop
	decodingLoop:
	for (int y = 0 ; y < height ; y++)
	{
	    //runs through the pixels of the subject image
	    for (int x = 0 ; x < width ; x++)
	    {
		//gets color and relative RGB values
		cl = SImage.getRGB (x, y);
		R = cl >> 16 & 0xff;
		G = cl >> 8 & 0xff;
		B = cl >> 0 & 0xff;
		// RGB values are converted to bit arrays
		redBits = convertToBits (R);
		greenBits = convertToBits (G);
		blueBits = convertToBits (B);
		// the last digits of the RGB bit arrays are taken and placed in the character bit array
		bits [0] = redBits [5];
		bits [1] = redBits [6];
		bits [2] = redBits [7];
		bits [3] = greenBits [5];
		bits [4] = greenBits [6];
		bits [5] = greenBits [7];
		bits [6] = blueBits [6];
		bits [7] = blueBits [7];
		if (!lengthFound)
		{
		    textLength += convertToInt (bits);
		    if (convertToInt (bits) < 255)
			lengthFound = true;
		}
		else
		{
		    counter++;
		    text += bitsToString (bits);
		}
		if (!(counter < textLength))
		    break decodingLoop;
	    }
	}
	return text;
    }


    //converts an array of bits into an Image
    public static BufferedImage bitsToImage (int[] bits, BufferedImage mainImage)
    {
	//creates all necessary variables to convert bits into Image
	int bitsIndex = 0;
	int redBits[] = new int [8], greenBits[] = new int [8], blueBits[] = new int [8];
	int width = mainImage.getWidth (), height = mainImage.getHeight (), R, G, B, cl, l = bits.length / 8, lBits[] = null;
	Color clr;
	encodingLoop:
	for (int y = 0 ; y < height ; y++)
	{
	    //runs through the Images, creating the encoded Image from the text and mainImage
	    for (int x = 0 ; x < width ; x++)
	    {
		//gets the color and RGB values of the current pixel
		cl = mainImage.getRGB (x, y);
		R = cl >> 16 & 0xff;
		G = cl >> 8 & 0xff;
		B = cl >> 0 & 0xff;
		//converts the RGB values into bits
		redBits = convertToBits (R);
		greenBits = convertToBits (G);
		blueBits = convertToBits (B);
		if (l > 0)
		{ //l is the length of the string, which is hidden in the first few pixels
		    lBits = convertToBits (l);
		    redBits [5] = lBits [0];
		    redBits [6] = lBits [1];
		    redBits [7] = lBits [2];
		    greenBits [5] = lBits [3];
		    greenBits [6] = lBits [4];
		    greenBits [7] = lBits [5];
		    blueBits [6] = lBits [6];
		    blueBits [7] = lBits [7];
		    l -= 255;
		}
		// hides the text in the image, as long as there is still some text remaining
		else if (bitsIndex < bits.length)
		    for (int i = 0 ; i < 8 ; i++, bitsIndex++)
			switch (bitsIndex % 8)
			{
			    case 0:
				redBits [5] = bits [bitsIndex];
			    case 1:
				redBits [6] = bits [bitsIndex];
			    case 2:
				redBits [7] = bits [bitsIndex];
			    case 3:
				greenBits [5] = bits [bitsIndex];
			    case 4:
				greenBits [6] = bits [bitsIndex];
			    case 5:
				greenBits [7] = bits [bitsIndex];
			    case 6:
				blueBits [6] = bits [bitsIndex];
			    case 7:
				blueBits [7] = bits [bitsIndex];
			    default:
				break;
			}
		// the RGB values are used to create the encoded image
		clr = new Color (convertToInt (redBits), convertToInt (greenBits), convertToInt (blueBits));
		mainImage.setRGB (x, y, clr.getRGB ());
		if (bitsIndex >= bits.length)
		    break encodingLoop;
	    }
	}
	return mainImage;
    }


    //converts an integer value into an array of bits
    private static int[] convertToBits (int x)
    {
	int[] bits = new int [8];
	for (int i = 7 ; i >= 0 ; i--)
	{
	    bits [i] = x % 2; // 1 or 0
	    x /= 2; // moves to the next place in the binary number
	}
	return bits;
    }


    //converts an array of bits into an integer
    private static int convertToInt (int[] bits)
    {
	int x = 0;
	for (int i = 0, j = 7 ; i < bits.length && j >= 0 ; j--, i++)
	    x += bits [j] * Math.pow (2, i);
	if (x > 255) //limits the possible return value to 255
	    x = 255;
	return x;
    }
}
