package ch.zhaw.pm2.racetrack.textbased.file.selector;

import ch.zhaw.pm2.racetrack.Config;
import ch.zhaw.pm2.racetrack.game.Car;
import ch.zhaw.pm2.racetrack.game.path.follower.FollowerLoader;
import ch.zhaw.pm2.racetrack.game.util.PositionVector.Direction;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * The text based specific implementation of the follower file loader
 *
 * @author weberph5, abuechi
 * @version 1.0.0
 */
public class TerminalBasedFollowerFileSelector extends AbstractTextBasedFileSelector<List<Direction>> {
    private final Car car;

    /**
     * Initializing follower file selector with a config object specifying the directory of the follower files
     *
     * @param config the config object containing required information about the follower directory
     * @param car    the car that selected the Path Follower Strategy
     */
    public TerminalBasedFollowerFileSelector(Config config, Car car) {
        super(config);
        this.car = car;
    }

    @Override
    public File getFileDirectory() {
        return getConfig().getFollowerDirectory();
    }

    @Override
    public List<Direction> loadSelectedFile(File selectedFollowerFile) {
        try {
            return new FollowerLoader().loadFollowerFile(selectedFollowerFile, car);
        } catch (IOException e) {
            notifyUserOfSelectionFail();
            return selectFile();
        }
    }
}
