package org.firstinspires.ftc.teamcode.kowalski.hardware;

import com.qualcomm.robotcore.hardware.Servo;

/**
 A servo class that works on physical angles instead of abstract units.
 */
public class PhysicalServo {
    private final Servo servo;

    public AngleRange rangeMapping;

    /**
     * Create a <code>PhysicalServo</code> from a Servo.
     * This uses a degree range of 0 - 270, which is the default of the often-used REV servos.
     * @param from The robotcore.hardware.Servo to use.
     */
    public PhysicalServo(Servo from) {
        servo = from;
        rangeMapping = AngleRange.RevServoDefaults;
    }

    /**
     * Create a <code>PhysicalServo</code> from a Servo with a specific range.
     * See <a href="#{@link}">{@link AngleRange}</a> for the range definition
     * @param from The robotcore.hardware.Servo to use.
     * @param mapping The degrees that this servo runs on.
     */
    public PhysicalServo(Servo from, AngleRange mapping) {
        servo = from;
        rangeMapping = mapping;
    }

    /**
     * Set the rotation of the servo.
     * @param rotation A rotation in degrees.
     */
    public void setRotation(double rotation) {
        servo.setPosition(rangeMapping.degreesToUnits(rotation));
    }

    /**
     * Get the rotation of the servo.
     * @return A rotation in degrees.
     */
    public double getRotation() {
        return rangeMapping.unitsToDegrees(servo.getPosition());
    }
}
