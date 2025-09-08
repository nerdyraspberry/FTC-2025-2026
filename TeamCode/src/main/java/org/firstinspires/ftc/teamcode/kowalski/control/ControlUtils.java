package org.firstinspires.ftc.teamcode.kowalski.control;

import java.util.Arrays;

public class ControlUtils {
    public static double treshold = 0.1;

    private static double boolToDouble(boolean bool) {
        return bool ? 1 : 0;
    }

    /**
     * Use two discrete controller inputs as one joystick axis.
     * @param negative The discrete input for the negative direction, range [0, 1].
     * @param positive The discrete input for the positive direction, range [0, 1].
     * @return The joystick-style input, range [-1, 1]
     */
    public static double discreteAxis(double negative, double positive) {
        return positive - negative;
    }
    /**
     * Use two discrete controller inputs as one joystick axis.
     * @param negative The discrete input for the negative direction, `true` or `false`.
     * @param positive The discrete input for the positive direction, `true` or `false`.
     * @return The joystick-style input, range [-1, 1]
     */
    public static double discreteAxis(boolean negative, boolean positive) {
        return ControlUtils.boolToDouble(positive) - ControlUtils.boolToDouble(negative);
    }

    /**
     * Check if every input matches the input treshold.
     * This allows to only run a code branch if input is actively being pressed.
     * @param inputs Variable args, the inputs (only double inputs are supported)/
     * @return A boolean, if true: every input meets treshold.
     */
    public static boolean withTreshold(double... inputs) {
        return Arrays.stream(inputs).mapToObj(k -> k >= treshold).reduce(true, (a, o) -> a && o);
    }
}
