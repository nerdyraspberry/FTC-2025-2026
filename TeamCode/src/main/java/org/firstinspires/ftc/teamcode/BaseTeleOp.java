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
    final double speedChangeIncrement = .01;
    final double maxSpeed = 1.0;
    double speed = 0.5;

    final int speedChangeDebounce = 250;

    final double servoGateClose = 0.3;
    final double servoGateOpen = 0.45;

    final double shootingMotorOnSpeed = -1.0;
    // END VARIABLES

    @Override
    public void runOpMode() {
        drivetrain = SharedThingemajigs.makeMecanum(hardwareMap);

        pd = new PositionDeterminer(hardwareMap);
        pd.initialize();

        shootingMotor = hardwareMap.get(DcMotor.class, "shootingMotor");
        ballHoldingServo = hardwareMap.get(Servo.class, "ballHoldingServo");

        ballHoldingServo.setDirection(Servo.Direction.REVERSE);

        waitForStart();

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

            shootingMotor.setPower((gamepad1.x || gamepad1.square) ? shootingMotorOnSpeed : 0.0);

            ballHoldingServo.setPosition((gamepad1.y || gamepad1.triangle) ? servoGateOpen : servoGateClose);

            if (gamepad1.left_bumper) {
                shootingMotor.setPower(shootingMotorOnSpeed);
                sleep(300);
                ballHoldingServo.setPosition(servoGateOpen);
                sleep(3000);
                ballHoldingServo.setPosition(servoGateClose);
                shootingMotor.setPower(0.0);
            }
        }
    }
}
