package org.firstinspires.ftc.teamcode.kowalski.dashboard;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Arrays;

public class Dashboard {
    Telemetry telemetry;
    DashboardText node;

    public Dashboard(Telemetry telemetry) {
        this.telemetry = telemetry;
        this.node = new DashboardText();
    }

    public void initialize() {
        telemetry.setItemSeparator("");
        telemetry.setCaptionValueSeparator("");
    }
    public void render() {
        // noinspection ResultOfMethodCallIgnored
        Arrays.stream(node.render()).map(line -> telemetry.addData("", line));

        this.telemetry.update();
    }
}
