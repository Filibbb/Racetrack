package ch.zhaw.pm2.racetrack.game;

import ch.zhaw.pm2.racetrack.Config;

import java.io.File;
import java.util.Objects;

/**
 * A class to load files from file system.
 *
 * @author weberph5, abuechi
 * @version 1.0.0
 */
public abstract class AbstractFileSelector<T> {
    private final Config config;

    /**
     * Initializing file selector with a config object specifying the directory of files
     *
     * @param config the config object containing required information about the track directory
     */
    protected AbstractFileSelector(Config config) {
        this.config = config;
    }

    /**
     * Prompts the user to select a file
     *
     * @return an integer representing the index of the selected file
     */
    protected abstract int promptFileSelection();

    /**
     * Displays the available files for the user
     *
     * @param fileDirectoryContent the file list of the file directory
     */
    protected abstract void displayAvailableFiles(File[] fileDirectoryContent);

    /**
     * Shows the user an error for loading directory
     */
    protected abstract void notifyUserOfDirectoryFileLoadingFail();

    /**
     * Shows the user an error while loading a file.
     */
    protected abstract void notifyUserOfSelectionFail();

    /**
     * Method to convert selected file into respective objects
     *
     * @param file the selected file
     * @return the respective object.
     */
    protected abstract T loadSelectedFile(File file);

    /**
     * Retrieves the file directory for the use case specific
     *
     * @return the file directory required for the current usage
     */
    protected abstract File getFileDirectory();

    /**
     * Method to select a file
     *
     * @return the selected file
     */
    public T selectFile() {
        final File fileDirectory = getFileDirectory();
        if (isDirectoryValid(fileDirectory.listFiles())) {
            displayAvailableFiles(fileDirectory.listFiles());
            final int selectedFileIndex = promptFileSelection();
            final File selectedFile = Objects.requireNonNull(fileDirectory.listFiles())[selectedFileIndex];
            return loadSelectedFile(selectedFile);
        } else {
            notifyUserOfDirectoryFileLoadingFail();
            return selectFile();
        }
    }

    /**
     * Gets Configuration object used for file directories etc
     *
     * @return - current config object
     */
    protected Config getConfig() {
        return config;
    }

    private boolean isDirectoryValid(final File[] directoryFiles) {
        return directoryFiles != null && directoryFiles.length > 0;
    }
}
