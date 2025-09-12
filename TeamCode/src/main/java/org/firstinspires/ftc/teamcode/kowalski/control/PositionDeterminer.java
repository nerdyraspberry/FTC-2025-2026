package org.firstinspires.ftc.teamcode.kowalski.control;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.SharedThingemajigs;

public class PositionDeterminer {
    private final IMU imu;

    public PositionDeterminer(HardwareMap hardwareMap) {
        IMU.Parameters imuParams =
                new IMU.Parameters(new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.BACKWARD,
                        RevHubOrientationOnRobot.UsbFacingDirection.UP
                ));

        this.imu = hardwareMap.get(IMU.class, "imu");
        this.imu.initialize(imuParams);

    }

    /** Reset everything! */
    public void initialize() {
        this.imu.resetYaw();
    }

    YawPitchRollAngles lastYpra;

    /** Get the angle that the robot is currently pointing in.
     * If the IMU does not return a valid angle, it will retry, incurring ~1/4th of a second
     * of delay (to give it time to possibly come unstuck or whatever, I don't know if this'll
     * work in the first place). If that still won't work, it might return data that's less than a
     * second old, which is approximately correct (kinda). If *that* still doesn't work,
     * it returns 0. Jank, jank galore. Here be dragons...
     * @return The angle.
     */
    public double getAngle() {
        boolean imuFailed = false;
        YawPitchRollAngles ypra = this.imu.getRobotYawPitchRollAngles();

        if (ypra.getAcquisitionTime() == 0) {
            // The IMU has failed, retry (max) two times...
            imuFailed = true;
            for (int i = 0; i < 2 && imuFailed; i++) {
                SharedThingemajigs.sleep(250);
                ypra = this.imu.getRobotYawPitchRollAngles();

                imuFailed = ypra.getAcquisitionTime() == 0;
            }
        }
        if (imuFailed) {
            // That correction still didn't do anything; check if the last measurement
            // is recent enough to be considered "accurate" (for certain -- incorrect -- definitions
            // of "accurate"), and just yeet that if that's the case.
            if (System.nanoTime() - lastYpra.getAcquisitionTime() < 1000) {
                return 180.0 + -lastYpra.getYaw(AngleUnit.DEGREES);
            } else {
                // ...IMU be like: hell nah this is way above my paygrade.
                System.out.println("IMU: I give up :(");
                return 0.0;
            }
        }

        lastYpra = ypra;
        // Makes sure all angles are in the range 0.0 - 360.0, and that 90 degrees to the right is
        // actually at alpha = 90.0.
        return 180.0 + -ypra.getYaw(AngleUnit.DEGREES);
    }

    /** Gets the current velocity around the robot's axis in degrees per second.
     * @return The velocity in degrees per second.
     */
    public double getRotationalVelocity() {
        AngularVelocity av = imu.getRobotAngularVelocity(AngleUnit.DEGREES);
        return av.zRotationRate;
    }

    public boolean hasIMUDoneFuckedUp() {
        return this.imu.getRobotYawPitchRollAngles()
                .getAcquisitionTime() == 0;
    }
}
