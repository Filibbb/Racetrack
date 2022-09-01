package ch.zhaw.pm2.racetrack.game.track;

import ch.zhaw.pm2.racetrack.Config;
import ch.zhaw.pm2.racetrack.game.Car;
import ch.zhaw.pm2.racetrack.game.util.PositionVector;
import ch.zhaw.pm2.racetrack.given.ConfigSpecification.SpaceType;
import ch.zhaw.pm2.racetrack.given.TrackSpecification;

import java.util.ArrayList;
import java.util.List;

import static ch.zhaw.pm2.racetrack.given.ConfigSpecification.SpaceType.*;

/**
 * This class represents the racetrack board.
 *
 * <p>The racetrack board consists of a rectangular grid of 'width' columns and 'height' rows.
 * The zero point of he grid is at the top left. The x-axis points to the right and the y-axis points downwards.</p>
 * <p>Positions on the track grid are specified using {@link PositionVector} objects. These are vectors containing an
 * x/y coordinate pair, pointing from the zero-point (top-left) to the addressed space in the grid.</p>
 *
 * <p>Each position in the grid represents a space which can hold an enum object of type {@link SpaceType}.<br>
 * Possible Space types are:
 * <ul>
 *  <li>WALL : road boundary or off track space</li>
 *  <li>TRACK: road or open track space</li>
 *  <li>FINISH_LEFT, FINISH_RIGHT, FINISH_UP, FINISH_DOWN :  finish line spaces which have to be crossed
 *      in the indicated direction to winn the race.</li>
 * </ul>
 * <p>Beside the board the track contains the list of cars, with their current state (position, velocity, crashed,...)</p>
 *
 * <p>At initialization the track grid data is read from the given track file. The track data must be a
 * rectangular block of text. Empty lines at the start are ignored. Processing stops at the first empty line
 * following a non-empty line, or at the end of the file.</p>
 * <p>Characters in the line represent SpaceTypes. The mapping of the Characters is as follows:</p>
 * <ul>
 *   <li>WALL : '#'</li>
 *   <li>TRACK: ' '</li>
 *   <li>FINISH_LEFT : '&lt;'</li>
 *   <li>FINISH_RIGHT: '&gt;'</li>
 *   <li>FINISH_UP   : '^;'</li>
 *   <li>FINISH_DOWN: 'v'</li>
 *   <li>Any other character indicates the starting position of a car.<br>
 *       The character acts as the id for the car and must be unique.<br>
 *       There are 1 to {@link Config#MAX_CARS} allowed. </li>
 * </ul>
 *
 * <p>All lines must have the same length, used to initialize the grid width).
 * Beginning empty lines are skipped.
 * The tracks ends with the first empty line or the file end.<br>
 * An file is invalid if
 * <ul>
 *   <li>not all track lines have the same length</li>
 *   <li>the file contains no track lines (grid height is 0)</li>
 *   <li>the file contains more than {@link Config#MAX_CARS} cars</li>
 * </ul>
 * <p>
 * And the user will be asked to retry the specific operation.
 * <p>The Track can return a String representing the current state of the race (including car positons)</p>
 */
public class Track implements TrackSpecification {

    private static final int FIRST_FINISH_LINE_INDEX = 0;
    private static final char CRASH_INDICATOR = 'X';
    private final List<PositionVector> finishLineCoordinates;
    private final List<Car> carsOnTrack;
    private final List<List<SpaceType>> raceTrack;

    /**
     * Initialize a Track from the given track file.
     *
     * @param trackFileRows Reference to a file containing the track data
     */
    public Track(final List<String> trackFileRows) {
        finishLineCoordinates = new ArrayList<>();
        carsOnTrack = new ArrayList<>();
        raceTrack = new ArrayList<>();
        createRaceTrack(trackFileRows);
    }

    private void createRaceTrack(List<String> trackFileRows) {
        for (String row : trackFileRows) {
            addGridRowToRaceTrack(row);
        }
    }

    /**
     * Return the type of space at the given position.
     * If the location is outside the track bounds, it is considered a wall.
     *
     * @param position The coordinates of the position to examine
     * @return The type of track position at the given location
     */
    @Override
    public SpaceType getSpaceType(PositionVector position) {
        return raceTrack.get(position.getY()).get(position.getX());
    }

    /**
     * Return the number of cars.
     *
     * @return Number of cars
     */
    @Override
    public int getCarCount() {
        return carsOnTrack.size();
    }

    /**
     * Get instance of specified car.
     *
     * @param carIndex The zero-based carIndex number
     * @return The car instance at the given index
     */
    @Override
    public Car getCar(int carIndex) {
        return carsOnTrack.get(carIndex);
    }

