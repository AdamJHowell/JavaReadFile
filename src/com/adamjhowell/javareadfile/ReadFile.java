package com.adamjhowell.javareadfile;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ReadFile
{
	private static final Logger LOGGER = Logger.getLogger( ReadFile.class.getName() );


	@java.lang.SuppressWarnings( "squid:S106" )
	public static void main( String[] args )
	{
		LOGGER.setLevel( Level.WARNING );
		if( args.length > 0 )
		{
			List<String> inAl = readFile( args[0] );
			for( String line : inAl )
			{
				System.out.println( line );
			}
		}
		else
		{
			System.out.println( "Please enter the name of the file to read at the command line." );
		}
	} // End of main() method.


	/**
	 * readFile() reads a file and returns each uncommented line with a length greater than 0.
	 * The class should have a private static boolean DEBUG set to either true or false.
	 *
	 * @param inFileName a string representing the file to open.
	 * @return an ArrayList<String> containing every non-empty line from the input file, or null if the file could not be opened.
	 */

	private static List<String> readFile( String inFileName )
	{
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
						LOGGER.log( Level.FINEST, "readFile() is skipping a line that has only comments at row  %s", inputLineCount );
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
						LOGGER.log( Level.FINEST, "readFile is skipping a zero length line at row %s", inputLineCount );
					}
				}
			}
		}
		catch( IOException ioe )
		{
			ioe.getMessage();
		}
		return inAl;
	} // End of readFile() method.
}
