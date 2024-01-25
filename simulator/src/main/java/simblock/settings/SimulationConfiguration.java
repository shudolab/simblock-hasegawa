/*
 * Copyright 2019 Distributed Systems Group
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package simblock.settings;

/**
 * The type Simulation configuration allows for specific simulation instance
 * configuration.
 */
public class SimulationConfiguration {

        /**
         * The kind of routing table.
         */
        public static final String TABLE = "simblock.node.routing.BitcoinCoreTable";

        /**
         * The consensus algorithm to be used.
         */
        // TODO not documented in markdown
        // TODO return to PoW
        public static final String ALGO = "simblock.node.consensus.ProofOfWork";

        /**
         * The constant AVERAGE_COINS.
         */
        // TODO
        public static final int AVERAGE_COINS = 4000;
        /**
         * The constant STDEV_OF_COINS.
         */
        // TODO
        public static final int STDEV_OF_COINS = 2000;

        /**
         * The reward a PoS minter gets for staking.
         */
        public static final double STAKING_REWARD = 0.01;

        /**
         * Block size. (unit: byte).
         */
        public static final long BLOCK_SIZE = 535000;// 6110;//8000;//535000;//0.5MB

        /**
         * The rate of nodes that cause churn.
         */
        public static final float CHURN_NODE_RATE = 0.976f;
        /**
         * Compact block size. (unit: byte)
         */
        public static final long COMPACT_BLOCK_SIZE = 18 * 1000; // 18KB
        /**
         * CBR failure rate for a node that always connect network.
         */
        public static final float CBR_FAILURE_RATE_FOR_CONTROL_NODE = 0.13f;
        /**
         * CBR failure rate for a node that causes churn.
         */
        public static final float CBR_FAILURE_RATE_FOR_CHURN_NODE = 0.27f;

        /**
         * The distribution of data size that a control node receives when fails CBR.
         */
        public static final float[] CBR_FAILURE_BLOCK_SIZE_DISTRIBUTION_FOR_CONTROL_NODE = {
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f
        };
        /**
         * The distribution of data size that a churn node receives when fails CBR.
         */
        public static final float[] CBR_FAILURE_BLOCK_SIZE_DISTRIBUTION_FOR_CHURN_NODE = {
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
                        0.01f, 0.01f,
                        0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f,
                        0.02f, 0.02f,
                        0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f,
                        0.02f, 0.02f,
                        0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f,
                        0.02f, 0.02f,
                        0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f,
                        0.02f, 0.02f,
                        0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.02f, 0.03f,
                        0.03f, 0.03f,
                        0.03f, 0.03f, 0.03f, 0.03f, 0.03f, 0.03f, 0.03f, 0.03f, 0.03f, 0.03f, 0.03f, 0.03f, 0.03f,
                        0.03f, 0.03f,
                        0.03f, 0.03f, 0.03f, 0.03f, 0.03f, 0.03f, 0.03f, 0.03f, 0.03f, 0.03f, 0.03f, 0.03f, 0.03f,
                        0.03f, 0.03f,
                        0.03f, 0.03f, 0.03f, 0.03f, 0.03f, 0.03f, 0.04f, 0.04f, 0.04f, 0.04f, 0.04f, 0.04f, 0.04f,
                        0.04f, 0.04f,
                        0.04f, 0.04f, 0.04f, 0.04f, 0.04f, 0.04f, 0.04f, 0.04f, 0.04f, 0.04f, 0.04f, 0.04f, 0.04f,
                        0.04f, 0.04f,
                        0.04f, 0.04f, 0.04f, 0.05f, 0.05f, 0.05f, 0.05f, 0.05f, 0.05f, 0.05f, 0.05f, 0.05f, 0.05f,
                        0.05f, 0.05f,
                        0.05f, 0.05f, 0.05f, 0.05f, 0.05f, 0.05f, 0.05f, 0.05f, 0.05f, 0.06f, 0.06f, 0.06f, 0.06f,
                        0.06f, 0.06f,
                        0.06f, 0.06f, 0.06f, 0.06f, 0.06f, 0.06f, 0.06f, 0.06f, 0.06f, 0.06f, 0.06f, 0.07f, 0.07f,
                        0.07f, 0.07f,
                        0.07f, 0.07f, 0.07f, 0.07f, 0.07f, 0.07f, 0.07f, 0.07f, 0.07f, 0.07f, 0.08f, 0.08f, 0.08f,
                        0.08f, 0.08f,
                        0.08f, 0.08f, 0.08f, 0.08f, 0.08f, 0.08f, 0.08f, 0.09f, 0.09f, 0.09f, 0.09f, 0.09f, 0.09f,
                        0.09f, 0.09f,
                        0.09f, 0.09f, 0.09f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.11f, 0.11f,
                        0.11f, 0.11f,
                        0.11f, 0.11f, 0.11f, 0.11f, 0.11f, 0.12f, 0.12f, 0.12f, 0.12f, 0.12f, 0.12f, 0.12f, 0.12f,
                        0.13f, 0.13f,
                        0.13f, 0.13f, 0.13f, 0.13f, 0.13f, 0.14f, 0.14f, 0.14f, 0.14f, 0.14f, 0.14f, 0.14f, 0.15f,
                        0.15f, 0.15f,
                        0.15f, 0.15f, 0.15f, 0.16f, 0.16f, 0.16f, 0.16f, 0.16f, 0.16f, 0.17f, 0.17f, 0.17f, 0.17f,
                        0.17f, 0.18f,
                        0.18f, 0.18f, 0.18f, 0.18f, 0.19f, 0.19f, 0.19f, 0.19f, 0.19f, 0.2f, 0.2f, 0.2f, 0.2f, 0.21f,
                        0.21f, 0.21f,
                        0.21f, 0.22f, 0.22f, 0.22f, 0.22f, 0.23f, 0.23f, 0.23f, 0.23f, 0.24f, 0.24f, 0.24f, 0.24f,
                        0.25f, 0.25f,
                        0.25f, 0.26f, 0.26f, 0.26f, 0.27f, 0.27f, 0.27f, 0.28f, 0.28f, 0.28f, 0.29f, 0.29f, 0.29f, 0.3f,
                        0.3f, 0.3f,
                        0.31f, 0.31f, 0.31f, 0.32f, 0.32f, 0.32f, 0.33f, 0.33f, 0.34f, 0.34f, 0.35f, 0.35f, 0.36f,
                        0.36f, 0.37f,
                        0.37f, 0.38f, 0.38f, 0.39f, 0.39f, 0.4f, 0.4f, 0.41f, 0.41f, 0.42f, 0.42f, 0.43f, 0.43f, 0.44f,
                        0.44f,
                        0.45f,
                        0.45f, 0.46f, 0.46f, 0.47f, 0.47f, 0.48f, 0.48f, 0.49f, 0.5f, 0.51f, 0.52f, 0.53f, 0.54f, 0.55f,
                        0.56f,
                        0.57f, 0.58f, 0.59f, 0.6f, 0.61f, 0.62f, 0.63f, 0.64f, 0.65f, 0.66f, 0.67f, 0.68f, 0.69f, 0.7f,
                        0.71f,
                        0.72f, 0.73f, 0.74f, 0.75f, 0.76f, 0.77f, 0.78f, 0.79f, 0.8f, 0.81f, 0.82f, 0.83f, 0.84f, 0.85f,
                        0.86f,
                        0.87f, 0.88f, 0.89f, 0.9f, 0.91f, 0.92f, 0.93f, 0.94f, 0.95f, 0.96f
        };
}
