package ch.zhaw.pm2.racetrack.game.track;

import ch.zhaw.pm2.racetrack.game.Car;
import ch.zhaw.pm2.racetrack.game.util.PositionVector;

import java.util.List;

import static ch.zhaw.pm2.racetrack.given.ConfigSpecification.SpaceType.*;

/**
 * This class checks if the car crossed the finish line correctly
 *
 * @author fupat002
 * @version 1.0.0
 */
public class FinishLineChecker {

    private static final int FIRST_INDEX = 0;

    private Track track;

    /**
     * Creates a new finish line checker object
     * @param track the current track
     */

    public FinishLineChecker(Track track) {
        this.track = track;
    }

    /**
     * Checks if the car crossed the finish line correctly.
     * If the car crosses the finish line incorrectly, it will be noted.
     * So the player can't go backwards across the finish line and forwards again to win the game.
     *
     * @param currentPlayer Car whose turn it is at the moment
     * @param path          the path it drives
     */
    public void checkFinishLineCrossings(Car currentPlayer, List<PositionVector> path) {
        for (PositionVector finishLinePoint : track.getFinishLineCoordinates()) {
            if (path.contains(finishLinePoint)) {
                if (isHorizontalFinishingLine()) {
                    carIsCrossingHorizontalFinishLine(currentPlayer, path);
                } else {
                    carIsCrossingVerticalFinishLine(currentPlayer, path);
                }
            }
        }
    }

    private boolean isHorizontalFinishingLine() {
        return track.getFinishLineSymbol().equals(FINISH_DOWN) || track.getFinishLineSymbol().equals(FINISH_UP);
    }

    private void carIsCrossingVerticalFinishLine(Car currentPlayer, List<PositionVector> path) {
        final PositionVector pointOfFinishLineCrossing = calculatePointOfFinishLinieCrossing(path);
        int xCoordinateFinishLine = track.getFinishLineXCoordinate();
        int xCoordinateOneBeforeCrossing = getPointBeforeCrossing(path, pointOfFinishLineCrossing).getX();
        boolean finishLineIsRight = track.getFinishLineSymbol().equals(FINISH_RIGHT);
        updateFinishLineCrossings(xCoordinateFinishLine, xCoordinateOneBeforeCrossing, finishLineIsRight, currentPlayer);
    }

    private void carIsCrossingHorizontalFinishLine(Car currentPlayer, List<PositionVector> path) {
        final PositionVector pointOfFinishLineCrossing = calculatePointOfFinishLinieCrossing(path);
        int yCoordinateFinishLine = track.getFinishLineYCoordinate();
        int yCoordinateOneBeforeCrossing = getPointBeforeCrossing(path, pointOfFinishLineCrossing).getY();
        boolean finishLineIsDown = track.getFinishLineSymbol().equals(FINISH_DOWN);
        updateFinishLineCrossings(yCoordinateFinishLine, yCoordinateOneBeforeCrossing, finishLineIsDown, currentPlayer);
    }

    private void updateFinishLineCrossings(int coordinateFinishLine, int coordinateOneBeforeCrossing, boolean hasCorrectFinishLineOrientation, Car currentPlayer) {
        if (hasPlayerCrossedFinishLineCorrectly(coordinateFinishLine, coordinateOneBeforeCrossing, hasCorrectFinishLineOrientation)) {
            currentPlayer.setFinishLineCrossings(currentPlayer.getFinishLineCrossings() + 1);
        } else {
            currentPlayer.setFinishLineCrossings(currentPlayer.getFinishLineCrossings() - 1);
        }
    }

    private PositionVector getPointBeforeCrossing(List<PositionVector> path, PositionVector pointOfFinishLineCrossing) {
        final int indexOfFinishLineCrossingPoint = path.indexOf(pointOfFinishLineCrossing);
        if (indexOfFinishLineCrossingPoint == FIRST_INDEX) {
            return path.get(indexOfFinishLineCrossingPoint);
        } else {
            return path.get(indexOfFinishLineCrossingPoint - 1);
        }
    }

    private boolean hasPlayerCrossedFinishLineCorrectly(int finishLineCoordinate, int coordinateOneBeforeCrossing, boolean hasCorrectFinishLineOrientation) {
        return (finishLineCoordinate > coordinateOneBeforeCrossing && hasCorrectFinishLineOrientation) || (finishLineCoordinate < coordinateOneBeforeCrossing && !hasCorrectFinishLineOrientation);
    }

    private PositionVector calculatePointOfFinishLinieCrossing(List<PositionVector> path) {
        for (PositionVector finishLinePoint : track.getFinishLineCoordinates()) {
            if (path.contains(finishLinePoint)) {
                return finishLinePoint;
            }
        }
        return null;
    }
}
