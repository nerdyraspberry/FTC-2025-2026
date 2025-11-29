package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.kowalski.control.ControlUtils;
import org.firstinspires.ftc.teamcode.kowalski.control.PositionDeterminer;
import org.firstinspires.ftc.teamcode.kowalski.systems.MecanumDrive;

@TeleOp(name = "Base TeleOP")
public class BaseTeleOp extends LinearOpMode {
    private MecanumDrive drivetrain;
    private PositionDeterminer pd;

    private DcMotor shootingMotor;
    private Servo ballHoldingServo;

    private BallAnalyser ballAnalyser;

    // VARIABLES
    // NOTE: some variables have been moved to SharedThingemajigs.java
    final double speedChangeIncrement = .05;
    double maxSpeed = 1.0;
    double speed;
    boolean safeMode = false;

    final int speedChangeDebounce = 250;

    final double delayBeforeLatchCanOpen = .5;
    // END VARIABLES

    @Override
    public void runOpMode() {
        drivetrain = SharedThingemajigs.makeMecanum(hardwareMap);

        pd = new PositionDeterminer(hardwareMap);
        pd.initialize();

        shootingMotor = hardwareMap.get(DcMotor.class, "shootingMotor");
        ballHoldingServo = hardwareMap.get(Servo.class, "ballHoldingServo");

        ballHoldingServo.setDirection(Servo.Direction.REVERSE);

        ballAnalyser = new BallAnalyser(.01, shootingMotor);

        // Allow setting safe mode
        if (gamepad1.right_bumper) {
            maxSpeed = 0.5;
            safeMode = true;
        }
        speed = maxSpeed;

        waitForStart();
        ballHoldingServo.setPosition(SharedThingemajigs.servoGateClose);

        boolean mayOpenLatch = false;
        double lastSpinupTime = 0.0;

        boolean prevXState = false, prevYState = false;
        while (opModeIsActive()) {
            // Move
            drivetrain.move(speed * gamepad1.left_stick_x, -speed * gamepad1.left_stick_y)
                    .rotate(speed * ControlUtils.discreteAxis(gamepad1.left_trigger, gamepad1.right_trigger))
                    .apply();

            // Speed change
            if (gamepad1.dpad_up && speed < maxSpeed) {
                speed += speedChangeIncrement;
                sleep(speedChangeDebounce);
            }
            if (gamepad1.dpad_down && speed > 0.00) {
                speed -= speedChangeIncrement;
                sleep(speedChangeDebounce);
            }
            if (gamepad1.dpad_left) speed = maxSpeed;

            boolean xState = gamepad1.x || gamepad1.square;
            boolean yState = gamepad1.y || gamepad1.triangle;
            if (xState != prevXState) {
                shootingMotor.setPower(xState ? SharedThingemajigs.shootingMotorOnSpeed : 0.0);
                if (xState) System.out.println("Started motor spin!");
                else System.out.println("Stopped motor spin!");

                prevXState = xState;
            }
            // Book-keeping: will prevent servo from opening if motor is not up to speed
            if (xState && !mayOpenLatch) {
                mayOpenLatch = true;
                lastSpinupTime = getRuntime();
            } else if (!xState && mayOpenLatch) {
                mayOpenLatch = false;
            }

            double deltaOpen = getRuntime() - lastSpinupTime;
            // Book-keeping: check the spinning speed of the motor
            // The value is only sensible if the motor has ran, and has been up to speed at least once:
            // otherwise, comparing percentages does not make sense.
            boolean hasBall = ballAnalyser.delaySample(getRuntime());
            boolean sensible = lastSpinupTime > 0.0 && deltaOpen > delayBeforeLatchCanOpen;

            telemetry.addData("Pressing Y?", yState);
            telemetry.addData("Allowed to open?", mayOpenLatch && deltaOpen > delayBeforeLatchCanOpen);
            telemetry.addData("Will open?", yState && (mayOpenLatch && deltaOpen > delayBeforeLatchCanOpen));
            telemetry.addData("HAS BALL?", hasBall && sensible);

            telemetry.update();
            if (yState != prevYState) {
                ballHoldingServo.setPosition(yState && (mayOpenLatch && deltaOpen > delayBeforeLatchCanOpen) ? SharedThingemajigs.servoGateOpen : SharedThingemajigs.servoGateClose);
                if (mayOpenLatch && deltaOpen > delayBeforeLatchCanOpen) {
                    if (yState) System.out.println("Opening hatch!");
                    else System.out.println("Closing hatch!");
                }

                prevYState = yState;
            }

            if (gamepad1.left_bumper) {
                drivetrain.move(0, 0).rotate(0).apply();
                SharedThingemajigs.autoShoot(shootingMotor, ballHoldingServo, this, true);
            }
            if (safeMode) {
                telemetry.addData("SAFE MODE", "ENABLED");
                telemetry.update();
            }
        }
    }
}
