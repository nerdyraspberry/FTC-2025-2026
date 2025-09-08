package org.firstinspires.ftc.teamcode.kowalski.hardware;

/**
 * An angle range for the <a href="#{@link}">{@link PhysicalServo}</a>.
 */
public class AngleRange {
    public double fromDegrees;
    public double toDegrees;

    /** The default range of a REV servo */
    public static AngleRange RevServoDefaults = new AngleRange(0, 270);

    /**
     * Create a range of angles
     * @param from The start angle, in degrees. This corresponds to <code>0.0</code> in servo units.
     * @param to The end angle, in degrees. This corresponds to <code>1.0</code> in servo units.
     */
    public AngleRange(int from, int to) {
        this.fromDegrees = from;
        this.toDegrees = to;
    }

    /**
     * <b>This function is used only by PhysicalServo, it is normally not necessary to call it yourself.</b>
     * @param degrees The amount of degrees to convert to units, range <code>[from, to]</code>
     * @return The amount of units, range <code>[0, 1]</code>
     */
    public double degreesToUnits(double degrees) {
        return (degrees - this.fromDegrees) / (this.toDegrees - this.fromDegrees);
    }

    /**
     * <b>This function is used only by PhysicalServo, it is normally not necessary to call it yourself.</b>
     * @param units The amount of units to convert to degrees, range <code>[0.0, 1.0]</code>
     * @return The amount of degrees, range <code>[from, to]</code>
     */
    public double unitsToDegrees(double units) {
        return (units * (this.toDegrees - this.fromDegrees)) + this.fromDegrees;
    }
}