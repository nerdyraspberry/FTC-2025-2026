package org.firstinspires.ftc.teamcode.kowalski.dashboard;

import java.util.Arrays;

public class VerticalContainer extends DashboardText {
    public DashboardText[] children;

    public int[] sizing() {
        if (children.length == 0) return new int[]{0, 0};

        return new int[]{
                Arrays.stream(children).mapToInt(dt -> dt.sizing()[1]).max().getAsInt(),
                Arrays.stream(children).mapToInt(dt -> dt.sizing()[1]).sum(),
        };
    }

    public String[] render() {
        return Arrays.stream(children).map(DashboardText::render).reduce((a, o) -> {
            String[] both = Arrays.copyOf(a, a.length + o.length);
            System.arraycopy(o, 0, both, a.length, o.length);
            return both;
        }).get();
    }
}
