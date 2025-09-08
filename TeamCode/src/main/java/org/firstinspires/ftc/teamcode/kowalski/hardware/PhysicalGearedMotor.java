package org.firstinspires.ftc.teamcode.kowalski.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * A motor + encoder class that works with degrees (or meters, see PhysicalServo.initDistanceMode) instead of abstract clicks.
 */
public class PhysicalGearedMotor {
    private DcMotor motor;

    private int ticksPerRevolution;
    private double metersPerDegree = -1;

    public static class TicksPerRevolution {
        public static int RevHDHexMotor = 28;
    }

    // TODO why doesn't it work manually

    /**
     * Create a <code>PhysicalGearedMotor</code> from a DcMotor.
     * @param from The robotcore.hardware.DcMotor to use
     * @param ratio The gear ratio of your motor, see <a href="#{@link}">{@link GearRatio}</a>
     * @param ticksPerRevolution How many encoder ticks occur per revolution.
     *                           You can find this in the encoder's documentation.
     */
    public PhysicalGearedMotor(DcMotor from, double ratio, int ticksPerRevolution) {
        motor = from;
        this.ticksPerRevolution = (int) ((double) ticksPerRevolution * ratio);

        this.motorInit();
        this.setPower(1.0);
    }

    private void motorInit() {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setTargetPosition(0);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    /**
     * Set whether or not the motor is in reversed mode.
     * @param isInverted Reversed?
     */
    public void setInvertedMode(boolean isInverted) {
        motor.setDirection(isInverted ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);
    }

    /**
     * Rotate a set amount of degrees, relative to the current motor position.
     * @param degrees The amount of degrees to rotate.
     */
    public void rotateBy(double degrees) {
        motor.setTargetPosition(motor.getCurrentPosition() + rotationToUnits(degrees));
    }

    /**
     * Rotate towards an amount of degrees.
     * Keep in mind that rotations aren't normalized, to rotate twice you can do <code>rotateTo(2 * 360)</code>
     * @param degrees The amount of degrees to rotate towards.
     */
    public void rotateTo(double degrees) {
        motor.setTargetPosition(rotationToUnits(degrees));
    }

    /**
     * Get the current rotation in degrees.
     * @return The rotation in degrees.
     */
    public double getRotation() {
        return unitsToRotation(motor.getCurrentPosition());
    }

    public double getRawTargetPosition() {
        return motor.getTargetPosition();
    }
    public double getRawCurPosition() {
        return motor.getCurrentPosition();
    }

    /**
     * Switch this motor into raw setPower-mode (disregarding target rotations)
     * @return Chainer
     */
    public PhysicalGearedMotor makeRaw() {
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        return this;
    }

    /**
     * Switch this motor into target rotation/position mode.
     * @return Chainer
     */
    public PhysicalGearedMotor makePhysical() {
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        return this;
    }

    /**
     * Reset the encoder to 0, setting the new start position of the motor to the current position.
     */
    public void resetEncoder() {
        this.motorInit();
    }

    /**
     * Set the power of the motor (how fast it rotates).
     * @param power The power, in the range [-1, 1]. Negative numbers invert the direction.
     */
    public void setPower(double power) {
        motor.setPower(power);
    }

    /**
     * Configure this PhysicalGearedMotor instance to be able to work with meters.
     * @param metersPerDegree The amount of meters the attached object travels per degree rotation (after gear ratio'ing)
     */
    public void initDistanceMode(double metersPerDegree) {
        this.metersPerDegree = metersPerDegree;
    }

    /**
     * Move the motor a set amount of meters. The distance mode must be initialized.
     * @param meters How many meters to move
     */
    public void moveBy(double meters) {
        if (this.metersPerDegree < 0) throw new IllegalStateException("PhysicalGearedMotor.moveTo was called but the distance mode wasn't initialized. (see initDistanceMode)");
        this.rotateBy(meters / this.metersPerDegree);
    }
    /**
     * Move the motor to a set amount of meters. The distance mode must be initialized.
     * @param meters What position to move to
     */
    public void moveTo(double meters) {
        if (this.metersPerDegree < 0) throw new IllegalStateException("PhysicalGearedmotor.moveTo was called but the distance mode wasn't initialized. (see initDistanceMode)");
        this.rotateTo(meters / this.metersPerDegree);
    }

    /**
     * Get the amount of meters the motor has moved from its initial position.
     * @return What position the motor is at
     */
    public double getPosition() {
        if (this.metersPerDegree < 0) throw new IllegalStateException("PhysicalGearedmotor.moveTo was called but the distance mode wasn't initialized. (see initDistanceMode)");
        return this.getRotation() * this.metersPerDegree;
    }

    /**
     * Get the power of the motor (how fast it rotates).
     * @return THe power, in the range [-1, 1].
     */
    public double getPower() {
        return motor.getPower();
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zpb) {
        motor.setZeroPowerBehavior(zpb);
    }

    private int rotationToUnits(double degrees) {
        return (int) (degrees / 360 * ticksPerRevolution);
    }
    private double unitsToRotation(int units) {
        return ((double) units / ticksPerRevolution) * 360;
    }
}
