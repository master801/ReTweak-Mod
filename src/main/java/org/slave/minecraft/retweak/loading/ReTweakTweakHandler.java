package org.slave.minecraft.retweak.loading;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slave.lib.exceptions.InvalidSortException;
import org.slave.minecraft.retweak.loading.tweak.Tweak;

import java.util.Comparator;

/**
 * Created by Master on 4/27/2016 at 7:19 AM.
 *
 * @author Master
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReTweakTweakHandler {

    private static ReTweakTweakHandler instance;

    //TODO

    public static ReTweakTweakHandler getInstance() {
        if (ReTweakTweakHandler.instance == null) ReTweakTweakHandler.instance = new ReTweakTweakHandler();
        return ReTweakTweakHandler.instance;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class TweakComparator implements Comparator<Tweak> {

        static final Comparator<Tweak> INSTANCE = new TweakComparator();

        @Override
        public int compare(final Tweak o1, final Tweak o2) {
            if (o1 != null && o2 != null) {
                if (o1.getWantedSortIndex() < 0 || o2.getWantedSortIndex() < 0) throw new InvalidSortException("Cannot have sort index less than zero!");
                if (o1.getWantedSortIndex() == o2.getWantedSortIndex()) throw new InvalidSortException("Sort index of Tweak cannot be the same!");
                if (o1.getWantedSortIndex() < o2.getWantedSortIndex()) return -1;
                if (o1.getWantedSortIndex() > o2.getWantedSortIndex()) return 1;
            }
            return 0;
        }

    }

}
