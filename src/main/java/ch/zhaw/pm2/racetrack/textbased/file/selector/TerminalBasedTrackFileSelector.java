package ch.zhaw.pm2.racetrack.textbased.file.selector;

import ch.zhaw.pm2.racetrack.Config;
import ch.zhaw.pm2.racetrack.game.track.Track;
import ch.zhaw.pm2.racetrack.game.track.TrackLoader;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * The text based specific implementation of the track file loader
 *
 * @author abuechi
 * @version 1.0.0
 */
public class TerminalBasedTrackFileSelector extends AbstractTextBasedFileSelector<Track> {
    /**
     * Initializing track file selector with a config object specifying the directory of the track files
     *
     * @param config the config object containing required information about the track directory
     */
    public TerminalBasedTrackFileSelector(Config config) {
        super(config);
    }

    @Override
    public File getFileDirectory() {
        return getConfig().getTrackDirectory();
    }

    @Override
    public Track loadSelectedFile(File selectedTrackFile) {
        try {
            final TrackLoader trackloader = new TrackLoader();
            final List<String> trackFileRows = trackloader.loadTrackFile(selectedTrackFile);
            if (trackloader.isTrackFileValid(trackFileRows)) {
                return new Track(trackFileRows);
            } else {
                notifyUserOfSelectionFail();
                selectFile();
            }
        } catch (IOException e) {
            notifyUserOfSelectionFail();
            selectFile();
        }
        return selectFile();
    }
}
