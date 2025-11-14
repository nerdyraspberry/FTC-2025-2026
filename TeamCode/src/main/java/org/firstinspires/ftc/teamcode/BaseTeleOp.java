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

    // VARIABLES
    final double speedChangeIncrement = .05;
    double maxSpeed = 1.0;
    double speed;
    boolean safeMode = false;

    final int speedChangeDebounce = 250;

    final double servoGateClose = 0.3;
    final double servoGateOpen = 0.52;

    final double shootingMotorOnSpeed = 1.0;

    final double delayBeforeLatchCanOpen = .5;
    // END VARIABLES

    // NOTE: this code is not YET useful, but it may be? I'm not sure!
    // When changing the cache size, make sure to change the amount of zeroes in the array.
    final int motorCacheSize = 4;
    int[] motorDeltaCache = {0, 0, 0, 0};
    int prevMotorPos = 0;

    private void _shiftCacheData() {
        for (int i = 0; i < motorCacheSize - 1; i++)
            motorDeltaCache[i + 1] = motorDeltaCache[i];
    }
    private double getMotorDelta() {
        // Calculate motor delta
        int motorPos = shootingMotor.getCurrentPosition();
        int newDelta = motorPos - prevMotorPos;
        prevMotorPos = motorPos;

        // Store in cache
        _shiftCacheData();
        motorDeltaCache[0] = newDelta;

        // Perform averaging
        double avgMotorDelta = 0;
        for (int i = 0; i < motorCacheSize; i++)
            avgMotorDelta += (double)motorDeltaCache[i];
        avgMotorDelta /= 4;

        return avgMotorDelta;
    }

    @Override
    public void runOpMode() {
        drivetrain = SharedThingemajigs.makeMecanum(hardwareMap);

        pd = new PositionDeterminer(hardwareMap);
        pd.initialize();

        shootingMotor = hardwareMap.get(DcMotor.class, "shootingMotor");
        ballHoldingServo = hardwareMap.get(Servo.class, "ballHoldingServo");

        ballHoldingServo.setDirection(Servo.Direction.REVERSE);

        // Allow setting safe mode
        if (gamepad1.right_bumper) {
            maxSpeed = 0.5;
            safeMode = true;
        }
        speed = maxSpeed;

        waitForStart();

        boolean mayOpenLatch = false;
        double lastSpinupTime = 0.0;

        int motorPos = 0;
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

            if (!safeMode) {
                shootingMotor.setPower((gamepad1.x || gamepad1.square) ? shootingMotorOnSpeed : 0.0);
                // Book-keeping: will prevent servo from opening if motor is not up to speed
                if (gamepad1.x && !mayOpenLatch) {
                    mayOpenLatch = true;
                    lastSpinupTime = getRuntime();
                } else if (!gamepad1.x && mayOpenLatch) {
                    mayOpenLatch = false;
                }
                // Book-keeping: check the spinning speed of the motor
                // TODO: figure out if and how this may be used
                //telemetry.addData("Delta Pos", getMotorDelta());

                double deltaOpen = getRuntime() - lastSpinupTime;
                telemetry.addData("Pressing Y?", gamepad1.y);
                telemetry.addData("Allowed to open?", mayOpenLatch && deltaOpen > delayBeforeLatchCanOpen);
                telemetry.addData("Will open?", (gamepad1.y || gamepad1.triangle) && (mayOpenLatch && deltaOpen > delayBeforeLatchCanOpen));
                telemetry.update();
                ballHoldingServo.setPosition((gamepad1.y || gamepad1.triangle) && (mayOpenLatch && deltaOpen > delayBeforeLatchCanOpen) ? servoGateOpen : servoGateClose);

                if (gamepad1.left_bumper) {
                    shootingMotor.setPower(shootingMotorOnSpeed);
                    sleep((long)(delayBeforeLatchCanOpen * 1000.0));
                    ballHoldingServo.setPosition(servoGateOpen);
                    sleep(3000);
                    ballHoldingServo.setPosition(servoGateClose);
                    shootingMotor.setPower(0.0);
                }
            } else {
                telemetry.addData("SAFE MODE", "ENABLED");
                telemetry.update();
            }
        }
    }
}
