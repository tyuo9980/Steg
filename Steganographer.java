import java.awt.*;
import java.io.*;
import javax.imageio.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

// A multi-purpose program used for Steganography:
// Hiding information within other medias, such as
// Hiding an image file within another image file
// while retaining the original image file's properties
//
// This program is capable of :
//   - Hiding an image file within another image file
//   - Hiding text within an image file
//   - Decoding the hidden image from an image file
//   - Decoding the hidden text from an image file

public class Steganographer
{
    //console variables
    public static PCPC c;                                               // Creates a new PCPC console
    public static StrCon b;                                             // Creates a new String Converter Object
    public static Graphics g;                                           // Creates new Graphics //
    public static Graphics2D g2;                                        //        Objects       //
    public static int cW = 1024;                                        // Sets the width of the frame to 1024 pixels
    public static int cH = 640;                                         // Sets the height of the frame to 640 pixels

    //mouse variables
    public static int mX, mY;                                           //
    public static int x = -694, x2 = -694, y = 0;                       //
    public static int dragX, dragY;                                     //PRESET              Also contains the variables
    public static int posX = -208;                                      //CONSTANTS           that will be used for getting
    public static int mState, imgScreenState = 0, txtScreenState = 0;   //DO                  mouse X and Y coordinates
    public static int loadX = 660;                                      //NOT                 for mouse press and
    public static int qInt = 0;                                         //TOUCH               mouse drag functions
    public static double width, height, ratio;                          //
    public static boolean mDown, mPressed, overQ, imageInput;           //

    //image variables
    public static Image image, pg2img, pg2img2, imageError, help;       //
    public static Image pg2txt, pg2txt2, next, txt_input;               //ALL
    public static Image bg_img, pg2_bg, pg3_bg, img_input, prev;        //IMAGE
    public static Image bt_over, bt_down, hm_bg;                        //VARIABLES
    public static Image plus_grd, close_down, close_light;              //ARE
    public static Image st_down, st_light, FileImg;                     //INITIATED
    public static Image mini_down, mini_light;                          //HERE
    public static Image plus, plus_down, loadbar, loadlight;            //
    public static Image pg2mover, checkmark, questionmark;              //
    public static BufferedImage newresize, resizeImg, bimg;             //
    public static boolean ValidFile = true;                             // Valid File variable for valid extension checking

    //Other variables
    public static int pgNum = 1;                                        // To change page number
    public static int button;                                           // Button variable for second page button selection
    public static int fileType = 0;                                     // 1 for text, 2 for image, 0 for not valid. Filetype variable
    public static String filePath, f2p = "";                            // Variables used to store the file path for dropped files
    public static String textFormat = "txt";                            //
    public static String[] picFormat = {"jpg", "png", "jpeg", "bmp"};   // Valid file extensions

    //image encoding variables
    public static File base;
    public static File hidden;
    public static File encoded;
    public static File bas = new File ("base.png");
    public static File hid = new File ("input.png");

    public static void main (String args[]) throws Exception // The Main Method, runs the program
    {
	init ();                //Initiates all variables, frames, etc
	c = new PCPC ();        //Creates a new PCPC object
	g = c.getGraphics ();   //Gets graphics
	g2 = (Graphics2D) g;    //Graphics object
	start ();               //Main loop of program
    }


    /*
    The imgDecode function will decode an encrypted image. i.e. If an image is hidden
    within another image, this function will output the hidden image. The parameter for
    this function is a string "bas" that denotes the original image's filepath. Outputs
    an image file named "ImgDecoded.png" in the same folder.
    */
    public static void imgDecode (String bas) throws Exception
    {
	//This function decodes an image that has another image
	//hidden within it. Retuns and outputs the hidden image

	int w, h, w2, h2;
	int pixels[];
	int newpixels[];

	File encoded = new File (bas);
	File decoded = new File ("ImgDecoded.png");

	// reads in images and gets parameters
	BufferedImage img = ImageIO.read (encoded);
	int a, d, c, a2, d2, c2;
	w = img.getWidth ();
	h = img.getHeight ();
	pixels = new int [w * h];
	newpixels = new int [w * h];
	img.getRGB (0, 0, w, h, pixels, 0, w);

	for (int i = 0 ; i < w * h ; i++)
	{
	    // bitshifts to get rgb values
	    a = pixels [i] >> 16 & 0xff;
	    d = pixels [i] >> 8 & 0xff;
	    c = pixels [i] & 0xff;

	    // bitshifts to get hidden rgb values
	    a = a << 4;
	    d = d << 4;
	    c = c << 4;

	    a %= 256;
	    d %= 256;
	    c %= 256;

	    // creates decoded pixel
	    String SAHA = comp (intToBase (a, 2) + "" + intToBase (d, 2) + "" + intToBase (c, 2));
	    newpixels [i] = -baseToInt (SAHA, 2) - 1;
	}

	// creates decoded file
	img.setRGB (0, 0, w, h, newpixels, 0, w);
	ImageIO.write (img, "png", decoded);
    }


