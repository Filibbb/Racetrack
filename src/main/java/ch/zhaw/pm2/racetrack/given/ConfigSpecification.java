package ch.zhaw.pm2.racetrack.given;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This interface specifies stuff we use to test Racetrack for grading. It shall not be altered! <br/>
 * It defines how the Game can be configured.
 */
public interface ConfigSpecification {
    int MAX_CARS = 9;

    File getMoveDirectory();

    void setMoveDirectory(File moveDirectory);

    File getFollowerDirectory();

    void setFollowerDirectory(File followerDirectory);

    File getTrackDirectory();

    void setTrackDirectory(File trackDirectory);

    /**
     * Possible Move Strategies selected by the Console to configure the Cars. (This shall not be altered!)
     */
    public enum StrategyType {
        DO_NOT_MOVE, USER, MOVE_LIST, PATH_FOLLOWER, PATH_FINDER
    }

    /**
     * Possible space types of the grid. (This shall not be altered!)
     * The char value is used to parse from the track file and represents
     * the space type in the text representation created by toString().
     */
    public enum SpaceType {
        WALL('#'),
        TRACK(' '),
        FINISH_UP('^'),
        FINISH_DOWN('v'),
        FINISH_LEFT('<'),
        FINISH_RIGHT('>');

        private final char value;

        SpaceType(final char c) {
            value = c;
        }

        public char getValue() {
            return value;
        }

        /**
         * Tries to find a matching space type by value for a given char value and returns it.
         *
         * @param character the character that should match a spacetype.
         * @return the matching spacetype character by value : null if no value matched.
         */
        public static SpaceType getSpaceTypeByValue(char character) {
            for (SpaceType spaceType : SpaceType.values()) {
                if (spaceType.getValue() == character) {
                    return spaceType;
                }
            }
            return null;
        }

        /**
         * Collect all value representation and return them as a list.
         *
         * @return all available character representations
         */
        public static List<Character> getAllAvailableCharacterRepresentations() {
            final List<Character> availableValueRepresentations = new ArrayList<>();
            for (SpaceType spaceType : values()) {
                availableValueRepresentations.add(spaceType.getValue());
            }
            return availableValueRepresentations;
        }
    }
}
