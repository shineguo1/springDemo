/*
 * Copyright (C) 2011 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package gxj.study.demo.datastruct.hash.my;

import com.google.common.hash.Funnel;
import com.google.common.hash.Hashing;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;
import gxj.study.demo.datastruct.hash.Hash;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Collections of strategies of generating the k * log(M) bits required for an element to be mapped
 * to a BloomFilter32 of M bits and k hash functions. These strategies are part of the serialized form
 * of the Bloom filters that use them, thus they must be preserved as is (no updates allowed, only
 * introduction of new versions).
 * <p>
 * <p>Important: the order of the constants cannot change, and they cannot be deleted - we depend on
 * their ordinal for BloomFilter32 serialization.
 *
 * @author Dimitris Andreou
 * @author Kurt Alfred Kluever
 */
enum BloomFilterStrategies implements BloomFilter32.Strategy {

    /**
     * See "Less Hashing, Same Performance: Building a Better Bloom Filter" by Adam Kirsch and Michael
     * Mitzenmacher. The paper argues that this trick doesn't significantly deteriorate the
     * performance of a Bloom filter (yet only needs two 32bit hash functions).
     */
    MURMUR128_MITZ_32() {
        @Override
        public <T> boolean put(
                T object, Funnel<? super T> funnel, int numHashFunctions, LockFreeBitArray bits) {
            long bitSize = bits.bitSize();
            long hash64 = Hashing.murmur3_128().hashObject(object, funnel).asLong();
            int hash1 = (int) hash64;
            int hash2 = (int) (hash64 >>> 32);

            boolean bitsChanged = false;
            for (int i = 1; i <= numHashFunctions; i++) {
                int combinedHash = hash1 + (i * hash2);
                // Flip all the bits if it's negative (guaranteed positive number)
                if (combinedHash < 0) {
                    combinedHash = ~combinedHash;
                }
                bitsChanged |= bits.set(combinedHash % bitSize);
            }
            return bitsChanged;
        }

        @Override
        public <T> boolean mightContain(
                T object, Funnel<? super T> funnel, int numHashFunctions, LockFreeBitArray bits) {
            long bitSize = bits.bitSize();
            long hash64 = Hashing.murmur3_128().hashObject(object, funnel).asLong();
            int hash1 = (int) hash64;
            int hash2 = (int) (hash64 >>> 32);

            for (int i = 1; i <= numHashFunctions; i++) {
                int combinedHash = hash1 + (i * hash2);
                // Flip all the bits if it's negative (guaranteed positive number)
                if (combinedHash < 0) {
                    combinedHash = ~combinedHash;
                }
                if (!bits.get(combinedHash % bitSize)) {
                    return false;
                }
            }
            return true;
        }
    },
    /**
     * simple
     */
    SIMPLE() {
        @Override
        public <T> boolean put(
                T object, Funnel<? super T> funnel, int numHashFunctions, LockFreeBitArray bits) {
            long bitSize = bits.bitSize();
            int hash32 = Hash.hashInt(object);
            short hash1 = (short) hash32;
            short hash2 = (short) (hash32 >>> 16);

            boolean bitsChanged = false;
            for (int i = 1; i <= numHashFunctions; i++) {
                short combinedHash = (short) (hash1 + (i * hash2));
                // Flip all the bits if it's negative (guaranteed positive number)
                if (combinedHash < 0) {
                    combinedHash = (short) ~combinedHash;
                }
                bitsChanged |= bits.set(combinedHash % bitSize);
            }
            return bitsChanged;
        }

        @Override
        public <T> boolean mightContain(
                T object, Funnel<? super T> funnel, int numHashFunctions, LockFreeBitArray bits) {
            long bitSize = bits.bitSize();
            int hash32 = Hash.hashInt(object);
            short hash1 = (short) hash32;
            short hash2 = (short) (hash32 >>> 16);

            for (int i = 1; i <= numHashFunctions; i++) {
                short combinedHash = (short) (hash1 + (i * hash2));
                // Flip all the bits if it's negative (guaranteed positive number)
                if (combinedHash < 0) {
                    combinedHash = (short) ~combinedHash;
                }
                if (!bits.get(combinedHash % bitSize)) {
                    return false;
                }
            }
            return true;
        }
    };

    /**
     * Models a lock-free array of bits.
     * <p>
     * <p>We use this instead of java.util.BitSet because we need access to the array of longs and we
     * need compare-and-swap.
     */
    static final class LockFreeBitArray implements Serializable {
        private static final int LONG_ADDRESSABLE_BITS = 6;
        final AtomicLongArray data;
        private final AtomicLong bitCount;