    /*
    The imgEncode function will encode an image within another image, i.e. hide it within the "base"
    image so that nobody can see the hidden image. The parameter for this function is two strings "bas"
    and "hid". "bas" denotes the file path for the base image in which the second image will be hidden in,
    and "hid" denotes the file path for the image that is to be hidden within the base image. Outputs an image
    "ImgEncoded.png" in the same folder.
    */
    public static void imgEncode (String bas, String hid) throws Exception
    {

	//This function hides an image within another image
	//Returns and outputs the new image with the second
	//image hidden within it.

	int w, h, w2, h2;
	int pixels[];
	int pixels2[];
	int newpixels[];
	int a, d, c, a2, d2, c2;

	base = new File (bas);
	hidden = new File (hid);
	encoded = new File ("ImgEncoded.png");

	// reads in images and gets parameters
	BufferedImage img = ImageIO.read (base);
	BufferedImage img2 = ImageIO.read (hidden);

	w = img.getWidth ();
	h = img.getHeight ();

	pixels = new int [w * h];
	pixels2 = new int [w * h];
	newpixels = new int [w * h];

	img.getRGB (0, 0, w, h, pixels, 0, w);
	img2.getRGB (0, 0, w, h, pixels2, 0, w);

	for (int i = 0 ; i < w * h ; i++)
	{
	    // bitshifts to get rgb values
	    a = pixels [i] >> 16 & 0xff;
	    d = pixels [i] >> 8 & 0xff;
	    c = pixels [i] & 0xff;
	    a2 = pixels2 [i] >> 16 & 0xff;
	    d2 = pixels2 [i] >> 8 & 0xff;
	    c2 = pixels2 [i] & 0xff;

	    // clears last 4 bits
	    a /= 16;
	    a *= 16;
	    d /= 16;
	    d *= 16;
	    c /= 16;
	    c *= 16;
	    a2 /= 16;
	    d2 /= 16;
	    c2 /= 16;

	    //replaces last 4 bits with hidden image
	    a += a2;
	    d += d2;
	    c += c2;

	    //creates encoded pixel
	    String SAHA = comp (intToBase (a, 2) + intToBase (d, 2) + intToBase (c, 2));
	    newpixels [i] = -baseToInt (SAHA, 2) - 1;
	}

	// creates encoded file
	img.setRGB (0, 0, w, h, newpixels, 0, w);
	ImageIO.write (img, "png", encoded);
    }


