package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Proto Tester Autonomous")
public class PrototypeTesterAutonomous extends LinearOpMode {
    private DcMotor test1;
    private DcMotor test2;

    @Override
    public void runOpMode() {
        test1 = hardwareMap.get(DcMotor.class, "test1");
        test2 = hardwareMap.get(DcMotor.class, "test2");

        waitForStart();

        test1.setPower(1.0);
        test2.setPower(1.0);

        // This makes Android Studio stop complaining about the empty loop.
        // It is empty because it's just there to wait for the stop button
        // to be pressed.
        //noinspection StatementWithEmptyBody
        while (opModeIsActive());

        test1.setPower(0.0);
        test2.setPower(0.0);
    }
}