    /**
     * Get the id of the specified car.
     *
     * @param carIndex The zero-based carIndex number
     * @return A char containing the id of the car
     */
    @Override
    public char getCarId(int carIndex) {
        return carsOnTrack.get(carIndex).getId();
    }

    /**
     * Get the position of the specified car.
     *
     * @param carIndex The zero-based carIndex number
     * @return A PositionVector containing the car's current position
     */
    @Override
    public PositionVector getCarPos(int carIndex) {
        return carsOnTrack.get(carIndex).getPosition();
    }

    /**
     * Get the velocity of the specified car.
     *
     * @param carIndex The zero-based carIndex number
     * @return A PositionVector containing the car's current velocity
     */
    @Override
    public PositionVector getCarVelocity(int carIndex) {
        return carsOnTrack.get(carIndex).getVelocity();
    }

    /**
     * Gets character at the given position.
     * If there is a crashed car at the position, {@link #CRASH_INDICATOR} is returned.
     *
     * @param y            position Y-value
     * @param x            position X-vlaue
     * @param currentSpace char to return if no car is at position (x,y)
     * @return character representing position (x,y) on the track
     */
    @Override
    public char getCharAtPosition(int y, int x, SpaceType currentSpace) {
        for (Car car : carsOnTrack) {
            if (car.getPosition().equals(new PositionVector(x, y))) {
                return car.isCrashed() ? CRASH_INDICATOR : car.getId();
            }
        }
        return currentSpace.getValue();
    }

    /**
     * Return a String representation of the track, including the car locations.
     *
     * @return A String representation of the track
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        final List<List<Character>> printableRaceTrackRows = getCurrentTrackRepresentation();
        for (List<Character> columnCharacters : printableRaceTrackRows) {
            for (Character character : columnCharacters) {
                stringBuilder.append(character.toString());
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    private List<List<Character>> getCurrentTrackRepresentation() {
        final List<List<Character>> trackRepresentation = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < raceTrack.size(); rowIndex++) {
            trackRepresentation.add(getCurrentRowRepresentation(rowIndex));
        }
        return trackRepresentation;
    }

    private List<Character> getCurrentRowRepresentation(final int rowIndex) {
        final List<SpaceType> currentRaceTrackRow = raceTrack.get(rowIndex);
        final List<Character> row = new ArrayList<>();
        for (int columnIndex = 0; columnIndex < currentRaceTrackRow.size(); columnIndex++) {
            row.add(getCharAtPosition(rowIndex, columnIndex, currentRaceTrackRow.get(columnIndex)));
        }
        return row;
    }

    private void addGridRowToRaceTrack(String row) {
        final List<SpaceType> gridRow = new ArrayList<>();
        for (char symbol : row.toCharArray()) {
            gridRow.add(getSpaceTypeForSymbol(gridRow.size(), symbol));
        }
        raceTrack.add(gridRow);
    }

    private SpaceType getSpaceTypeForSymbol(final int currentRowSize, char symbol) {
        final SpaceType spaceType = getSpaceTypeByValue(symbol);
        if (spaceType != null) {
            if (isSpaceTypePartOfFinishLine(spaceType)) {
                finishLineCoordinates.add(new PositionVector(currentRowSize, raceTrack.size()));
            }
            return spaceType;
        } else {
            addCarToTheRace(symbol, new PositionVector(currentRowSize, raceTrack.size()));
            return TRACK;
        }
    }

    private boolean isSpaceTypePartOfFinishLine(final SpaceType spaceType) {
        return spaceType.equals(FINISH_DOWN) || spaceType.equals(FINISH_UP) || spaceType.equals(FINISH_RIGHT) || spaceType.equals(FINISH_LEFT);
    }

    /**
     * Adds a car with a unique car ID to the race at the specified position.
     *
     * @param carID            Car identifier used to represent the car on the track
     * @param startingPosition The start position
     */
    public void addCarToTheRace(char carID, PositionVector startingPosition) {
        carsOnTrack.add(new Car(carID, startingPosition));
    }

    public List<Car> getCarsOnTrack() {
        return carsOnTrack;
    }

    public int getFinishLineXCoordinate() {
        return finishLineCoordinates.get(FIRST_FINISH_LINE_INDEX).getX();
    }

    public int getFinishLineYCoordinate() {
        return finishLineCoordinates.get(FIRST_FINISH_LINE_INDEX).getY();
    }

    public List<PositionVector> getFinishLineCoordinates() {
        return finishLineCoordinates;
    }

    public SpaceType getFinishLineSymbol() {
        return getSpaceType(finishLineCoordinates.get(FIRST_FINISH_LINE_INDEX));
    }
}
