package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.kowalski.systems.MecanumDrive;
import org.firstinspires.ftc.teamcode.kowalski.systems.MecanumMotor;

public class SharedThingemajigs {
    public static final double servoGateClose = 0.3;
    public static final double servoGateOpen = 0.52;

    static final double shootingMotorOnSpeed = 0.80;

    /** Creates a MecanumDrive using the configuration for this season;
     * this prevents the signs and motor names from having to be duplicated across
     * OpModes, which is hard to maintain.
     * @param hardwareMap The hardware map to fetch the motors from.
     * @return A MecanumDrive instance.
     */
    public static MecanumDrive makeMecanum(HardwareMap hardwareMap) {
        MecanumDrive drivetrain = new MecanumDrive(
                hardwareMap,
                "linksvoor", "rechtsvoor",
                "linksachter", "rechtsachter"
        );

        drivetrain
                .setSigns(MecanumMotor.FrontLeft, "-", "-", "-")
                .setSigns(MecanumMotor.FrontRight, "-", "+", "-")
                .setSigns(MecanumMotor.BackLeft, "+", "-", "-")
                .setSigns(MecanumMotor.BackRight, "-", "-", "+");

        return drivetrain;
    }

    /** For some reason, the LinearOpMode sleep() command is just... built different
     * This emulates that so that it can be used in functions not inside LinearOpMode.
     * Definitely ugly, but it is what it is... (it's literally identical to the one
     * in LinearOpMode)
     * @param millis The amount of milliseconds to Built Different Sleep(TM) for.
     */
    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void autoShoot(DcMotor shootingMotor, Servo ballHoldingServo) {

        shootingMotor.setPower(shootingMotorOnSpeed);
        sleep(1300);
        ballHoldingServo.setPosition(servoGateOpen);
//        double startTime = opMode.getRuntime();
//        while (true) {
//            boolean hasBall = ballAnalyser.delaySample(opMode.getRuntime());
//
//            if (hasBall || opMode.getRuntime() - startTime > 3.0)
//                break;
//        }
        sleep(2000);
        ballHoldingServo.setPosition(servoGateClose);
        shootingMotor.setPower(0.0);
    }
}
