package ch.zhaw.pm2.racetrack.game.track;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static ch.zhaw.pm2.racetrack.given.ConfigSpecification.SpaceType.getAllAvailableCharacterRepresentations;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.emptyList;

/**
 * Class to load a track file and check if it is a valid track file.
 *
 * @author weberph5, abuechi
 * @version 1.0.0
 */
public class TrackLoader {
    private static final int FIRST_ROW_INDEX = 0;
    private static final int MINIMUM_REQUIRED_CARS_TO_PLAY = 2;

    /**
     * Method to check if the file has content and if all lines have the same length.
     *
     * @param trackFileRows the rows of the selected files as an array of Strings.
     * @return true if valid, false if not.
     */
    public boolean isTrackFileValid(final List<String> trackFileRows) {
        return trackFileRows != null
            && !trackFileRows.isEmpty()
            && areTrackRowsSameSize(trackFileRows)
            && containsAtLeastTwoCarsWithDistinctSymbol(trackFileRows);
    }

    private boolean containsAtLeastTwoCarsWithDistinctSymbol(List<String> trackFileRows) {
        final List<Character> allAvailableCharacterRepresentations = getAllAvailableCharacterRepresentations();
        final Set<Character> uniqueCarSymbols = new HashSet<>();
        int carCount = 0;
        for (String row : trackFileRows) {
            final char[] symbols = row.toCharArray();
            for (char symbolInRow : symbols) {
                if (!allAvailableCharacterRepresentations.contains(symbolInRow)) {
                    carCount++;
                    uniqueCarSymbols.add(symbolInRow);
                }
            }
        }
        return carCount >= MINIMUM_REQUIRED_CARS_TO_PLAY && uniqueCarSymbols.size() == carCount;
    }

    private boolean areTrackRowsSameSize(List<String> trackFileRows) {
        final int firstLineLength = trackFileRows.get(FIRST_ROW_INDEX).length();
        for (String row : trackFileRows) {
            if (firstLineLength != row.length()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method to convert the selected file into an array of Strings.
     *
     * @param selectedFile the file that was selected.
     * @return the track file as an array of Strings or an empty list if the file is invalid.
     * @throws FileNotFoundException - throws if selected file is not available
     */
    public List<String> loadTrackFile(File selectedFile) throws IOException {
        if (selectedFile.exists()) {
            final List<String> fileRowContent = new ArrayList<>();
            Scanner scanner = new Scanner(selectedFile, UTF_8);
            while (scanner.hasNextLine()) {
                fileRowContent.add(scanner.nextLine());
            }
            scanner.close();
            return fileRowContent;
        } else {
            return emptyList();
        }

    }
}
