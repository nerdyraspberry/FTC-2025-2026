package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

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

        if (startPos == StartingPosition.TRIANGLE) {
            // Move out of the triangle
            drivetrain.move(0.0, 1.0)
                    .apply();
            opMode.sleep(1000);
            drivetrain.move(0, 0).apply();
        } else {
            // Don't do anything here... yet
        }
    }
}
