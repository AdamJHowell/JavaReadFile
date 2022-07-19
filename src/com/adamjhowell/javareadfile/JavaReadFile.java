package com.adamjhowell.javareadfile;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class contains multiple methods which show different ways to read text files in Java.
 * The method names, while long, describe the technique used and the output.
 * The readAllLinesToList() and readAllBytesToString() methods require Java 7.
 *
 * @version JDK 1.7
 */
public class JavaReadFile
{
	private static final Logger MAIN_LOGGER = Logger.getLogger( JavaReadFile.class.getName() );


	/**
	 * main() is only meant to show one way to call each method.
	 * Note that this will read the entire file into memory, three times, so be mindful of memory concerns.
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
			List<String> inAl1 = readAllLinesToList( args[0] );
			List<String> inAl2 = bufferedReaderReadLineToList( args[0] );
			String inString = readAllBytesToString( args[0] );

			if( !inAl1.isEmpty() )
				for( String line : inAl1 )
					System.out.println( line );
			else
			{
				logString = "The input file: " + args[0] + ", was empty or did not exist.";
				System.out.println( logString );
				MAIN_LOGGER.log( Level.SEVERE, logString );
			}
			if( new HashSet<>( inAl1 ).containsAll( inAl2 ) )
				System.out.println( "\nreadAllLinesToList() and bufferedReaderReadLineToList() match!\n" );
			else
				System.out.println( "\nreadAllLinesToList() and bufferedReaderReadLineToList() do not match!\n" );
			System.out.println( inString );
		}
		else
			System.out.println( "Please enter the name of the input file as a command line argument." );
	} // End of main() method.


	/**
	 * readAllLinesToList() opens a file and returns a List of Strings.
	 * Each element in the List represents one line.
	 * Empty lines are not added to the output List.
	 * Commented substrings are not added to the output List.
	 * Zero-length lines are not added to the output List.
	 *
	 * @param inFileName a string representing the file name to open.
	 * @return an ArrayList containing every significant line from the input file, or an empty ArrayList.
	 */
	static List<String> readAllLinesToList( String inFileName )
	{
		String logString = "readAllLinesToList()";
		MAIN_LOGGER.log( Level.FINE, logString );
		// commentString can be changed to whatever you wish to use as a comment indicator.
		// When it is encountered, commentString and everything to the right of it will be ignored.
		String commentString = "//";
		int inputLineCount = 0;
		List<String> inAl;
		List<String> outAL = new ArrayList<>();
		logString = "readAllLinesToList() is opening \"" + inFileName + "\", and using " + commentString + " as a comment marker.";
		MAIN_LOGGER.log( Level.FINE, logString );

		// Attempt to open the file using the Java 7 Files class, which will close the file automatically.
		try
		{
			inAl = Files.readAllLines( Paths.get( inFileName ) );
		}
		catch( IOException ioException )
		{
			logString = "readAllLinesToList() threw an IOException reading " + inFileName + ": " + ioException.getLocalizedMessage();
			MAIN_LOGGER.log( Level.SEVERE, logString );
			return outAL;
		}

		for( String line : inAl )
		{
			inputLineCount++;
			// Check for comments.
			if( line.contains( commentString ) )
			{
				// Grab all text up to the comment.
				String subString = line.substring( 0, line.indexOf( commentString ) ).trim();

				// Only add lines with content.
				if( subString.length() > 0 )
					outAL.add( subString );
				else
				{
					logString = "readAllLinesToList() is skipping a line that has only comments at row " + inputLineCount;
					MAIN_LOGGER.log( Level.FINEST, logString );
				}
			}
			else
			{
				// Ignore empty lines and lines that contain only whitespace.
				if( line.length() > 0 && !line.matches( "\\s+" ) )
					outAL.add( line.trim() );
				else
				{
					logString = "readAllLinesToList() is skipping a zero length line at row " + inputLineCount;
					MAIN_LOGGER.log( Level.FINEST, logString );
				}
			}
		}
		return outAL;
	} // End of readAllLinesToList() method.


	/**
	 * bufferedReaderReadLineToList() opens a file and returns a List of Strings.
	 * Each element in the List represents one line.
	 * Empty lines are not added to the output List.
	 * Commented substrings are not added to the output List.
	 * Zero-length lines are not added to the output List.
	 *
	 * @param inFileName a string representing the file name to open.
	 * @return an ArrayList containing every non-empty line from the input file, or an empty List if the file could not be opened.
	 */
	static List<String> bufferedReaderReadLineToList( String inFileName )
	{
		String logString = "bufferedReaderReadLineToList()";
		MAIN_LOGGER.log( Level.FINE, logString );
		// commentString can be changed to whatever you wish to use as a comment indicator.  When this String is encountered, the rest of the line will be ignored.
		String commentString = "//";
		List<String> inAl = new ArrayList<>();

		// Open the file using try-with-resources.
		try( BufferedReader inBR = new BufferedReader( new FileReader( inFileName ) ) )
		{
			String line;
			int inputLineCount = 0;

			while( ( line = inBR.readLine() ) != null )
			{
				inputLineCount++;
				// Check for comments.
				if( line.contains( commentString ) )
				{
					// Grab all text up to the comment.
					String subString = line.substring( 0, line.indexOf( commentString ) );

					// Only add lines with content.
					if( subString.length() > 0 )
						inAl.add( subString );
					else
					{
						logString = "bufferedReaderReadLineToList() is skipping a line that has only comments at row " + inputLineCount;
						MAIN_LOGGER.log( Level.FINE, logString );
					}
				}
				else
				{
					// Ignore empty lines and lines that contain only whitespace.
					if( line.length() > 0 && !line.matches( "\\s+" ) )
						inAl.add( line.trim() );
					else
					{
						logString = "bufferedReaderReadLineToList is skipping a zero length line at row " + inputLineCount;
						MAIN_LOGGER.log( Level.FINE, logString );
					}
				}
			}
		}
		catch( IOException ioException )
		{
			logString = "bufferedReaderReadLineToList() threw an IOException: " + ioException.getLocalizedMessage();
			MAIN_LOGGER.log( Level.SEVERE, logString );
		}
		return inAl;
	} // End of bufferedReaderReadLineToList() method.


	/**
	 * readAllBytesToString() takes a file name and returns the contents in one large String.
	 *
	 * @param inFileName the path to the file to read.
	 * @return a String that represents the contents of the file.
	 */
	static String readAllBytesToString( String inFileName )
	{
		String logString = "readAllBytesToString()";
		MAIN_LOGGER.log( Level.FINE, logString );

		byte[] byteArray = new byte[0];
		// Attempt to open the file using the Java 7 Files class, which will close the file automatically.
		try
		{
			byteArray = Files.readAllBytes( Paths.get( inFileName ) );
		}
		catch( IOException ioException )
		{
			logString = "readAllBytesToString() threw an IOException: " + ioException.getLocalizedMessage();
			MAIN_LOGGER.log( Level.SEVERE, logString );
		}
		return new String( byteArray, StandardCharsets.UTF_8 );
	} // End of readAllBytesToString() method.
}