    //The init method initiates the program by loading all neccessary images that are needed by the GUI
    //to display the contents, as well as the button variable to 0.
    public static void init () throws Exception
    {
	// Gathering all image files//
	bg_img = ImageIO.read (new File ("img/layout/bg_img1.png"));
	bt_over = ImageIO.read (new File ("img/buttons/bt_over.png"));
	bt_down = ImageIO.read (new File ("img/buttons/bt_down.png"));
	hm_bg = ImageIO.read (new File ("img/layout/home_bg.png"));
	plus_grd = ImageIO.read (new File ("img/layout/plus_gradient.png"));
	close_down = ImageIO.read (new File ("img/buttons/close_down.png"));
	close_light = ImageIO.read (new File ("img/buttons/close_light.png"));
	mini_down = ImageIO.read (new File ("img/buttons/mini_down.png"));
	mini_light = ImageIO.read (new File ("img/buttons/mini_light.png"));
	pg2_bg = ImageIO.read (new File ("img/layout/pg2_bg.png"));
	pg3_bg = ImageIO.read (new File ("img/layout/pg3_bg.png"));
	pg2mover = ImageIO.read (new File ("img/buttons/pg2_mover.png"));
	checkmark = ImageIO.read (new File ("img/buttons/checkmark.png"));
	questionmark = ImageIO.read (new File ("img/buttons/questionmark.png"));
	loadlight = ImageIO.read (new File ("img/layout/load_light.png"));
	pg2img = ImageIO.read (new File ("img/buttons/Image_clean.png"));
	pg2img2 = ImageIO.read (new File ("img/buttons/Image_over.png"));
	imageError = ImageIO.read (new File ("img/layout/image_type_error.png"));
	pg2txt = ImageIO.read (new File ("img/buttons/Text_clean.png"));
	pg2txt2 = ImageIO.read (new File ("img/buttons/Text_over.png"));
	help = ImageIO.read (new File ("img/buttons/help.png"));
	next = ImageIO.read (new File ("img/buttons/next.png"));
	img_input = ImageIO.read (new File ("img/layout/img_input.png"));
	txt_input = ImageIO.read (new File ("img/layout/img_input.png"));
	prev = ImageIO.read (new File ("img/buttons/prev.png"));

	button = 0;

    }


    // Splits the file path to find out what extension the file has. Parameter is a string "fp"
    // denoting the file's file path. returns the file extension. e.g. If the original file's filepath
    // was C:\hello.txt this function will return the string "txt"
    public static String fileExtension (String fp)
    {
	if (fp.contains ("."))
	{
	    String path[] = fp.split ("\\.");
	    String ext2 = path [1];
	    return ext2;
	}
	else
	{
	    return "0";
	}
    }


    // Checks if the file extension is valid for use within the steganography program.
    // Accepted file extensions include: txt, png, jpg, jpeg, bmp.
    public static int Extension (String ext)
    {
	int fType = 0;
	if (ext.equals ("0"))
	{
	    return 0;
	}
	else
	{
	    if (ext.toUpperCase ().equals ("TXT"))
	    {
		fType = 1;
	    }
	    else
	    {
		for (int i = 0 ; i < 4 ; i++)
		{
		    if (ext.toUpperCase ().equals (picFormat [i].toUpperCase ()))
		    {
			fType = 2;
			break;
		    }

		}
	    }
	    return fType;
	}
    }


    // Loads loadbar animation
    public static void LoadAnimation ()
    {
	if (c.mCX > 457 && c.mCX < 457 + 114 && c.mCY > 13 && c.mCY < 13 + 46)
	{
	    loadX += 1;
	    if (loadX >= 947)
	    {
		loadX = 947;
	    }
	    g.drawImage (loadbar, 660, 16, loadX, 32, 0, 0, 286, 16, null);

	}
    }


    //~~~~~~~~~~~~~~~~~~~
    // Image to Image
    //~~~~~~~~~~~~~~~~~~~

    // method used to complement the bits from an imported string
    public static String comp (String s)
    {
	String ss = "";
	for (int i = 0 ; i < s.length () ; i++)
	{
	    // flips bits
	    char blah = s.charAt (i);
	    if (blah == '1')
	    {
		ss += "0";
	    }
	    else
		ss += "1";
	}

	return ss;
    }


    // changes the base of an inputted number in base 10 to a custom base
    public static String intToBase (int input, int base)
    {
	String hex = "", hex2 = "";
	long temp;
	while (input > 0)
	{
	    //converts and finds the corresponding converted character
	    temp = input % base;
	    input /= base;
	    if (temp > 9)
		hex = hex + (char) (temp + 55);
	    else
		hex = hex + temp;
	}

	while (hex.length () < 8)
	    hex += '0';

	// reverses the string
	for (int i = 7 ; i >= 0 ; i--)
	{
	    hex2 += hex.charAt (i);
	}

	return hex2;
    }


    // reverse of intToBase; converts the inputted number to base 10
    public static int baseToInt (String input, int base)
    {
	int ans = 0;
	for (int i = 0 ; i < input.length () ; i++)
	{
	    //finds corresponding values and converts to base 10
	    if ((int) input.charAt (i) > 57)
		ans += Math.pow (base, i) * ((int) input.charAt (input.length () - i - 1) - 55);
	    else
		ans += Math.pow (base, i) * ((int) input.charAt (input.length () - i - 1) - 48);
	}
	return ans;
    }


