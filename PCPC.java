// by Peter Li
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// v1.15 - 3/28/12
// - Added getPixelColor function
// - Fixed uninitialized canvas bug
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// v1.14 - 12/14/11
// - Massive efficiency improvements
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// v1.13 - 11/25/11
// - Audio features
// - Added random color methods
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// v1.12 - 11/20/11
// - Implemented MouseListener
// - Implemented MouseMotionListener
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// v1.11 - 11/17/11
// - File IO features
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// v1.1 - 11/04/11
// - Added graphics panel
// - Added Graphics drawing capabilities
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// v1.0 - 10/02/11
// - Initial Release
// - Basic IO features
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.lang.Object;
import java.io.*;
import java.text.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.awt.event.KeyListener;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.lang.Object;
import java.awt.image.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.Random;
import java.awt.GradientPaint;
import javax.swing.JFrame;
import java.net.URL;
import java.io.BufferedReader;
import java.awt.*;
import javax.sound.sampled.*;

public class PCPC implements KeyListener, MouseListener, MouseMotionListener, DropTargetListener
{
    // GUI variables
    public String version = "PC Pwns Console v1.15";
    public JFrame f;
    public Panel p;
    public TextArea textArea;
    public static TextField textField;
    public static DropTarget dt;
    public static JTextArea ta;

    // general variables
    public static boolean inputted = false;

    public static String iString = "";
    public static int iInt = 0;
    public static long iLong = 0;
    public static double iDouble = 0;
    public static float iFloat = 0;
    public static short iShort = 0;
    public static String text = "";
    public static String[] filePath = new String [1337];

    public Random rd = new Random ();
    public int randomInt;
    public int red;
    public int green;
    public int blue;
    public Color clr;

    // audio variables
    public static AudioInputStream stream;
    public static AudioFormat format;
    public static Clip clip;

    // mouse variables
    public static int mouseX;
    public static int mouseY;
    public static int mDownX;
    public static int mDownY;
    public static int mCX, mCY;
    public static boolean mClicked;
    public static boolean mPressed;
    public static boolean mReleased;
    public static boolean mDown;
    public static Point dragLoc = new Point ();

    // keyboard variables
    public static int key;
    public static int keyR;

    // graphics output variables
    public Graphics BufferedGraphics;
    public Graphics ImageGraphics;
    public Graphics g;
    public BufferedImage image;
    public Canvas canvas;

    public static int width = 1024;
    public static int height = 640;

    // I/O variables
    public static BufferedReader reader;
    public static BufferedWriter writer;

    // printstream for setOut();
    //PrintStream printStream = new PrintStream (new FilteredStream (new ByteArrayOutputStream ()));

    public PCPC () throws Exception
    {
	// frame
	f = new JFrame (version);
	f.setSize (1024, 640);

	// textarea
	//textArea = new TextArea(100, 30);
	//textArea.setEditable(false);

	// textfield
	//textField = new TextField();
	//textField.addKeyListener(this);

	// canvas
	canvas = new Canvas ();
	canvas.addMouseListener (this);
	canvas.addMouseMotionListener (this);
	canvas.addKeyListener (this);
	canvas.setBackground (Color.WHITE);
	canvas.setSize (width, height);

	// double buffering
	image = new BufferedImage (width, height, BufferedImage.TYPE_INT_ARGB);
	BufferedGraphics = image.getGraphics ();

	// panel
	p = new Panel ();
	p.setLayout (new BorderLayout (10, 10));
	//p.add (textArea, BorderLayout.LINE_START);
	p.add (canvas, BorderLayout.CENTER);
	//p.add (textField, BorderLayout.PAGE_START);
	dt = new DropTarget (canvas, this);

	f.getContentPane ().add (p);
	f.setUndecorated (true);
	f.setTitle ("Steganographer");
	f.setIconImage (new ImageIcon ("img/layout/Tray_48.png").getImage ());
	f.setVisible (true);
	f.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

	//System.setOut (printStream);
    }

/*
    // creates new stream output to console screen
    class FilteredStream extends FilterOutputStream
    {
	public FilteredStream (OutputStream aStream)
	{
	    super (aStream);
	}

	public void write (byte b[], int off, int len) throws IOException
	{
	    String aString = new String (b, off, len);
	    textArea.append (aString);
	}
    }
*/

    // ////////////////////////////////
    // FILE IO
    // ////////////////////////////////

    public static String readFile (String file, int line) throws Exception
    {
	String text = "";
	reader = new BufferedReader (new FileReader (file));

	for (int i = 1 ; i <= line ; i++)
	{
	    text = reader.readLine ();
	}

	reader.close ();

	return text;
    }


    public static void writeFile (String text, String file) throws Exception
    {
	writer = new BufferedWriter (new FileWriter (file));

	writer.write (text);
	writer.flush ();
	writer.close ();
    }


