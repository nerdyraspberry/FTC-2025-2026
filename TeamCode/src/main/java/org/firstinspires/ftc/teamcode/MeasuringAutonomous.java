package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.kowalski.control.PositionDeterminer;
import org.firstinspires.ftc.teamcode.kowalski.systems.MecanumDrive;
import org.firstinspires.ftc.teamcode.kowalski.systems.MecanumMotor;

@Autonomous(name = "Measuring Autonomous")
public class MeasuringAutonomous extends LinearOpMode {
    private MecanumDrive drivetrain;
    private PositionDeterminer pd;

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

        pd = new PositionDeterminer(hardwareMap);
        pd.initialize();

        waitForStart();

        drivetrain
                .move(0.0, 0.0)
                .rotate(0.2)
                .apply();
        while (opModeIsActive()) {
            telemetry.addData("angle", pd.getAngle());
            telemetry.update();

            if (pd.getAngle() >= 80.0 && pd.getAngle() <= 90.0)
                break;
        }
        drivetrain.move(0.0, 0.0)
                .rotate(0.0)
                .apply();
    }
}
