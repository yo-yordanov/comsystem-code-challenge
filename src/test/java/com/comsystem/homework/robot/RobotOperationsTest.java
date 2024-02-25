package com.comsystem.homework.robot;

import com.comsystem.homework.model.RobotAction;
import com.comsystem.homework.model.RobotPlan;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains the tests for the {@link RobotOperations} class
 *
 * @author Yordan Yordanov
 * @see RobotOperations
 */
class RobotOperationsTest {

    private final RobotOperations RobotOperations = new RobotOperations();

    /**
     * Test the correctness of the {@link RobotOperations#excavateStonesForDays(int)} method
     */
    @Test
    void testExcavateStonesForDays() {
        // Test with minimum number of days
        RobotPlan plan1 = RobotOperations.excavateStonesForDays(0);
        assertEquals(0, plan1.numberOfStones()); // Expected number of stones for 0 days

        // Test with maximum number of days
        RobotPlan plan2 = RobotOperations.excavateStonesForDays(61);
        assertEquals(2147483647, plan2.numberOfStones()); // Expected number of stones for 61 days

        // Test with random number of days
        Random random = new Random();
        RobotPlan plan3 = RobotOperations.excavateStonesForDays(random.nextInt(61));

        int collectedStones = accumulateStones(plan3);

        assertEquals(plan3.numberOfStones(), collectedStones);
    }

    /**
     * Test the correctness of the {@link RobotOperations#daysRequiredToCollectStones(int)} method
     */
    @Test
    void testDaysRequiredToCollectStones() {
        // Test with minimum number of stones
        RobotPlan plan1 = RobotOperations.daysRequiredToCollectStones(0);
        assertEquals(0, plan1.numberOfDays()); // Expected number of days for 0 stones

        // Test with maximum number of stones
        RobotPlan plan2 = RobotOperations.daysRequiredToCollectStones(2147483647);
        assertEquals(61, plan2.numberOfDays()); // Expected number of days for 2147483647 stones

        // Test with random number of stones
        Random random = new Random();
        RobotPlan plan3 = RobotOperations.daysRequiredToCollectStones(random.nextInt(2147483647));

        int collectedStones = accumulateStones(plan3);

        assertTrue(collectedStones >= plan3.numberOfStones());
    }

    /**
     * Helper method to calculate the number of stones collected by a robot plan by iterating through the actions
     *
     * @param plan the robot plan
     * @return the number of stones collected by the robot plan
     */
    private int accumulateStones(RobotPlan plan) {
        int stones = 0;
        int cloned = 0;
        for (RobotAction action : plan.robotActions()) {
            if (action == RobotAction.CLONE) {
                cloned++;
            }
            if (action == RobotAction.DIG) {
                stones += (1 << cloned);
            }
        }
        return stones;
    }

    /**
     * Test the correctness of both methods by comparing their results
     */
    @Test
    void testCompareExcavationAndApproximation() {
        Random random = new Random();

        // Test with random number of days
        int randomNumber1 = random.nextInt(62);

        RobotPlan plan1excavation = RobotOperations.excavateStonesForDays(randomNumber1);
        RobotPlan plan1approximation = RobotOperations.daysRequiredToCollectStones(plan1excavation.numberOfStones());

        assertEquals(randomNumber1, plan1approximation.numberOfDays());

        // Test with random number of stones
        int randomNumber2 = random.nextInt(2147483647);

        RobotPlan plan2approximation = RobotOperations.daysRequiredToCollectStones(randomNumber2);
        RobotPlan plan2excavation = RobotOperations.excavateStonesForDays(plan2approximation.numberOfDays());

        assertTrue(randomNumber2 <= plan2excavation.numberOfStones());
    }
}