package ch.zhaw.pm2.racetrack.helpers;

import java.util.ArrayList;
import java.util.List;

/**
 * A test utility to simplify loading tracks and testing tracks
 */
public class TestTrackCreationUtil {
    /**
     * Creates the Rows of challenge.txt Track as Stings and feeds them into a List which then will be returned.
     *
     * @return List of Rows to build a Track.
     */
    public static List<String> createChallengeTrack() {
        List<String> trackRows = new ArrayList<>();
        trackRows.add("###############################################################");
        trackRows.add("#################                                 #############");
        trackRows.add("###############                                     ###########");
        trackRows.add("###############                                       #########");
        trackRows.add("###############         ##################            #########");
        trackRows.add("###############        ####################           #########");
        trackRows.add("##############        #####################           #########");
        trackRows.add("############        ######################           ##########");
        trackRows.add("#########         ######################           ############");
        trackRows.add("#########       ######################           ##############");
        trackRows.add("#########      #####################           ################");
        trackRows.add("#########        #################           ##################");
        trackRows.add("#########         ################           ##################");
        trackRows.add("##########         ##################           ###############");
        trackRows.add("###########         ####################          #############");
        trackRows.add("###########         #######################          ##########");
        trackRows.add("##########         ##########################         #########");
        trackRows.add("#########         ############################         ########");
        trackRows.add("########         #############################         ########");
        trackRows.add("#######         ##############################         ########");
        trackRows.add("######         #############################           ########");
        trackRows.add("######         ############################           #########");
        trackRows.add("######                > a                           ###########");
        trackRows.add("######                >                          ##############");
        trackRows.add("########              > b                     #################");
        trackRows.add("###############################################################");
        return trackRows;
    }

    /**
     * Creates the Rows of oval-clock-up.txt Track as Stings and feeds them into a List which then will be returned.
     * @return List of Rows to build a Track.
     */
    public static List<String> createOvalClockTrack() {
        List<String> trackRows = new ArrayList<>();
        trackRows.add("##################################################");
        trackRows.add("##################################################");
        trackRows.add("##############                       #############");
        trackRows.add("##########                              ##########");
        trackRows.add("#######                                    #######");
        trackRows.add("######  a   b   #################           ######");
        trackRows.add("#####^^^^^^^^^^###################           #####");
        trackRows.add("#####          ###################           #####");
        trackRows.add("######          #################           ######");
        trackRows.add("#######                                    #######");
        trackRows.add("##########                              ##########");
        trackRows.add("##############                      ##############");
        trackRows.add("##################################################");
        trackRows.add("##################################################");
        return trackRows;
    }

    public static List<String> createHorizontalTrack() {
        List<String> trackRows = new ArrayList<>();
        trackRows.add("##############################");
        trackRows.add("#a                         > #");
        trackRows.add("#b                         > #");
        trackRows.add("##############################");
        return trackRows;
    }

}
