package com.comsystem.homework.rest;

/**
 * This exception is thrown when the number of days is not a non-negative value less than 61
 *
 * @see com.comsystem.homework.robot.RobotOperations#excavateStonesForDays(int)
 */
public class IllegalDaysException extends RuntimeException {
    public IllegalDaysException(int numberOfDays) {
        super("The number of days must be a non-negative value less than 61: " + numberOfDays);
    }
}
