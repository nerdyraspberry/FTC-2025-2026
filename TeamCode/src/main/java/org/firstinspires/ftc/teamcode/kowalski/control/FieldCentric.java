package org.firstinspires.ftc.teamcode.kowalski.control;

import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * A class which rotated field-centric motions into robot-centric motions which
 * may be fed into the MecanumDrive class
 */
public class FieldCentric {
    private final IMU imu;

    /**
     * Create an instance of the FieldCentric helper class
     * @param rimu The IMU to use in rotated motions.
     *             It must be initialized; this class does <i>not</i> do that for you.
     */
    public FieldCentric(IMU rimu) {
        this.imu = rimu;
    }

    /**
     * Rotate a given movement.
     * @param x X axis movement
     * @param y Y axis movement
     * @return The rotated movement, see also MecanumDrive.move
     */
    public double[] rotate(double x, double y) {
        double heading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        double rotatedX = x * Math.cos(-heading) - y * Math.sin(-heading);
        double rotatedY = x * Math.sin(-heading) + y * Math.cos(-heading);

        return new double[]{rotatedX, rotatedY};
    }
}