    public static void appendFile (String text, String file) throws Exception
    {
	writer = new BufferedWriter (new FileWriter (file, true));

	writer.write (text);
	writer.newLine ();
	writer.flush ();
	writer.close ();
    }


    // ////////////////////////////////
    // GRAPHICS
    // ////////////////////////////////

    public Graphics getGraphics ()
    {
	return BufferedGraphics;
    }


    public void ViewUpdate ()
    {
	ImageGraphics = canvas.getGraphics ();
	ImageGraphics.drawImage (image, 0, 0, width, height, null);
    }


    public int getRGB (int x, int y)
    {
	int c = image.getRGB (x, y);
	return c;
    }


    public Color getPixelColor (int x, int y)
    {
	int c = image.getRGB (x, y);
	Color clr = new Color (c);

	return clr;
    }


    public void cls ()
    {
	g = getGraphics ();
	clr = g.getColor ();
	g.setColor (Color.WHITE);
	g.fillRect (0, 0, width, height);
	g.setColor (clr);
    }


    public Color randColor ()
    {
	red = rd.nextInt (256);
	green = rd.nextInt (256);
	blue = rd.nextInt (256);

	clr = new Color (red, green, blue);

	return clr;
    }


    public Color randBlue ()
    {
	red = rd.nextInt (256);
	green = red;
	blue = 255;

	clr = new Color (red, green, blue);

	return clr;
    }


    public Color randRed ()
    {
	red = 255;
	green = rd.nextInt (256);
	blue = green;

	clr = new Color (red, green, blue);

	return clr;
    }


    public Color randGreen ()
    {
	red = rd.nextInt (256);
	green = 255;
	blue = red;

	clr = new Color (red, green, blue);

	return clr;
    }


    // ////////////////////////////////
    // AUDIO
    // ////////////////////////////////

    public static void playAudio (String file) throws Exception
    {
	stream = AudioSystem.getAudioInputStream (new File (file));
	format = stream.getFormat ();
	DataLine.Info info = new DataLine.Info (Clip.class, format);
	clip = (Clip) AudioSystem.getLine (info);
	clip.open (stream);
	clip.start ();
    }


    public static void loopAudio (String file) throws Exception
    {
	stream = AudioSystem.getAudioInputStream (new File (file));
	format = stream.getFormat ();
	DataLine.Info info = new DataLine.Info (Clip.class, format);
	clip = (Clip) AudioSystem.getLine (info);
	clip.open (stream);
	clip.loop (Clip.LOOP_CONTINUOUSLY);
    }


    public static void stopAudio ()
    {
	clip.close ();
    }


    // ////////////////////////////////
    // TEXT
    // ////////////////////////////////

    public void clt ()
    {
	textArea.setText ("");
    }


    public static String getText ()
    {
	if (inputted)
	{
	    inputted = false;
	    return text;
	}
	return "";
    }


    // ////////////////////////////////
    // GENERAL METHODS
    // ////////////////////////////////

    public int randInt (int i)
    {
	randomInt = rd.nextInt (i);

	return randomInt;
    }


    public void delay (int d) throws Exception
    {
	Thread.sleep (d);
    }


    public static String getString ()
    {
	do
	{
	}
	while (inputted == false);

	inputted = false;
	iString = textField.getText ();
	return iString;

    }


    public static char getChar ()
    {
	do
	{
	}
	while (inputted == false);

	inputted = false;
	iString = textField.getText ();
	textField.setText ("");
	return iString.charAt (0);
    }


    public static Number getNumber ()
    {
	String numberString = getString ();
	try
	{
	    numberString = numberString.trim ().toUpperCase ();
	    return NumberFormat.getInstance ().parse (numberString);
	}
	catch (Exception e)
	{
	    // returns 0
	    return new Integer (0);
	}
    }


    public static int getInt ()
    {
	// returns int
	return getNumber ().intValue ();
    }


    public static long getLong ()
    {
	// returns long
	return getNumber ().longValue ();
    }


    public static float getFloat ()
    {
	// returns float
	return getNumber ().floatValue ();
    }


    public static double getDouble ()
    {
	// returns double
	return getNumber ().doubleValue ();
    }


    public static double getShort ()
    {
	// returns short
	return getNumber ().shortValue ();
    }


    public int getCanvasWidth ()
    {
	// returns width
	return canvas.getWidth ();
    }


    public int getCanvasHeight ()
    {
	// returns height
	return canvas.getHeight ();
    }


    public int getMouseX ()
    {
	// returns x coord
	return mouseX;
    }


    public int getMouseY ()
    {
	// returns y coord
	return mouseY;
    }


    public boolean getClick ()
    {
	// returns mouseclick status
	if (mClicked)
	{
	    mClicked = false;
	    return true;
	}

	return false;
    }


    public boolean getPress ()
    {
	// returns mousepressed status
	if (mPressed)
	{
	    mPressed = false;
	    return true;
	}

	return false;
    }


