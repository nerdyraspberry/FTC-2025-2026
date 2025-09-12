package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.kowalski.control.ControlUtils;
import org.firstinspires.ftc.teamcode.kowalski.control.PositionDeterminer;
import org.firstinspires.ftc.teamcode.kowalski.systems.MecanumDrive;

@TeleOp(name = "Base TeleOP")
public class BaseTeleOp extends LinearOpMode {
    private MecanumDrive drivetrain;
    private PositionDeterminer pd;

    @Override
    public void runOpMode() {
        drivetrain = SharedThingemajigs.makeMecanum(hardwareMap);

        pd = new PositionDeterminer(hardwareMap);
        pd.initialize();

        waitForStart();

        double speed = 1.0;
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
            if (gamepad1.dpad_left) speed = 1.0;
        }
    }
}
