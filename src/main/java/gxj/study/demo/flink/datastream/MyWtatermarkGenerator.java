package gxj.study.demo.flink.datastream;

import org.apache.flink.annotation.Public;
import org.apache.flink.api.common.eventtime.Watermark;
import org.apache.flink.api.common.eventtime.WatermarkGenerator;
import org.apache.flink.api.common.eventtime.WatermarkOutput;

import java.time.Duration;

import static org.apache.flink.util.Preconditions.checkArgument;
import static org.apache.flink.util.Preconditions.checkNotNull;

/**
 * @author xinjie_guo
 * @date 2022/7/3 13:27
 */
public class MyWtatermarkGenerator<T> implements WatermarkGenerator<T> {

        /** The maximum timestamp encountered so far. */
        private long maxTimestamp;

        /** The maximum out-of-orderness that this watermark generator assumes. */
        private final long outOfOrdernessMillis;

        /**
         * Creates a new watermark generator with the given out-of-orderness bound.
         *
         * @param maxOutOfOrderness The bound for the out-of-orderness of the event timestamps.
         */
        public MyWtatermarkGenerator(Duration maxOutOfOrderness) {
            checkNotNull(maxOutOfOrderness, "maxOutOfOrderness");
            checkArgument(!maxOutOfOrderness.isNegative(), "maxOutOfOrderness cannot be negative");

            this.outOfOrdernessMillis = maxOutOfOrderness.toMillis();

            // start so that our lowest watermark would be Long.MIN_VALUE.
            this.maxTimestamp = Long.MIN_VALUE + outOfOrdernessMillis + 1;
        }

        // ------------------------------------------------------------------------

        @Override
        public void onEvent(T event, long eventTimestamp, WatermarkOutput output) {
            System.out.println(this);
            maxTimestamp = Math.max(maxTimestamp, eventTimestamp);
            output.emitWatermark(new Watermark(maxTimestamp - outOfOrdernessMillis - 1));
        }

        @Override
        public void onPeriodicEmit(WatermarkOutput output) {
//            output.emitWatermark(new Watermark(maxTimestamp - outOfOrdernessMillis - 1));
        }
    }