        LockFreeBitArray(long bits) {
            checkArgument(bits > 0, "data length is zero!");
            // Avoid delegating to this(long[]), since AtomicLongArray(long[]) will clone its input and
            // thus double memory usage.
            this.data =
                    new AtomicLongArray(Ints.checkedCast(LongMath.divide(bits, 64, RoundingMode.CEILING)));
            this.bitCount = new AtomicLong();
        }

        // Used by serialization
        LockFreeBitArray(long[] data) {
            checkArgument(data.length > 0, "data length is zero!");
            this.data = new AtomicLongArray(data);
            this.bitCount = new AtomicLong();
            long bitCount = 0;
            for (long value : data) {
                bitCount += Long.bitCount(value);
            }
            this.bitCount.addAndGet(bitCount);
        }

        /**
         * Returns true if the bit changed value.
         */
        boolean set(long bitIndex) {
            if (get(bitIndex)) {
                return false;
            }

            int longIndex = (int) (bitIndex >>> LONG_ADDRESSABLE_BITS);
            long mask = 1L << bitIndex; // only cares about low 6 bits of bitIndex

            long oldValue;
            long newValue;
            do {
                oldValue = data.get(longIndex);
                newValue = oldValue | mask;
                if (oldValue == newValue) {
                    return false;
                }
            } while (!data.compareAndSet(longIndex, oldValue, newValue));

            // We turned the bit on, so increment bitCount.
            bitCount.incrementAndGet();
            return true;
        }

        boolean get(long bitIndex) {
            return (data.get((int) (bitIndex >>> LONG_ADDRESSABLE_BITS)) & (1L << bitIndex)) != 0;
        }

        /**
         * Careful here: if threads are mutating the atomicLongArray while this method is executing, the
         * final long[] will be a "rolling snapshot" of the state of the bit array. This is usually good
         * enough, but should be kept in mind.
         */
        public static long[] toPlainArray(AtomicLongArray atomicLongArray) {
            long[] array = new long[atomicLongArray.length()];
            for (int i = 0; i < array.length; ++i) {
                array[i] = atomicLongArray.get(i);
            }
            return array;
        }

        /**
         * Number of bits
         */
        long bitSize() {
            return (long) data.length() * Long.SIZE;
        }

        /**
         * Number of set bits (1s).
         * <p>
         * <p>Note that because of concurrent set calls and uses of atomics, this bitCount is a (very)
         * close *estimate* of the actual number of bits set. It's not possible to do better than an
         * estimate without locking. Note that the number, if not exactly accurate, is *always*
         * underestimating, never overestimating.
         */
        long bitCount() {
            return bitCount.get();
        }

        LockFreeBitArray copy() {
            return new LockFreeBitArray(toPlainArray(data));
        }

        /**
         * Combines the two BitArrays using bitwise OR.
         * <p>
         * <p>NOTE: Because of the use of atomics, if the other LockFreeBitArray is being mutated while
         * this operation is executing, not all of those new 1's may be set in the final state of this
         * LockFreeBitArray. The ONLY guarantee provided is that all the bits that were set in the other
         * LockFreeBitArray at the start of this method will be set in this LockFreeBitArray at the end
         * of this method.
         */
        void putAll(LockFreeBitArray other) {
            checkArgument(
                    data.length() == other.data.length(),
                    "BitArrays must be of equal length (%s != %s)",
                    data.length(),
                    other.data.length());
            for (int i = 0; i < data.length(); i++) {
                long otherLong = other.data.get(i);

                long ourLongOld;
                long ourLongNew;
                boolean changedAnyBits = true;
                do {
                    ourLongOld = data.get(i);
                    ourLongNew = ourLongOld | otherLong;
                    if (ourLongOld == ourLongNew) {
                        changedAnyBits = false;
                        break;
                    }
                } while (!data.compareAndSet(i, ourLongOld, ourLongNew));

                if (changedAnyBits) {
                    int bitsAdded = Long.bitCount(ourLongNew) - Long.bitCount(ourLongOld);
                    bitCount.addAndGet(bitsAdded);
                }
            }
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (o instanceof LockFreeBitArray) {
                LockFreeBitArray lockFreeBitArray = (LockFreeBitArray) o;
                // TODO(lowasser): avoid allocation here
                return Arrays.equals(toPlainArray(data), toPlainArray(lockFreeBitArray.data));
            }
            return false;
        }

        @Override
        public int hashCode() {
            // TODO(lowasser): avoid allocation here
            return Arrays.hashCode(toPlainArray(data));
        }
    }
}
