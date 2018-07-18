package com.adamjhowell.javareadfile;


import java.io.IOException;
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
		if( args.length > 0 )
		{
			List<String> inAl = readFile( args[0] );
			if( !inAl.isEmpty() )
			{
				for( String line : inAl )
				{
					System.out.println( line );
				}
			}
			else
			{
				System.out.println( "The input file: " + args[0] + ", was empty or did not exist." );
			}
		}
		else
		{
			System.out.println( "Please enter the name of the file to read at the command line." );
		}
	} // End of main() method.


	/**
	 * readFile() opens a file and returns an ArrayList of Strings.
	 * Empty lines are ignored.
	 * Commented substrings are ignored.
	 * Zero-length lines are ignored.
	 * The class should have a logger named MAIN_LOGGER (from java.util.logging.Logger).
	 *
	 * @param inFileName a string representing the file to open.
	 * @return an ArrayList<String> containing every significant line from the input file, or an empty ArrayList.
	 */
	private static List<String> readFile( String inFileName )
	{
		// commentString can be changed to whatever you wish to use as a comment indicator.
		// When it is encountered, commentString and everything to the right of it will be ignored.
		String commentString = "//";
		int inputLineCount = 0;
		List<String> inAl;
		List<String> outAL = new ArrayList<>();
		MAIN_LOGGER.log( Level.FINEST, "readFile() is opening {0}, and using {1} as a comment marker.", new Object[]{ inFileName, commentString } );

		// Attempt to open the file using the Java 8 Files class.  This ensures it will close automatically.
		try
		{
			inAl = Files.readAllLines( Paths.get( inFileName ) );
		}
		catch( IOException e )
		{
			MAIN_LOGGER.log( Level.WARNING, "IO Exception when opening input file: " + inFileName );
			MAIN_LOGGER.log( Level.WARNING, e.getLocalizedMessage() );
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
					MAIN_LOGGER.log( Level.FINEST, "readFile() is skipping a line that has only comments at row {0}", inputLineCount );
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
					MAIN_LOGGER.log( Level.FINEST, "readFile() is skipping a zero length line at row {0}", inputLineCount );
				}
			}
		}
		return outAL;
	} // End of readFile() method.
}
