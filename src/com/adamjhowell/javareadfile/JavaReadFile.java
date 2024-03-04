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
 * The readAllLinesToListMinusComments() and readAllBytesToString() methods require Java 7.
 *
 * @version JDK 1.7
 */
public class JavaReadFile
{
  private static final Logger mainLogger = Logger.getLogger( JavaReadFile.class.getName() );


  /**
   * main() is only meant to show one way to call each method.
   * Note that this will read the entire file into memory, three times, so be mindful of memory concerns.
   *
   * @param args element zero must be the name of a text file to read.
   */
  @java.lang.SuppressWarnings( "squid:S106" )
  public static void main( String[] args )
  {
    mainLogger.setLevel( Level.WARNING );
    String logString = "JavaReadFile main()";
    mainLogger.log( Level.FINE, logString );

    if( args.length > 0 )
    {
      List<String> inAl1 = readAllLinesToListMinusComments( args[0], "//" );
      List<String> inAl2 = readAllLinesToListMinusComments( args[0], "#" );
      List<String> inAl3 = bufferedReaderReadLineToList( args[0] );
      String inString = readAllBytesToString( args[0] );

      if( !inAl1.isEmpty() )
      {
        for( String line : inAl1 )
          System.out.println( line );
        for( String line : inAl2 )
          System.out.println( line );
      }
      else
      {
        logString = "The input file: " + args[0] + ", was empty or did not exist.";
        System.out.println( logString );
        mainLogger.log( Level.SEVERE, logString );
      }
      if( new HashSet<>( inAl1 ).containsAll( inAl3 ) )
        System.out.println( "\nreadAllLinesToListMinusComments() and bufferedReaderReadLineToList() match!\n" );
      else
        System.out.println( "\nreadAllLinesToListMinusComments() and bufferedReaderReadLineToList() do not match!\n" );
      System.out.println( inString );
    }
    else
      System.out.println( "Please enter the name of the input file as a command line argument." );
  } // End of main() method.


  /**
   * readAllLinesToListMinusComments() opens a file and returns a List of Strings.
   * Each element in the List represents one line.
   * Empty lines are not added to the output List.
   * Commented substrings are not added to the output List.
   * Zero-length lines are not added to the output List.
   *
   * @param inFileName   a String representing the file name to open.
   * @param commentToken a String representing the beginning of a comment.
   * @return an ArrayList containing every significant line from the input file, or an empty ArrayList.
   */
  static List<String> readAllLinesToListMinusComments( String inFileName, String commentToken )
  {
    mainLogger.log( Level.FINE, "readAllLinesToListMinusComments()" );
    int inputLineCount = 0;
    List<String> inAl;
    List<String> outAL = new ArrayList<>();
    mainLogger.log( Level.FINE, () -> "readAllLinesToListMinusComments() is opening \"" + inFileName + "\", and using " + commentToken + " as a comment marker." );

    // Attempt to open the file using the Java 7 Files class, which will close the file automatically.
    try
    {
      inAl = Files.readAllLines( Paths.get( inFileName ) );
    }
    catch( IOException ioException )
    {
      mainLogger.log( Level.SEVERE, () -> "readAllLinesToListMinusComments() threw an IOException reading " + inFileName + ": " + ioException.getLocalizedMessage() );
      return outAL;
    }

    for( String line : inAl )
    {
      inputLineCount++;
      // Check for comments.
      if( line.contains( commentToken ) )
      {
        // Grab all text up to the comment.
        String subString = line.substring( 0, line.indexOf( commentToken ) ).trim();

        // Only add lines with content.
        if( subString.length() > 0 )
          outAL.add( subString );
        else
          mainLogger.log( Level.FINEST, "readAllLinesToListMinusComments() is skipping a line that has only comments at row {0}", inputLineCount );
      }
      else
      {
        // Ignore empty lines and lines that contain only whitespace.
        if( line.length() > 0 && !line.matches( "\\s+" ) )
          outAL.add( line.trim() );
        else
          mainLogger.log( Level.FINEST, "readAllLinesToListMinusComments() is skipping a zero length line at row {0}", inputLineCount );
      }
    }
    return outAL;
  } // End of readAllLinesToListMinusComments() method.


  /**
   * readAllLinesToList() will read a text file into an ArrayList, and return that List.
   *
   * @param inFileName the file to read.
   * @return a List<String>, where each element represents one line from the input file.
   */
  static List<String> readAllLinesToList( String inFileName )
  {
    mainLogger.log( Level.FINE, "readAllLinesToList()" );

    List<String> inAl = new ArrayList<>();
    try
    {
      inAl = Files.readAllLines( Paths.get( inFileName ) );
    }
    catch( IOException ioException )
    {
      mainLogger.log( Level.SEVERE, () -> "readAllLinesToListMinusComments() threw an IOException reading " + inFileName + ": " + ioException.getLocalizedMessage() );
    }
    return inAl;
  } // End of readAllLinesToList() method.


  /**
   * bufferedReaderReadLineToList() opens a file and returns a List of Strings.
   * Each element in the List represents one line.
   * Empty lines are not added to the output List.
   * Commented substrings are not added to the output List.
   * Zero-length lines are not added to the output List.
   *
   * @param inFileName a String representing the file name to open.
   * @return an ArrayList containing every non-empty line from the input file, or an empty List if the file could not be opened.
   */
  static List<String> bufferedReaderReadLineToList( String inFileName )
  {
    mainLogger.log( Level.FINE, "bufferedReaderReadLineToList()" );
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
            mainLogger.log( Level.FINE, "bufferedReaderReadLineToList() is skipping a line that has only comments at row {0}", inputLineCount );
        }
        else
        {
          // Ignore empty lines and lines that contain only whitespace.
          if( line.length() > 0 && !line.matches( "\\s+" ) )
            inAl.add( line.trim() );
          else
            mainLogger.log( Level.FINE, "bufferedReaderReadLineToList is skipping a zero length line at row {0}", inputLineCount );
        }
      }
    }
    catch( IOException ioException )
    {
      mainLogger.log( Level.SEVERE, "bufferedReaderReadLineToList() threw an IOException: {0}", ioException.getLocalizedMessage() );
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
    mainLogger.log( Level.FINE, "readAllBytesToString()" );

    byte[] byteArray = new byte[0];
    // Attempt to open the file using the Java 7 Files class, which will close the file automatically.
    try
    {
      byteArray = Files.readAllBytes( Paths.get( inFileName ) );
    }
    catch( IOException ioException )
    {
      mainLogger.log( Level.SEVERE, "readAllBytesToString() threw an IOException: {0}", ioException.getLocalizedMessage() );
    }
    return new String( byteArray, StandardCharsets.UTF_8 );
  } // End of readAllBytesToString() method.
}
