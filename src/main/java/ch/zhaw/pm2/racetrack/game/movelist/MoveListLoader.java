package ch.zhaw.pm2.racetrack.game.movelist;

import ch.zhaw.pm2.racetrack.game.util.PositionVector.Direction;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.util.Collections.emptyList;

/**
 * Class to load a move list file and make it usable for the move list strategy.
 *
 * @author weberph5
 * @version 1.0.0
 */
public class MoveListLoader {

    /**
     * Loads the previously selected move list file.
     *
     * @param selectedFile the selected file
     * @return a list of directions as instructions for the car with this strategy
     * @throws IOException if reading file did not work correctly or file did not exist
     */
    public List<Direction> loadMoveListFile(File selectedFile) throws IOException {
        if (selectedFile.exists()) {
            final List<Direction> directionList = new ArrayList<>();
            Scanner scanner = new Scanner(selectedFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                for (Direction direction : Direction.values()) {
                    if (line.equals(direction.name())) {
                        directionList.add(direction);
                    }
                }

            }
            scanner.close();
            return directionList;

        }
        return emptyList();
    }
}
