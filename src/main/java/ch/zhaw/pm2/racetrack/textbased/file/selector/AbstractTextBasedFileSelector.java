package ch.zhaw.pm2.racetrack.textbased.file.selector;

import ch.zhaw.pm2.racetrack.Config;
import ch.zhaw.pm2.racetrack.game.AbstractFileSelector;

import java.io.File;
import java.util.Objects;

import static ch.zhaw.pm2.racetrack.textbased.io.TextIOHelper.println;
import static ch.zhaw.pm2.racetrack.textbased.io.TextIOHelper.readIntFromTerminal;

/**
 * The abstract base for text based specific implementations of file loaders as many share the same implementation
 *
 * @author abuechi
 * @version 1.0.0
 */
public abstract class AbstractTextBasedFileSelector<T> extends AbstractFileSelector<T> {
    /**
     * Initializing file selector with a config object specifying the directory of files
     *
     * @param config the config object containing required information about the track directory
     */
    protected AbstractTextBasedFileSelector(Config config) {
        super(config);
    }

    /**
     * Prompts the user in a text based view to enter a file name via terminal
     *
     * @return the user entered file name
     */
    @Override
    public int promptFileSelection() {
        return readIntFromTerminal(0, Objects.requireNonNull(getFileDirectory().listFiles()).length - 1, "Please type the listed number of the above file names:");
    }

    /**
     * Displays the read files available in a text based form.
     *
     * @param fileDirectoryContent the read content of the config follower directory
     */
    @Override
    public void displayAvailableFiles(File[] fileDirectoryContent) {
        println("Available Files: \n");
        for (int index = 0; index < fileDirectoryContent.length; index++) {
            println(index + " " + fileDirectoryContent[index].getName());
        }
    }

    /**
     * The action that is to be executed upon an error during the selection of the follower file
     */
    @Override
    public void notifyUserOfSelectionFail() {
        println("There was an error loading the selected file. File seems to be invalid or was moved.");
    }

    /**
     * The action that is to be executed upon an error during the selection of the track file
     */
    @Override
    public void notifyUserOfDirectoryFileLoadingFail() {
        println("Directory is empty. Please add files to folder:" + getFileDirectory().getAbsolutePath());
    }
}
