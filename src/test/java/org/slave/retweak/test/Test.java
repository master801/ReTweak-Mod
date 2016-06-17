package org.slave.retweak.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Master on 6/14/2016 at 10:33 PM.
 *
 * @author Master
 */
public final class Test {

    private static final Logger LOGGER = LoggerFactory.getLogger("Test");

    private Test() {
        throw new IllegalStateException();
    }

    public static void main(final String[] arguments) {
        int input = 33;
        int output;

        if (input > 16) {
            output = input / 16;
        } else {
            output = input;
        }

        Test.LOGGER.info(
                "{} {}",
                input,
                output
        );
    }

}
