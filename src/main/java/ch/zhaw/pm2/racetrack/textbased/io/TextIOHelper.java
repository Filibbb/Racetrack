package ch.zhaw.pm2.racetrack.textbased.io;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

/**
 * Static Class with methods to be used for specific Terminal In-/Outputs.
 * TextIO and TextTerminal Objects are only instanced here and don't need to be instanced separately by the users
 * of these Methods.
 */
public final class TextIOHelper {

    private static final TextIO TEXT_IO = TextIoFactory.getTextIO();
    private static final TextTerminal<?> TEXT_TERMINAL = TEXT_IO.getTextTerminal();

    /**
     * Private Constructor so no object of this class can ever be created.
     */
    private TextIOHelper() {
    }

    /**
     * Print line method
     *
     * @param lineToPrint Text that will be Printed to the Terminal
     */
    public static void println(String lineToPrint) {
        TEXT_TERMINAL.println(lineToPrint);
    }

    /**
     * This method displays the String from the Parameters and
     * reads a number between the given Boundaries (Parameters inclusive)
     * It makes sense, to add the min and max numbers into the contextMessage String.
     *
     * @param min            Minimum Number that has to be entered (inclusive)
     * @param max            Maximum Number that has to be entered (inclusive)
     * @param contextMessage String that will be printed to the Terminal, before the Input is requested.
     * @return The valid Number, added by the Player.
     */
    public static int readIntFromTerminal(int min, int max, String contextMessage) {
        return TEXT_IO.newIntInputReader().withMinVal(min).withMaxVal(max).read(contextMessage);
    }

    /**
     * This method helps to read string input from terminals by wrapping the text_io into a static utility method.
     *
     * @param contextMessage A context message given so the user is informed on what is to be expected from them
     * @return the return value of the user input
     */
    public static String readStringFromTerminal(String contextMessage) {
        return TEXT_IO.newStringInputReader().read(contextMessage);
    }
}
