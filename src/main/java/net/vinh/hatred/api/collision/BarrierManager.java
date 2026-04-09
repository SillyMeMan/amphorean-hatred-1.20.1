package net.vinh.hatred.api.collision;

import java.util.ArrayList;
import java.util.List;

public final class BarrierManager {
    private static final List<Barrier> barriers = new ArrayList<>(); // TODO: make this system resistant to world rejoins/server restarts

    public static void addBarrier(Barrier barrier) {
        barriers.add(barrier);
    }

    public static void removeBarrier(Barrier barrier) {
        barriers.remove(barrier);
    }

    public static List<Barrier> getCurrentBarriers() {
        return barriers;
    }
}
