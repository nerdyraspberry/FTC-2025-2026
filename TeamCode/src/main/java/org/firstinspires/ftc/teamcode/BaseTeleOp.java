package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.kowalski.control.ControlUtils;
import org.firstinspires.ftc.teamcode.kowalski.control.PositionDeterminer;
import org.firstinspires.ftc.teamcode.kowalski.systems.MecanumDrive;

@TeleOp(name = "Base TeleOP")
public class BaseTeleOp extends LinearOpMode {
    private MecanumDrive drivetrain;
    private PositionDeterminer pd;

    private DcMotor shootingMotor;

    double normalSpeed =1.0;
    double safeSpeed = 0.3;

    @Override
    public void runOpMode() {
        drivetrain = SharedThingemajigs.makeMecanum(hardwareMap);

        pd = new PositionDeterminer(hardwareMap);
        pd.initialize();

        shootingMotor = hardwareMap.get(DcMotor.class, "shootingMotor");

        waitForStart();

        double speed = safeSpeed;
        while (opModeIsActive()) {
            // Move
            drivetrain.move(speed * gamepad1.left_stick_x, -speed * gamepad1.left_stick_y)
                    .rotate(speed * ControlUtils.discreteAxis(gamepad1.left_trigger, gamepad1.right_trigger))
                    .apply();

            // Speed change
            if (gamepad1.dpad_up && speed < 1.00) {
                speed += .01;
                sleep(250);
            }
            if (gamepad1.dpad_down && speed > 0.00) {
                speed -= .01;
                sleep(250);
            }
            if (gamepad1.dpad_left) speed = safeSpeed;


            // Shooting
            shootingMotor.setPower(gamepad1.x ? - 1.0 : 0.0);
        }
    }
}
