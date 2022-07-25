package com.chup.mobcoinsplus.extras;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CoinsTop {
    public static @NotNull Map<UUID, Integer> sortByValue(@NotNull Map<UUID, Integer> hm) {
        List<Map.Entry<UUID, Integer> > list = new LinkedList<>(hm.entrySet());
        list.sort((o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));
        HashMap<UUID, Integer> temp = new LinkedHashMap<>();
        for (Map.Entry<UUID, Integer> aa : list) { temp.put(aa.getKey(), aa.getValue()); }
        return temp;
    }
}