    public boolean getRelease ()
    {
	// returns mousereleased status
	if (mReleased)
	{
	    mReleased = false;
	    return true;
	}

	return false;
    }


    public int getDragX ()
    {
	return dragLoc.x;
    }


    public int getDragY ()
    {
	return dragLoc.y;
    }


    public int getKey ()
    {
	// returns key pressed
	return key;
    }


    public int getKeyR ()
    {
	// returns key released
	return keyR;
    }


    // ////////////////////////////////
    // LISTENER ARGUMENTS
    // ////////////////////////////////

    // mouselistener arguments
    public void mouseClicked (MouseEvent e)
    {
	mClicked = true;

	mCX = e.getX ();
	mCY = e.getY ();
	//System.out.println (mCX + "    " + mCY);

	if (mCX > 980 && mCX < 1002 && mCY > 5 && mCY < 20)
	{
	    f.setState (Frame.ICONIFIED);
	    mouseX = 0;
	    mouseY = 0;
	}
	if (mouseX > 1003 && mouseX < 1023 && mouseY > 2 && mouseY < 22)
	{
	    System.exit (0);
	}

	if (mouseX > 844 && mouseX < 897 && mouseY > 610 && mouseY < 635) // w53 h25
	{
	    //importFile ();
	}
	dragLoc.x = 5000;
	dragLoc.y = 5000;
    }


    public void mouseEntered (MouseEvent e)
    {
    }


    public void mouseExited (MouseEvent e)
    {
    }


    public void mousePressed (MouseEvent e)
    {
	// mClicked = true;
	mPressed = true;
	mouseX = e.getX ();
	mouseY = e.getY ();
	mDown = true;
    }


    public void mouseReleased (MouseEvent e)
    {
	// mClicked = false;
	mReleased = true;
	mDown = false;
    }


    // mousemotionlistener arguments
    public void mouseDragged (MouseEvent e)
    {
	if (mouseY <= 15)
	{
	    f.setLocation (e.getXOnScreen () - mouseX, e.getYOnScreen () - mouseY);
	}
    }


    public void mouseMoved (MouseEvent e)
    {
	mouseX = e.getX ();
	mouseY = e.getY ();
    }


    // keylistener arguments
    public void keyTyped (KeyEvent e)
    {
    }


    public void keyPressed (KeyEvent e)
    {
	key = e.getKeyCode ();
	// text = textField.getText ();

	// sets inputted to true
	switch (key)
	{
	    case KeyEvent.VK_ENTER:
		inputted = true;
		text = textField.getText ();
		textField.setText ("");
	}
    }


    public void keyReleased (KeyEvent e)
    {
	keyR = e.getKeyCode ();
	// text = textField.getText ();

	if (keyR == key)
	{
	    key = 0;
	}
    }


    // droptargetlistener arguments
    public void dragEnter (DropTargetDragEvent dtde)
    {
    }


    public void dragExit (DropTargetEvent dte)
    {
	dragLoc.x = -1;
	dragLoc.y = -1;
    }


    public void dragOver (DropTargetDragEvent dtde)
    {
	dragLoc = dtde.getLocation ();
    }


    public void dropActionChanged (DropTargetDragEvent dtde)
    {
	dragLoc.x = -1;
	dragLoc.y = -1;
    }


    public String dropFilePath ()
    {
	return filePath [0];
    }


    public void drop (DropTargetDropEvent dtde)
    {
	try
	{
	    Transferable tr = dtde.getTransferable ();
	    DataFlavor[] flavors = tr.getTransferDataFlavors ();

	    for (int i = 0 ; i < flavors.length ; i++)
	    {
		if (flavors [i].isFlavorJavaFileListType ())
		{
		    dtde.acceptDrop (DnDConstants.ACTION_COPY_OR_MOVE);

		    java.util.List list = (java.util.List) tr.getTransferData (flavors [i]);

		    for (int j = 0 ; j < list.size () ; j++)
		    {
			filePath [j] = (String) (list.get (j) + "");
		    }

		    dtde.dropComplete (true);
		    dragLoc.x = -1;
		    dragLoc.y = -1;

		    return;
		}
		else if (flavors [i].isFlavorSerializedObjectType ())
		{
		    dtde.acceptDrop (DnDConstants.ACTION_COPY_OR_MOVE);
		    Object o = tr.getTransferData (flavors [i]);

		    System.out.println ("Object: " + o);

		    dtde.dropComplete (true);

		    dragLoc.x = -1;
		    dragLoc.y = -1;

		    return;
		}
		else if (flavors [i].isRepresentationClassInputStream ())
		{
		    dtde.acceptDrop (DnDConstants.ACTION_COPY_OR_MOVE);
		    dtde.dropComplete (true);

		    dragLoc.x = -1;
		    dragLoc.y = -1;

		    return;
		}
	    }
	    dtde.rejectDrop ();
	}
	catch (Exception e)
	{
	    e.printStackTrace ();
	    dtde.rejectDrop ();
	}
    }
}
