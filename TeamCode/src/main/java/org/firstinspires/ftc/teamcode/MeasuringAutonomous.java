package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.kowalski.control.PositionDeterminer;
import org.firstinspires.ftc.teamcode.kowalski.systems.MecanumDrive;

@Autonomous(name = "Measuring Autonomous")
public class MeasuringAutonomous extends LinearOpMode {
    private MecanumDrive drivetrain;
    private PositionDeterminer pd;

    private void waitUntilRotation(double degrees, double margin, double direction) {
        // Stop before the actual angle; robot will spin further due to physics or something
        System.out.println("== BEGIN ==");
        double startAngle = pd.getAngle();
        if (direction > 0 && degrees < startAngle)
            degrees += 360.0;
        else if (direction < 0 && degrees > startAngle)
            degrees -= 360.0;

        while (direction > 0 ? pd.getAngle() < degrees - margin
                            : pd.getAngle() > degrees + margin) {
            System.out.print("actual angle "); System.out.println(pd.getAngle());
            System.out.print("end angle "); System.out.println(degrees);

            if (pd.hasIMUDoneFuckedUp()) {
                System.out.println("OH GOD OH FUCK PLEASE NOT AGAIN!!!");
                return;
            }
        }
    }
    private void rotateBy(double degrees, double turnRate) {
        final double ALLOWED_MARGIN = 18.0;

        // Calculate the begin and end rotation
        double startAngle = pd.getAngle();
        // Desired end angle is angle-at-start + amount*direction-of-rotation
        double endAngle = (startAngle + degrees*Math.signum(turnRate)) % 360;

        // Start the motors
        drivetrain.move(0.0, 0.0)
                .rotate(turnRate)
                .apply();
        // Use an estimation of the amount of ms to sleep:
        this.waitUntilRotation(endAngle, ALLOWED_MARGIN, Math.signum(turnRate));

        System.out.print("actual angle "); System.out.println(pd.getAngle());
        System.out.print("end angle "); System.out.println(endAngle);

        // Stop the motors
        drivetrain.rotate(0.0).apply();
    }

    @Override
    public void runOpMode() {
        drivetrain = SharedThingemajigs.makeMecanum(hardwareMap);

        pd = new PositionDeterminer(hardwareMap);
        pd.initialize();

        waitForStart();
        for (int i = 0; i < 4; i++) {
            rotateBy(90, .5);
            if (pd.hasIMUDoneFuckedUp()) break;
            sleep(1000);
        }
        for (int i = 0; i < 4; i++) {
            rotateBy(90, -.5);
            if (pd.hasIMUDoneFuckedUp()) break;
            sleep(1000);
        }

        drivetrain.rotate(0.0).apply();
//        for (int x = 0; x < 10; x++) {
//            rotateBy(90, .5);
//            rotateBy(90, -.8);
//        }

//        drivetrain
//                .move(0.0, 0.0)
//                .rotate(0.8)
//                .apply();
//        sleep(250);
//        while (opModeIsActive()) {
//            telemetry.addData("angle", pd.getAngle());
//            telemetry.update();
//
//            if (pd.getAngle() >= 89.0 && pd.getAngle() <= 90.0) {
//                sleep(10);
//
//                if (pd.getAngle() > 90.5) {
//                    drivetrain.rotate(-.05)
//                            .apply();
//                    sleep(200);
//                }
//
//                break;
//            }
//        }

//        drivetrain.move(0.0, 0.0)
//                .rotate(0.0)
//                .apply();

        telemetry.addData("angle", pd.getAngle());
        telemetry.update();
        sleep(2000);
    }
}
