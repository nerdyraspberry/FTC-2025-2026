package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Alliance;
import org.firstinspires.ftc.teamcode.SharedThingemajigs;
import org.firstinspires.ftc.teamcode.StartingPosition;
import org.firstinspires.ftc.teamcode.kowalski.systems.MecanumDrive;

public class CommonAutonomous {
    public static void run(
            Alliance alliance,
            StartingPosition startPos,
            LinearOpMode opMode
    ) {
        MecanumDrive drivetrain = SharedThingemajigs.makeMecanum(opMode.hardwareMap);
        DcMotor shootingMotor = opMode.hardwareMap.get(DcMotor.class, "shootingMotor");

        if (startPos == StartingPosition.TRIANGLE) {
            // Move out of the triangle
            drivetrain.move(0.0, 1.0)
                    .apply();
            opMode.sleep(1000);
            drivetrain.move(0, 0).apply();
        } else if (startPos == StartingPosition.SCORING_AREA) {
            // Assume we are in the right position to score, just run the ball-shooting motor.
            shootingMotor.setPower(1.0);
            opMode.sleep(3000);
            shootingMotor.setPower(0.0);

            // Back out of the scoring zone
            drivetrain.move(0.0, -1.0)
                    .apply();
            opMode.sleep(1000);
            drivetrain.move(0, 0).apply();
        }
    }
}
