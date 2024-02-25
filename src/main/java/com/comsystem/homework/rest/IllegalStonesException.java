package com.comsystem.homework.rest;


/**
 * This exception is thrown when the number of stones is a negative integer
 *
 * @see com.comsystem.homework.robot.RobotOperations#daysRequiredToCollectStones(int)
 */
public class IllegalStonesException extends RuntimeException {
    public IllegalStonesException(int numberOfStones) {
        super("The number of stones must be a non-negative integer: " + numberOfStones);
    }
}
