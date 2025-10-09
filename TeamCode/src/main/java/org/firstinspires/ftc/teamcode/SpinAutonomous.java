package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Spin Autonomous")
public class SpinAutonomous extends LinearOpMode {
    private DcMotor shootingMotor;

    @Override
    public void runOpMode() throws InterruptedException {
        shootingMotor = hardwareMap.get(DcMotor.class, "shootingMotor");

        waitForStart();

        shootingMotor.setPower(1.0);
        while (opModeIsActive());
        shootingMotor.setPower(0.0);
    }
}
