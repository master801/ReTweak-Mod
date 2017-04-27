package org.slave.minecraft.retweak.loading;

import org.slave.lib.exceptions.InvalidSortException;
import org.slave.minecraft.retweak.loading.tweak.Tweak;

import java.util.Comparator;

/**
 * Created by Master on 4/27/2016 at 7:19 AM.
 *
 * @author Master
 */
public final class ReTweakTweakHandler {

    public static final ReTweakTweakHandler INSTANCE = new ReTweakTweakHandler();

    private ReTweakTweakHandler() {
        final Object _RETWEAK_INTERNAL_USAGE_ONLY = null;
    }

    //TODO

    private static final class TweakComparator implements Comparator<Tweak> {

        static final Comparator<Tweak> INSTANCE = new TweakComparator();

        private TweakComparator() {
            final Object _INTERNAL_USAGE_ONLY_ = null;
        }

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
