package ch.zhaw.pm2.racetrack.textbased;

import ch.zhaw.pm2.racetrack.Config;

/**
 * This Class contains the main method to start the game.
 * No arguments are expected.
 *
 * @author weberph5
 * @version 1.0.0
 */
public class TerminalRaceTrack {

    /**
     * Starts the application
     *
     * @param args - not used
     */
    public static void main(String[] args) {
        final TextBasedGamePhaseHandler textBasedGame = new TextBasedGamePhaseHandler(new Config());
        textBasedGame.startGame();
    }
}
