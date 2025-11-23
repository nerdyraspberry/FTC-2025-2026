package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

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
        Servo ballHoldingServo = opMode.hardwareMap.get(Servo.class, "ballHoldingServo");

        opMode.waitForStart();

        ballHoldingServo.setPosition(SharedThingemajigs.servoGateClose);

        if (startPos == StartingPosition.TRIANGLE) {
            // Move out of the triangle, into the scoring area
            drivetrain.move(0.0, 1.0)
                    .apply();
            opMode.sleep(1200);
            drivetrain.move(0, 0).apply();
            drivetrain.move(0.0 , 0.35)
                    .rotate(0.4)
                    .apply();
            opMode.sleep(1250);
            drivetrain.move(0, 0).rotate(0).apply();

            // Bombs away!
            SharedThingemajigs.autoShoot(shootingMotor, ballHoldingServo);
        } else if (startPos == StartingPosition.SCORING_AREA) {
            // Assume we are in the right position to score, just run the ball-shooting motor.
            SharedThingemajigs.autoShoot(shootingMotor, ballHoldingServo);

            // Back out of the scoring zone
            drivetrain.move(alliance == Alliance.BLUE ? -0.6 : 1.0, alliance == Alliance.BLUE ? -1.0 : -0.6)
                    .apply();
            opMode.sleep(1500);
            drivetrain.move(0, 0).apply();
        }

        ballHoldingServo.setPosition(SharedThingemajigs.servoGateClose);
    }
}
