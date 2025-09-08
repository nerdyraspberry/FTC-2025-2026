package org.firstinspires.ftc.teamcode.kowalski.hardware;

public class GearRatio {
    /**
     * Calculate a multiplication factor from a gear ratio like <code>x:y</code>
     * Example:
     * <code>60:1 => GearRatio.of(60, 1)</code>
     * @param x
     * @param y
     * @return The multiplication factor.
     */
    // note: a gear ratio of x : y that for the output to rotate y times the input must rotate x times.
    public static double of(int x, int y) {
        return (double)x / (double)y;
    }

    /** A gear ratio of 1 : 1 */
    public static double OneToOne = GearRatio.of(1, 1);
}
