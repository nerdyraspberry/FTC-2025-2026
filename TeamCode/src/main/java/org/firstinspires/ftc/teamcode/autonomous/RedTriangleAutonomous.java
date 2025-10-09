package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Alliance;
import org.firstinspires.ftc.teamcode.StartingPosition;

@Autonomous(name = "Autonomous (alliance = red, place = triangle)")
public class RedTriangleAutonomous extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        CommonAutonomous.run(
                Alliance.RED,
                StartingPosition.TRIANGLE,
                this
        );
    }
}