    //~~~~~~~~~~~~~~~~
    // Main frame
    //~~~~~~~~~~~~~~~~

    // draws the images of buttons and backgrounds
    public static void drawImage () throws Exception
    {

	g.drawImage (bg_img, 0, 0, null);
	g.drawImage (loadbar, 660, 16, loadX, 32, 0, 0, 1, 1, null);

	// buttons
	if (mX > 844 && mX < 1018 && mY > 610 && mY < 635) // w53 h25
	{
	    if (mX > 904 && mX < 957)
		g.drawImage (bt_over, 904, 610, null);
	    if (mX > 965)
		g.drawImage (bt_over, 965, 610, null);

	    if (mDown)
	    {
		if (mX > 904 && mX < 957)
		    g.drawImage (bt_down, 904, 610, null);
		if (mX > 965)
		    g.drawImage (bt_down, 965, 610, null);
	    }
	}

	// close button
	if (mX > 1003 && mX < 1023 && mY > 2 && mY < 22)
	{
	    g.drawImage (close_light, 1005, 3, null);
	    if (mDown)
		g.drawImage (close_down, 1005, 3, null);
	}

	//minimize button
	if (mX > 980 && mX < 1002 && mY > 5 && mY < 20)
	{
	    g.drawImage (mini_light, 979, 7, null);
	    if (mDown)
	    {
		g.drawImage (mini_down, 979, 7, null);
	    }
	}

	/***********************************/
	// changes background for when the page is 1
	if (pgNum == 1)
	{

	    //background

	    g.drawImage (plus, 862, 613, null); //drag plus
	    g.drawImage (hm_bg, 6, 67, null); // home

	    if (ValidFile == false) // Outputs error message if filetype is not valid
	    {
		g.drawImage (imageError, (cW / 2) - 150, (cH / 2) - 75, null);
		if (c.mCX > 482 && c.mCX < 545 && c.mCY > 348 && c.mCY < 370)
		{
		    ValidFile = true;
		}
	    }


	    //Draws gradients for aesthetic effects
	    if (dragX > 388 && dragX < 388 + 250 && dragY > 157 && dragY < 157 + 250)
	    {
		g.drawImage (plus_grd, 367, 150, null);
	    }


	    //Gets file path of the file that is dropped
	    if (dragX == -1 && dragY == -1)
	    {
		filePath = c.dropFilePath ();

		if (filePath != null && Extension (fileExtension (filePath)) != 0)
		{
		    pgNum++;
		    try
		    {
			FileImg = ImageIO.read (new File (filePath));
		    }
		    catch (Exception e)
		    {
		    }

		    //Resizes image to fit within the preview window
		    bimg = ImageIO.read (new File (filePath));
		    int type = bimg.getType () == 0 ? BufferedImage.TYPE_INT_ARGB:
		    bimg.getType ();
		    width = bimg.getWidth ();
		    height = bimg.getHeight ();
		    ratio = (width / height);

		    if (height > 347)
		    {
			resizeImg = resizeImage (bimg, type, (int) (347 * ratio), 347);

			if ((int) (347 * ratio) > 745)
			{
			    newresize = resizeImage (resizeImg, type, 745, (int) (745 * (1 / ratio)));
			}
		    }
		}
		else if (Extension (fileExtension (filePath)) == 0)
		{
		    ValidFile = false;
		}
	    }

	}

	/***************************************/
	/*PAGE 2*/
	/***************************************/
	// changes background for when the page is 2
	if (pgNum == 2)
	{
	    g.drawImage (pg2_bg, 6, 67, null); // page 2 background and misc images
	    g.drawImage (questionmark, 183, 183, null);
	    g.drawImage (next, 976, 614, null);
	    g.drawImage (prev, 915, 614, null);


	    if (c.mCX >= 183 && c.mCX <= 203 && c.mCY >= 183 && c.mCY <= 203)
	    {
		qInt++; // Draws the help window
	    }

	    //draws misc and page elements for the second page
	    if (mX >= 6 && mX <= 6 + 208)
	    {
		if (mY >= 272 && mY <= 327)
		{
		    g.drawImage (pg2mover, 6, 272, null);
		    g.drawImage (pg2img2, 35, 287, null);
		}
		if (mY >= 327 && mY <= 382)
		{
		    g.drawImage (pg2mover, 6, 327, null);
		    g.drawImage (pg2txt2, 35, 345, null);
		}
		if (mY >= 437 && mY <= 492)
		{
		    g.drawImage (pg2mover, 6, 437, null);
		    g.drawImage (pg2img2, 35, 452, null);
		}
		if (mY >= 492 && mY <= 547)
		{
		    g.drawImage (pg2mover, 6, 492, null);
		    g.drawImage (pg2txt2, 35, 510, null);
		}
	    }


	    // Control panel for selection (checkmarks)
	    if (c.mCX > 6 && c.mCX < 214)
	    {
		if (c.mCY > 272 && c.mCY < 327)
		{
		    button = 1;
		}
		else if (c.mCY >= 327 && c.mCY <= 382)
		{
		    button = 2;
		}
		else if (c.mCY >= 437 && c.mCY <= 492)
		{
		    button = 3;
		}
		else if (c.mCY >= 492 && c.mCY <= 602)
		{
		    button = 4;
		}

		c.mCX = 0;
		c.mCY = 0;
	    }

	    //Draws the checkmarks
	    if (button == 1)
	    {
		g.drawImage (checkmark, 170, 229 + 55, null);
	    }
	    else if (button == 2)
	    {
		g.drawImage (checkmark, 170, 285 + 55, null);
	    }
	    else if (button == 3)
	    {
		g.drawImage (checkmark, 170, 340 + 55 + 55, null);
	    }
	    else if (button == 4)
	    {
		g.drawImage (checkmark, 170, 394 + 55 + 55, null);
	    }

	    //Draws the image preview
	    if (Extension (fileExtension (filePath)) == 2)
	    {
		if (height > 347)
		{

		    if ((int) (347 * ratio) > 745)
		    {
			g.drawImage (newresize, 234, (int) (394 - ((int) (745 * (1 / ratio)) / 2)), null);
		    }
		    else
		    {
			g.drawImage (resizeImg, (int) (607 - (347 * ratio) / 2), 220, null);
		    }
		}
		else
		{
		    g.drawImage (bimg, (int) (607 - (width / 2)), (int) (394 - (height / 2)), null);
		}
	    }

	    // back button
	    if (c.mCX > 904 && c.mCX < 957 && c.mCY > 610 && c.mCY < 635)
	    {
		// closes prompt screen for 2nd image drop
		if (imgScreenState % 2 != 0 || txtScreenState % 2 != 0)
		{
		    if (imgScreenState % 2 != 0)
			imgScreenState = 0;
		    if (txtScreenState % 2 != 0)
			txtScreenState = 0;

		    c.mCX = 0;
		    c.mCY = 0;
		}
		// goes back to first page
		else if (imgScreenState % 2 == 0 || txtScreenState % 2 == 0)
		{
		    pgNum--;
		    button = 0;
		    c.mCX = 0;
		    c.mCY = 0;
		}
	    }


	    //Next button
	    if (c.mCX > 965 && c.mCX < 1018 && c.mCY > 610 && c.mCY < 635)
	    {
		// Assigning functions to each button
		if (button == 1)
		{
		    imgScreenState++; // If button is one, open window for second image input
		    c.mCX = 0;
		    c.mCY = 0;

		}
		else if (button == 2)
		{
		    txtScreenState++; // If button is two, open window for text file input
		    c.mCX = 0;
		    c.mCY = 0;
		}
		else if (button == 3)
		{
		    imgDecode (filePath); // If button is three, decode image and output the decoded image
		    pgNum++;
		    c.mCX = 0;
		    c.mCY = 0;
		}
		else if (button == 4)
		{
		    File lol = new File (filePath); // If button is four, decode image and output the decoded String
		    BufferedImage img = ImageIO.read (lol);
		    File file = new File ("StrDecode.txt");
		    file.createNewFile ();
		    BufferedWriter bw = new BufferedWriter (new FileWriter (file));
		    bw.write (b.strDecode (img));
		    bw.close ();
		    pgNum++;
		    c.mCX = 0;
		    c.mCY = 0;
		}
	    }

	    if (button == 1)
	    {
		//FILE ENCRYPTION METHOD: ENCRYPTS IMAGE FILE INTO IMAGE FILE
		if (!f2p.equals (filePath) && f2p != "")
		{
		    imgEncode (filePath, f2p);
		    pgNum++;
		}
		f2p = c.dropFilePath (); // Gets the second file
		if (!f2p.equals (filePath))
		{
		    imgScreenState--;
		    if (imgScreenState <= 0)
		    {
			imgScreenState = 0;
		    }
		}
	    }
	    else if (button == 2)
	    {
		// FILE ENCRYPTION METHOD: ENCRYPTS TEXT FILE INTO IMAGE FILE
		if (!f2p.equals (filePath) && f2p != "")
		{
		    File txt = new File (f2p);
		    File output = new File ("StrEncode.png");
		    File encoded = new File (filePath);
		    BufferedImage img = ImageIO.read (encoded);
		    ImageIO.write (b.strEncode (txt, img), "png", output);
		    pgNum++;

		}
		f2p = c.dropFilePath (); // Gets the second file
		if (!f2p.equals (filePath))
		{
		    txtScreenState--;
		    if (txtScreenState <= 0)
		    {
			txtScreenState = 0;
		    }
		}
	    }
	}

	/************************************/
	// PAGE 3
	/************************************/
	// changes background for when the page is 3
	if (pgNum == 3)
	{
	    g.drawImage (next, 976, 614, null);
	    g.drawImage (pg3_bg, 6, 67, null);

	    //Next button
	    if (c.mCX > 965 && c.mCX < 1018 && c.mCY > 610 && c.mCY < 635)
	    {
		pgNum = 1;
		reset ();
	    }
	}
    }


