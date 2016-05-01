package org.slave.minecraft.retweak.loading.train;

import org.slave.minecraft.retweak.loading.train.TrainCar.ClassName;
import org.slave.minecraft.retweak.loading.train.TrainCar.Desc;
import org.slave.minecraft.retweak.loading.train.TrainCar.Name;
import org.slave.tool.remapper.SRG;
import org.slave.tool.remapper.SRGEntries;
import org.slave.tool.remapper.SRG_Type;

import java.util.ArrayList;

/**
 * Created by Master on 4/29/2016 at 7:56 PM.
 *
 * @author Master
 */
public final class Train {

    private final ArrayList<TrainCar> classes = new ArrayList<>();

    private Train() {
    }

    private void _fromSRG(SRG srg) {
        fromSRG_Class(srg);
        fromSRG_Field(srg);
        fromSRG_Method(srg);
    }

    private void fromSRG_Class(SRG srg) {
        SRGEntries srgEntries = srg.getSRGEntries(SRG_Type.CL);
        for(String[] entry : srgEntries.getEntries()) {
            classes.add(new TrainCar(
                    new ClassName(
                            entry[0],
                            entry[1]
                    )
            ));
        }
    }

    private void fromSRG_Field(SRG srg) {
        SRGEntries srgEntries = srg.getSRGEntries(SRG_Type.FD);
        for(String[] entry : srgEntries.getEntries()) {
            TrainCar classTrainCar = getClass(new ClassName(
                    entry[0].substring(
                            0,
                            entry[0].lastIndexOf('/')
                    ),
                    entry[1].substring(
                            0,
                            entry[1].lastIndexOf('/')
                    )
            ));

            if (classTrainCar != null) {
                classTrainCar.addField(new TrainCar(
                        classTrainCar.getClassName(),
                        new Name(
                                entry[0].substring(
                                        entry[0].indexOf('/') + 1
                                ),
                                entry[1].substring(
                                        entry[1].indexOf('/') + 1
                                )
                        )
                ));
            } else {
                throw new IllegalStateException();
            }
        }
    }

    private void fromSRG_Method(SRG srg) {
        SRGEntries srgEntries = srg.getSRGEntries(SRG_Type.MD);
        for(String[] entry : srgEntries.getEntries()) {
            TrainCar classTrainCar = getClass(new ClassName(
                    entry[0].substring(
                            0,
                            entry[0].lastIndexOf('/')
                    ),
                    entry[2].substring(
                            0,
                            entry[2].lastIndexOf('/')
                    )
            ));

            if (classTrainCar != null) {
                classTrainCar.addMethod(new TrainCar(
                        classTrainCar.getClassName(),
                        new Name(
                                entry[0].substring(
                                        entry[0].indexOf('/') + 1
                                ),
                                entry[2].substring(
                                        entry[2].indexOf('/') + 1
                                )
                        ),
                        new Desc(
                                entry[1],
                                entry[3]
                        )
                ));
            } else {
                throw new IllegalStateException();
            }
        }
    }

    private TrainCar getClass(ClassName className) {
        for(TrainCar _class : classes) {
            if (_class.getClassName().equals(className)) return _class;
        }
        return null;
    }

    public static Train fromSRG(SRG srg) {
        if (srg == null) return null;
        Train train = new Train();
        train._fromSRG(srg);
        return train;
    }

}
