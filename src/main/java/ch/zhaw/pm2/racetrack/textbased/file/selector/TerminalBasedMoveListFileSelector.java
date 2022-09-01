package ch.zhaw.pm2.racetrack.textbased.file.selector;

import ch.zhaw.pm2.racetrack.Config;
import ch.zhaw.pm2.racetrack.game.movelist.MoveListLoader;
import ch.zhaw.pm2.racetrack.game.util.PositionVector.Direction;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * The text based specific implementation of the move list file loader
 *
 * @author weberph5
 * @version 1.0.0
 */
public class TerminalBasedMoveListFileSelector extends AbstractTextBasedFileSelector<List<Direction>> {

    /**
     * Initializing move list file selector with a config object specifying the directory of the follower files
     *
     * @param config the config object containing required information about the follower directory
     */
    public TerminalBasedMoveListFileSelector(Config config) {
        super(config);
    }

    @Override
    public File getFileDirectory() {
        return getConfig().getMoveDirectory();
    }

    @Override
    public List<Direction> loadSelectedFile(File selectedMoveListFile) {
        try {
            return new MoveListLoader().loadMoveListFile(selectedMoveListFile);
        } catch (IOException e) {
            notifyUserOfSelectionFail();
            return selectFile();
        }
    }
}