    // resets all variables to default values
    public static void reset ()
    {
	filePath = "";
	f2p = "";
	button = 0;
	x = -694;
	x2 = -694;
	y = 0;
	posX = -208;
	loadX = 660;
	qInt = 0;
	imgScreenState = 0;
	txtScreenState = 0;
    }


    // Method used for resizing an image
    private static BufferedImage resizeImage (BufferedImage img, int type, int width, int height)
    {
	BufferedImage resizedImage = new BufferedImage (width, height, type);
	Graphics2D g = resizedImage.createGraphics ();
	g.drawImage (img, 0, 0, width, height, null);
	g.dispose ();

	return resizedImage;
    }


    // Sliding UI method; second page: text/img input screen
    public static void SecondPic ()
    {
	if (imgScreenState % 2 != 0)
	{
	    if (x < 180)
		x += 15;
	}
	else
	{
	    if (x > -694)
		x -= 15;
	}

	if (txtScreenState % 2 != 0)
	{
	    if (x2 < 180)
		x2 += 15;
	}
	else
	{
	    if (x2 > -694)
		x2 -= 15;
	}

	g.drawImage (img_input, x, 100, null);
	g.drawImage (txt_input, x2, 100, null);
    }


    // METHOD FOR GETTING ALL MOUSE INPUTS FROM PCPC CONSOLE
    public static void getInput ()
    {
	mX = c.getMouseX ();
	mY = c.getMouseY ();
	mDown = c.mDown;
	mPressed = c.getPress ();
	dragX = c.getDragX ();
	dragY = c.getDragY ();

	if (mDown)
	{
	    mState = 1;
	}
	else
	{
	    mState = 0;
	}
    }


    // DRAWS THE HELP WINDOW
    public static void drawQ ()
    {
	if (qInt % 2 != 0)
	{
	    posX += 8;
	    if (posX >= 6)
	    {
		posX = 6;
	    }
	}
	else
	{
	    if (posX > -208)
		posX -= 8;
	}

	g.drawImage (help, posX, 208, null);
    }


    //MAIN RUN METHOD
    public static void start () throws Exception
    {
	long time = System.currentTimeMillis ();
	long now;
	float fps = 1000 / 60;

	while (true)
	{
	    now = System.currentTimeMillis ();
	    if (now - time > fps)
	    {
		time = now;

		c.cls ();           // Clear Screen
		getInput ();        // Get Input
		drawImage ();       // Draws all images
		drawQ ();           // Draws help window
		LoadAnimation ();   // Draws animations
		SecondPic ();       // Second file obtaining
		c.ViewUpdate ();    // Graphics update
	    }

	    Thread.sleep (1);
	}
    }
}


