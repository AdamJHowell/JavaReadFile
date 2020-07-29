package com.adamjhowell.javareadfile;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class JavaReadFile
{
	private static final Logger MAIN_LOGGER = Logger.getLogger( JavaReadFile.class.getName() );


	/**
	 * This is a simple driver to read in a text file and write the contents to the screen.
	 * Note that this will read the entire file into memory.  So this should not be used for very large files.
	 *
	 * @param args element zero must be the name of a text file to read.
	 */
	@java.lang.SuppressWarnings( "squid:S106" )
	public static void main( String[] args )
	{
		MAIN_LOGGER.setLevel( Level.WARNING );
		String logString = "JavaReadFile main()";
		MAIN_LOGGER.log( Level.FINE, logString );

		if( args.length > 0 )
		{
			List<String> inAl = readFileToList( args[0] );
			if( !inAl.isEmpty() )
			{
				for( String line : inAl )
				{
					System.out.println( line );
				}
			}
			else
			{
				logString = "The input file: " + args[0] + ", was empty or did not exist.";
				System.out.println( logString );
				MAIN_LOGGER.log( Level.SEVERE, logString );
			}
		}
		else
		{
			System.out.println( "Please enter the name of the file to read at the command line." );
		}
	} // End of main() method.


	/**
	 * readFileToList() opens a file and returns an ArrayList of Strings.
	 * Empty lines are ignored.
	 * Commented substrings are ignored.
	 * Zero-length lines are ignored.
	 *
	 * @param inFileName a string representing the file name to open.
	 * @return an ArrayList containing every significant line from the input file, or an empty ArrayList.
	 */
	private static List<String> readFileToList( String inFileName )
	{
		String logString = "readFileToList()";
		MAIN_LOGGER.log( Level.FINE, logString );
		// commentString can be changed to whatever you wish to use as a comment indicator.
		// When it is encountered, commentString and everything to the right of it will be ignored.
		String commentString = "//";
		int inputLineCount = 0;
		List<String> inAl;
		List<String> outAL = new ArrayList<>();
		logString = "readFileToList() is opening \"" + inFileName + "\", and using " + commentString + " as a comment marker.";
		MAIN_LOGGER.log( Level.FINE, logString );

		// Attempt to open the file using the Java 8 Files class.  This ensures it will close automatically.
		try
		{
			inAl = Files.readAllLines( Paths.get( inFileName ) );
		}
		catch( IOException e )
		{
			logString = "IO Exception when opening input file: " + inFileName;
			MAIN_LOGGER.log( Level.SEVERE, logString );
			MAIN_LOGGER.log( Level.SEVERE, e.getLocalizedMessage() );
			// Return an empty ArrayList to avoid NPEs in the calling method.
			return outAL;
		}

		// Read lines until EOF.
		for( String line : inAl )
		{
			inputLineCount++;
			// Check for comments.
			if( line.contains( commentString ) )
			{
				// Grab all of the text up to the comment.
				String subString = line.substring( 0, line.indexOf( commentString ) ).trim();

				// Only add lines with content.
				if( subString.length() > 0 )
				{
					// Add the line to our ArrayList.
					outAL.add( subString );
				}
				else
				{
					logString = "readFileToList() is skipping a line that has only comments at row " + inputLineCount;
					MAIN_LOGGER.log( Level.FINEST, logString );
				}
			}
			else
			{
				// Ignore empty lines and lines that contain only whitespace.
				if( line.length() > 0 && !line.matches( "\\s+" ) )
				{
					// Add the line to our ArrayList.
					outAL.add( line.trim() );
				}
				else
				{
					logString = "readFileToList() is skipping a zero length line at row " + inputLineCount;
					MAIN_LOGGER.log( Level.FINEST, logString );
				}
			}
		}
		return outAL;
	} // End of readFileToList() method.


	/**
	 * bufferedReadFileToList() reads a file and returns each uncommented line with a length greater than 0.
	 * Empty lines are ignored.
	 * Commented substrings are ignored.
	 * Zero-length lines are ignored.
	 *
	 * @param inFileName a string representing the file name to open.
	 * @return an ArrayList containing every non-empty line from the input file, or an empty List if the file could not be opened.
	 */
	private static List<String> bufferedReadFileToList( String inFileName )
	{
		String logString = "bufferedReadFileToList()";
		MAIN_LOGGER.log( Level.FINE, logString );
		// commentString can be changed to whatever you wish to use as a comment indicator.  When this String is encountered, the rest of the line will be ignored.
		String commentString = "//";
		List<String> inAl = new ArrayList<>();

		// Attempt to open the file using "try with resources", to ensure it will close automatically.
		try( BufferedReader inBR = new BufferedReader( new FileReader( inFileName ) ) )
		{
			String line;
			int inputLineCount = 0;

			// Read lines until EOF.
			while( ( line = inBR.readLine() ) != null )
			{
				inputLineCount++;
				// Check for comments.
				if( line.contains( commentString ) )
				{
					// Grab all of the text up to the comment.
					String subString = line.substring( 0, line.indexOf( commentString ) );

					// Only add lines with content.
					if( subString.length() > 0 )
					{
						// Add the line to our ArrayList.
						inAl.add( subString );
					}
					else
					{
						logString = "bufferedReadFileToList() is skipping a line that has only comments at row " + inputLineCount;
						MAIN_LOGGER.log( Level.FINE, logString );
					}
				}
				else
				{
					// Ignore empty lines and lines that contain only whitespace.
					if( line.length() > 0 && !line.matches( "\\s+" ) )
					{
						// Add the line to our ArrayList.
						inAl.add( line.trim() );
					}
					else
					{
						logString = "bufferedReadFileToList is skipping a zero length line at row " + inputLineCount;
						MAIN_LOGGER.log( Level.FINE, logString );
					}
				}
			}
		}
		catch( IOException ioe )
		{
			logString = ioe.getLocalizedMessage();
			MAIN_LOGGER.log( Level.SEVERE, logString );
		}
		return inAl;
	} // End of bufferedReadFileToList() method.


	/**
	 * readFileToString() takes a file name and returns the contents in one large String.
	 *
	 * @param inFileName the path to the file to read.
	 * @return a String that represents the contents of the file.
	 */
	static String readFileToString( String inFileName )
	{
		String logString = "readFileToString()";
		MAIN_LOGGER.log( Level.FINE, logString );

		byte[] encoded = new byte[0];
		try
		{
			encoded = Files.readAllBytes( Paths.get( inFileName ) );
		}
		catch( IOException ioe )
		{
			logString = "readFileToString() threw an IOException: " + ioe.toString();
			MAIN_LOGGER.log( Level.SEVERE, logString );
			ioe.printStackTrace();
		}
		return new String( encoded, StandardCharsets.UTF_8 );
	} // End of readFileToString() method.
}
