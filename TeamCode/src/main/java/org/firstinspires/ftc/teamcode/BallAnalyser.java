package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class BallAnalyser {
    final double interval;
    final DcMotor wheelMotor;

    double lastSample = 0.0;
    double maxSpeedSample = 22.0; // Ugly hardcode; TODO: good way to measure max speed without outliers?

    public BallAnalyser(double interval, DcMotor wheelMotor) {
        this.interval = interval;
        this.wheelMotor = wheelMotor;
    }

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
        int motorPos = wheelMotor.getCurrentPosition();
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

    public boolean sample() {
        double delta = getMotorDelta();
        //if (delta > maxSpeedSample)

        double percentOfMax = delta / maxSpeedSample * 100.0;
        System.out.print("Delta: ");
        System.out.print(delta);
        System.out.print(" / ");
        System.out.print(maxSpeedSample);
        System.out.print(" (");
        System.out.print(percentOfMax);
        System.out.println(")");

        // Do not say there is a ball if the motor is slowing down because the operator has told it to.
        boolean hasPassingBall = 10.0 < percentOfMax && percentOfMax < 70.0 && wheelMotor.getPower() > .2;

        //if (!sensible) System.out.println("Kowalski cannot tell if there is a ball.");
        if (hasPassingBall) System.out.println("Kowalski thinks there's a ball!");
        //else System.out.println("Kowalski doesn't think there's a ball.");

        return hasPassingBall;
    }

    private boolean lastSampledVal = false;
    public boolean delaySample(double curTime) {
        if (curTime - lastSample > interval) {
            lastSample = curTime;

            lastSampledVal = sample();
        }

        return lastSampledVal;
    }
}
