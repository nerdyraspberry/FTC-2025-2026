package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.kowalski.systems.MecanumDrive;
import org.firstinspires.ftc.teamcode.kowalski.systems.MecanumMotor;

@Autonomous(name = "Measuring Autonomous")
public class MeasuringAutonomous extends LinearOpMode {
    private MecanumDrive drivetrain;

    @Override
    public void runOpMode() throws InterruptedException {
        drivetrain = new MecanumDrive(
                hardwareMap,
                "linksvoor", "rechtsvoor",
                "linksachter", "rechtsachter"
        );

        drivetrain
                .setSigns(MecanumMotor.FrontLeft, "-", "-", "-")
                .setSigns(MecanumMotor.FrontRight, "-", "+", "-")
                .setSigns(MecanumMotor.BackLeft, "+", "-", "-")
                .setSigns(MecanumMotor.BackRight, "-", "-", "+");

        waitForStart();

        drivetrain
                .move(0.0, 0.0)
                .rotate(0.6)
                .apply();
        sleep(500);
        drivetrain.move(0.0, 0.0)
                .rotate(0.0)
                .apply();
    }
}
