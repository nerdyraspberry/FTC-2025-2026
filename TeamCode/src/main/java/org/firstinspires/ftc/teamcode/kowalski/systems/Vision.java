package org.firstinspires.ftc.teamcode.kowalski.systems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

/** KOE: The Kowalski Optical Engine (3000 (TM) and whatever))
 *
 */
public class Vision {
    private VisionPortal visionPortal;

    Vision(HardwareMap hardwareMap) {
        AprilTagProcessor aprilTagProcessor;
        aprilTagProcessor = AprilTagProcessor.easyCreateWithDefaults();

        visionPortal = VisionPortal.easyCreateWithDefaults(
                hardwareMap.get(CameraName.class, "Webcam"),
                aprilTagProcessor
        );
    }
}
