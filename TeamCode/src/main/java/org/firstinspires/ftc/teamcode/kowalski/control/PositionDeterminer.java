package org.firstinspires.ftc.teamcode.kowalski.control;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class PositionDeterminer {
    private IMU imu;

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

    /** Get the angle that the robot is currently pointing in */
    public double getAngle() {
        return -this.imu
                .getRobotYawPitchRollAngles()
                .getYaw(AngleUnit.DEGREES);
    }
}
