package ch.zhaw.pm2.racetrack.game.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Class containing the logic for the path calculator
 *
 * @author fupat002
 * @version 1.0.0
 */
public class PathCalculator {

    /**
     * Returns all the grid positions in the path between two positions, for use in determining line of sight.
     *
     * @param startLocation Starting position as a PositionVector
     * @param endLocation   Ending position as a PositionVector
     * @return The driving path as a List of PositionVector's, including the starting and ending positions.
     */
    //The Bresenham algorithm was copied unaltered from the "PM2 Projekt1 Anleitung".
    public List<PositionVector> calculatePath(PositionVector startLocation, PositionVector endLocation) {
        int startX = startLocation.getX();
        int startY = startLocation.getY();
        int endX = endLocation.getX();
        int endY = endLocation.getY();

        List<PositionVector> path = new ArrayList<>();
        int diffX = endX - startX;
        int diffY = endY - startY;
        int distX = Math.abs(diffX);
        int distY = Math.abs(diffY);
        int dirX = Integer.signum(diffX);
        int dirY = Integer.signum(diffY);
        int parallelStepX, parallelStepY;
        int diagonalStepX, diagonalStepY;
        int distanceSlowAxis, distanceFastAxis;
        //sets variables depending on the "fast" direction. The "fast" direction is the bigger/longer one.
        if (distX > distY) {
            parallelStepX = dirX;
            parallelStepY = 0;
            diagonalStepX = dirX;
            diagonalStepY = dirY;
            distanceSlowAxis = distY;
            distanceFastAxis = distX;
        } else {
            parallelStepX = 0;
            parallelStepY = dirY;
            diagonalStepX = dirX;
            diagonalStepY = dirY;
            distanceSlowAxis = distX;
            distanceFastAxis = distY;
        }
        int x = startX;
        int y = startY;
        int error = distanceFastAxis / 2;
        path.add(new PositionVector(x, y)); //add starting point to the path list
        for (int step = 0; step < distanceFastAxis; step++) {
            error -= distanceSlowAxis;
            if (error < 0) {
                error += distanceFastAxis;
                x += diagonalStepX;
                y += diagonalStepY;
            } else {
                x += parallelStepX;
                y += parallelStepY;
            }
            path.add(new PositionVector(x, y));//add every other point to the path list
        }
        return path;
    }
}
