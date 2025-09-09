package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.kowalski.control.PositionDeterminer;
import org.firstinspires.ftc.teamcode.kowalski.systems.MecanumDrive;
import org.firstinspires.ftc.teamcode.kowalski.systems.MecanumMotor;

@Autonomous(name = "Measuring Autonomous")
public class MeasuringAutonomous extends LinearOpMode {
    private MecanumDrive drivetrain;
    private PositionDeterminer pd;

    /** Linearly interpolates (and extrapolates) between two values
     * @param a The value of the thing at 0.0
     * @param b The value of the thing at 1.0
     * @param amount The value to estimate at. This may be out of the range [0.0, 1.0] to extrapolate.
     * @return The estimated number.
     */
    private double interpolate(double a, double b, double amount) {
        return a + (b - a) * amount;
    }

    private int interpolateForRotation(double degrees, double turnRate) {
        // TODO: do more measurements and actually write this function for real.
        return 1000;
    }
    private void rotateBy(double degrees, double turnRate) {
        final int MAX_TRIALS = 3;
        final double ALLOWED_MARGIN = 1.0;
        final double NUDGE_FACTOR = .5;

        // Calculate the begin and end rotation
        double startAngle = pd.getAngle();
        // Desired end angle is angle-at-start + amount*direction-of-rotation
        double endAngle = startAngle + degrees*Math.signum(turnRate);

        // Start the motors
        drivetrain.move(0.0, 0.0)
                .rotate(turnRate)
                .apply();
        // Use an estimation of the amount of ms to sleep:
        sleep(this.interpolateForRotation(Math.abs(degrees), turnRate));
        // Try a few times to correct the rotation to be the actual required rotation,
        // the estimation is probably wrong.
        for (int i = 0; i < MAX_TRIALS; i++) {
            double actualAngle = pd.getAngle();
            // Possibility 1: the rotation is within the margin of error
            if (actualAngle >= endAngle - ALLOWED_MARGIN && actualAngle <= endAngle + ALLOWED_MARGIN)
                break; // Then we just stop; no adjustments necessary
            // Possibility 2: we overshot
            else if (actualAngle >= endAngle + ALLOWED_MARGIN) {
                // Turn in the opposite direction, but slower: it is more likely our estimates will
                // be correct then
                double correctionTurnRate = -turnRate * NUDGE_FACTOR;
                // Calculate the difference between the current angle and the desired angle.
                // Sign is discarded because interpolateForRotation does not care anyway.
                double neededRotation = Math.abs(actualAngle - endAngle);
                drivetrain.rotate(correctionTurnRate)
                        .apply();
                sleep(this.interpolateForRotation(neededRotation, correctionTurnRate));
            }
            // Possibility 3: we undershot
            else if (actualAngle <= endAngle - ALLOWED_MARGIN) {
                // See previous block for what this does
                double correctionTurnRate = turnRate * NUDGE_FACTOR;
                double neededRotation = Math.abs(actualAngle - endAngle);

                drivetrain.rotate(correctionTurnRate)
                        .apply();
                sleep(this.interpolateForRotation(neededRotation, correctionTurnRate));
            }
        }

        // Stop the motors
        drivetrain.rotate(0.0)
                .apply();
    }

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

            if (pd.getAngle() >= 89.0 && pd.getAngle() <= 90.0) {
                sleep(10);

                if (pd.getAngle() > 90.5) {
                    drivetrain.rotate(-.05)
                            .apply();
                    sleep(200);
                }

                break;
            }
        }

        drivetrain.move(0.0, 0.0)
                .rotate(0.0)
                .apply();

        telemetry.addData("angle", pd.getAngle());
        telemetry.update();
        sleep(2000);
    }
}
