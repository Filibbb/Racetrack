package ch.zhaw.pm2.racetrack.game.path.follower;

import ch.zhaw.pm2.racetrack.game.Car;
import ch.zhaw.pm2.racetrack.game.util.PositionVector;
import ch.zhaw.pm2.racetrack.game.util.PositionVector.Direction;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static ch.zhaw.pm2.racetrack.game.util.PositionVector.*;
import static ch.zhaw.pm2.racetrack.game.util.PositionVector.Direction.*;
import static ch.zhaw.pm2.racetrack.game.util.PositionVector.subtract;

/**
 * Class to load a follower file and make it usable for the follower strategy.
 *
 * @author weberph5
 * @version 1.0.0
 */
public class FollowerLoader {
    private static final int X_COORDINATES_INDEX = 0;
    private static final int Y_COORDINATE_INDEX = 1;
    private final ArrayList<PositionVector> positionVectorList = new ArrayList<>();
    private final ArrayList<Direction> directionsList = new ArrayList<>();

    /**
     * Loads the previously selected follower file and passes it into conversion.
     *
     * @param selectedFile the selected file
     * @return a list of directions as instructions for the car with this strategy
     * @throws IOException if reading file did not work correctly or file did not exist at this spot exception cannot be handled (closing of streams are ensured) and therefore are thrown to the caller
     */
    public List<Direction> loadFollowerFile(File selectedFile, Car car) throws IOException {
        createPositionVectorList(selectedFile, car);
        return directionsList;
    }

    private void createPositionVectorList(File selectedFile, Car car) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(selectedFile)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String position = line.replaceAll("[^0-9,]", "");
                String[] readCoordinates = position.split(",");
                int vectorX = Integer.parseInt(readCoordinates[X_COORDINATES_INDEX]);
                int vectorY = Integer.parseInt(readCoordinates[Y_COORDINATE_INDEX]);
                positionVectorList.add(new PositionVector(vectorX, vectorY));
            }
        }
        positionVectorList.add(0, new PositionVector(car.getPosition()));


        convertPositionsToDirections(car);
    }

    private void convertPositionsToDirections(Car car) {
        PositionVector currentCarVelocity = new PositionVector(0, 0);
        PositionVector currentPosition = car.getPosition();
        int index = 0;
        PositionVector nextPosition = positionVectorList.get(1);
        for (PositionVector positionVector : positionVectorList) {
            currentPosition = positionVector;
            int nextIndex = index + 1;
            if (currentPosition == nextPosition){
                positionVectorList.remove(index);
            }
            if (nextIndex < positionVectorList.size()) {
                nextPosition = positionVectorList.get(nextIndex);
            } else {
                nextPosition = positionVectorList.get(0);
            }
            PositionVector distance = subtract(nextPosition, currentPosition);
            PositionVector requiredVelocityToReachTargetDistance = subtract(distance, currentCarVelocity);
            Direction direction = switchDirection(requiredVelocityToReachTargetDistance);
            currentCarVelocity = add(direction.getVector(), currentCarVelocity);
            currentPosition = add(currentPosition, direction.getVector());
            positionVectorList.add(index+1,currentPosition);
            directionsList.add(direction);
        }
    }

    private Direction switchDirection(PositionVector position) {
        if (position.getX() == 0 && position.getY() >= 1) return DOWN;
        if (position.getX() == 0 && position.getY() <= -1) return UP;
        if (position.getX() <= -1 && position.getY() == 0) return LEFT;
        if (position.getX() >= 1 && position.getY() == 0) return RIGHT;
        if (position.getX() == 0 && position.getY() == 0) return NONE;

        if (position.getX() <= -1 && position.getY() >= 1) return DOWN_LEFT;
        if (position.getX() >= 1 && position.getY() >= 1) return DOWN_RIGHT;
        if (position.getX() <= -1 && position.getY() <= -1) return UP_LEFT;
        if (position.getX() >= 1 && position.getY() <= -1) return UP_RIGHT;
        else return NONE;
    }

}
