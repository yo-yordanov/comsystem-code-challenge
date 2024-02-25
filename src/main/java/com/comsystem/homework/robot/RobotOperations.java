package com.comsystem.homework.robot;


import com.comsystem.homework.model.RobotAction;
import com.comsystem.homework.model.RobotPlan;
import com.comsystem.homework.rest.IllegalDaysException;
import com.comsystem.homework.rest.IllegalStonesException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class contains the algorithms that are used to calculate the action plans for the robot.
 *
 * @author Yordan Yordanov
 * @see RobotPlan
 */
public class RobotOperations {
    /**
     * The number of positive bits in an integer to left rotate 1 to get the number of stones that the robot can dig in a
     * single day.
     */
    private final static int INT_ROTATE_BITS = 30;

    /**
     * An algorithm that converts a number of days into an action plan.
     * <p>
     * The algorithm is designed to calculate the maximum number of stones that the robot can dig in the provided number
     * of days.
     *
     * @param days the number of days that the robot can work. The number of days must be a non-negative value
     *             less than 61
     * @return The action plan <em>must maximize</em> the number of stones that the robot will dig. In other words, this
     * algorithm must try to achieve the highest value of {@link RobotPlan#numberOfStones} possible for the
     * number of provided days. The value of {@link RobotPlan#numberOfDays} is equal to the input
     * days parameter
     * @see RobotPlan
     */
    public RobotPlan excavateStonesForDays(int days) {
        // The number of days must be a non-negative integer, the maximum number is limited by the integer type
        if (days < 0 || days > (2 * INT_ROTATE_BITS + 1)) {
            throw new IllegalDaysException(days);
        }

        // Edge case: if the robot has 0 days, it can't dig any stones
        if (days == 0) {
            return new RobotPlan(0, 0, new ArrayList<>());
        }

        List<RobotAction> robotActions = new ArrayList<>();
        int numberOfStones = 0;

        // The robot can only clone itself 30 times, because an integer can only hold 31 bits
        for (int i = 0; i < Math.min(days - 1, INT_ROTATE_BITS); i++) {
            robotActions.add(RobotAction.CLONE);
        }

        // At least one dig is required
        robotActions.add(RobotAction.DIG);
        numberOfStones += (1 << Math.min(days - 1, INT_ROTATE_BITS));

        // If there are more days, the robot can dig more
        for (int i = days - INT_ROTATE_BITS - 1, j = INT_ROTATE_BITS - 1; i > 0; i--, j--) {
            robotActions.add(j, RobotAction.DIG);
            numberOfStones += (1 << j);
        }

        return new RobotPlan(days, numberOfStones, robotActions);
    }

    /**
     * An algorithm that converts a number of stones into an action plan. Essentially this algorithm is the inverse of
     * {@link #excavateStonesForDays(int)}.
     * <p>
     * The algorithm is designed to calculate the minimum number of days required to collect the provided number of
     * stones or more (not exactly the provided number of stones).
     *
     * @param numberOfStones the number of stones the robot has to collect. The number of stones must be a non-negative
     *                       integer
     * @return The action plan <em>must minimize</em> the number of days necessary for the robot to dig the
     * provided number of stones. In other words, this algorithm must try to achieve the lowest value of
     * {@link RobotPlan#numberOfDays} possible for the number of provided stones. The value of
     * {@link RobotPlan#numberOfStones} is equal to the numberOfStones parameter
     * @see RobotPlan
     */
    public RobotPlan daysRequiredToCollectStones(int numberOfStones) {
        // The number of stones must be a non-negative integer, the maximum number is limited by the integer type
        if (numberOfStones < 0) {
            throw new IllegalStonesException(numberOfStones);
        }

        List<RobotAction> robotActions = new ArrayList<>();
        int days = 0;
        int originalNumberOfStones = numberOfStones;

        // The robot operations are generated in reverse order
        // The robot can only clone itself 30 times, because an integer can only hold 31 bits
        for (int i = INT_ROTATE_BITS; i >= 0; i--) {

            // If the robot has already dug at least once, it has to clone itself every next day
            if (!robotActions.isEmpty()) {
                robotActions.add(RobotAction.CLONE);
                days++;
            }

            // If the remaining number of stones is less than or equal to the number of stones that the robot can dig
            // on the next day, it can skip the current day to save time
            if (numberOfStones <= (1 << i - 1)) {
                continue;
            }

            // Otherwise, the robot has to dig and the number of stones is reduced
            if (numberOfStones > 0) {
                robotActions.add(RobotAction.DIG);
                numberOfStones -= (1 << i);
                days++;
            }
        }

        // The robot operations are generated in reverse order, so they have to be reversed
        Collections.reverse(robotActions);

        return new RobotPlan(days, originalNumberOfStones, robotActions);
    }

}
