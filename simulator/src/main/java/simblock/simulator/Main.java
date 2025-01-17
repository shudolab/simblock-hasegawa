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

package simblock.simulator;

import static simblock.settings.SimulatorConfigulation.readProperties;
import static simblock.settings.SimulatorConfigulation.getAverageMiningPower;
import static simblock.settings.SimulatorConfigulation.getCBRUsageRate;
import static simblock.settings.SimulatorConfigulation.getEndBlockHeight;
import static simblock.settings.SimulatorConfigulation.getInterval;
import static simblock.settings.SimulatorConfigulation.getNumOfNodes;
import static simblock.settings.SimulatorConfigulation.getStdevOfMiningPower;
import static simblock.settings.SimulationConfiguration.ALGO;
import static simblock.settings.SimulationConfiguration.TABLE;
import static simblock.settings.SimulationConfiguration.CHURN_NODE_RATE;
import static simblock.simulator.Network.getDegreeDistribution;
import static simblock.simulator.Network.getRegionDistribution;
import static simblock.simulator.Network.printRegion;
import static simblock.simulator.Simulator.addNode;
import static simblock.simulator.Simulator.getSimulatedNodes;
import static simblock.simulator.Simulator.printAllPropagation;
import static simblock.simulator.Simulator.printResult;
import static simblock.simulator.Simulator.setTargetInterval;
import static simblock.simulator.Timer.getCurrentTime;
import static simblock.simulator.Timer.getTask;
import static simblock.simulator.Timer.runTask;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import simblock.block.Block;
import simblock.node.Node;
import simblock.task.AbstractMintingTask;
import simblock.logger.BasicLogger;

/**
 * The type Main represents the entry point.
 */
public class Main {
    /**
     * The constant to be used as the simulation seed.
     */
    public static Random random = new Random(10);

    /**
     * The initial simulation time.
     */
    public static long simulationTime = 0;
    /**
     * Path to config file.
     */
    public static URI CONF_FILE_URI;
    /**
     * Output path.
     */
    public static URI OUT_FILE_URI;

    public static URI PROPERTIES_FILE_URI;

    static {
        try {
            CONF_FILE_URI = ClassLoader.getSystemResource("simulator.conf").toURI();
            OUT_FILE_URI = CONF_FILE_URI.resolve(new URI("../output/"));
            PROPERTIES_FILE_URI = CONF_FILE_URI.resolve(new URI("../../main/java/simblock/settings/properties/"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static String outputFileName = "output";
    private static String propagationFileName = "propagation";
    private static String resultFileName = "result";
    private static String propertiesFilePath = (PROPERTIES_FILE_URI + "initial.properties").toString()
            .replace("file:", "");

    static BasicLogger logger = BasicLogger.getLogger("simblock.output");
    static BasicLogger propagationLogger = BasicLogger.getLogger("simblock.propagation");
    static BasicLogger resultLogger = BasicLogger.getLogger("simblock.result");

    /* Parse command line option */
    private static void parseOption(String[] args) {
        if (args.length == 0) {
            return;
        }
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-properties":
                    if (i + 1 < args.length) {
                        propertiesFilePath = (PROPERTIES_FILE_URI + args[i + 1] + ".properties").toString()
                                .replace("file:", "");
                        // outputFileName = args[i + 1];
                        propagationFileName = args[i + 1];
                        resultFileName = args[i + 1];
                        i++;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /* Setup global logger */
    private static void setupLogger() {
        try {
            logger.setFileWriter(new File(OUT_FILE_URI.resolve("./visualize/" + outputFileName + ".json")));
            propagationLogger
                    .setFileWriter(
                            new File(OUT_FILE_URI.resolve("./propagation/" + propagationFileName + ".csv")));

            resultLogger.setFileWriter(new File(OUT_FILE_URI.resolve("./result/" + resultFileName + ".json")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The entry point.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        parseOption(args);
        readProperties(propertiesFilePath);
        setupLogger();

        final long start = System.currentTimeMillis();
        setTargetInterval(getInterval());

        // start json format
        logger.log("[");

        // Log regions
        BasicLogger staticLogger = BasicLogger.getLogger("simblock.static");
        try {
            staticLogger.setFileWriter(new File(OUT_FILE_URI.resolve("./static.json")));
            printRegion();
            staticLogger.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Setup network
        constructNetworkWithAllNodes(getNumOfNodes());

        // Simulate network
        simulationMain();

        // Print propagation information about all blocks
        printAllPropagation();

        printResult();

        // TODO logger
        System.out.println();

        Set<Block> blocks = new HashSet<>();

        // Get the latest block from the first simulated node
        Block block = getSimulatedNodes().get(0).getBlock();

        // Update the list of known blocks by adding the parents of the aforementioned
        // block
        while (block.getParent() != null) {
            blocks.add(block);
            block = block.getParent();
        }

        Set<Block> orphans = new HashSet<>();
        int averageOrphansSize = 0;
        // Gather all known orphans
        for (Node node : getSimulatedNodes()) {
            orphans.addAll(node.getOrphans());
            averageOrphansSize += node.getOrphans().size();
        }
        averageOrphansSize = averageOrphansSize / getSimulatedNodes().size();

        // Record orphans to the list of all known blocks
        blocks.addAll(orphans);

        ArrayList<Block> blockList = new ArrayList<>(blocks);

        // Sort the blocks first by time, then by hash code
        blockList.sort((a, b) -> {
            int order = Long.signum(a.getTime() - b.getTime());
            if (order != 0) {
                return order;
            }
            order = System.identityHashCode(a) - System.identityHashCode(b);
            return order;
        });

        // Log all orphans
        // TODO move to method and use logger
        for (Block orphan : orphans) {
            System.out.println(orphan + ":" + orphan.getHeight());
        }
        System.out.println(averageOrphansSize);

        /*
         * Log in format:
         * ＜fork_information, block height, block ID＞
         * fork_information: One of "OnChain" and "Orphan". "OnChain" denote block is on
         * Main chain.
         * "Orphan" denote block is an orphan block.
         */
        // TODO move to method and use logger
        try (BasicLogger blocklistLogger = BasicLogger.getLogger("simblock.blocklist")) {
            blocklistLogger.setFileWriter(new File(OUT_FILE_URI.resolve("./blockList.txt")));
            for (Block b : blockList) {
                if (!orphans.contains(b)) {
                    blocklistLogger.println("OnChain : " + b.getHeight() + " : " + b);
                } else {
                    blocklistLogger.println("Orphan : " + b.getHeight() + " : " + b);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.log("{");
        logger.log("\"kind\":\"simulation-end\",");
        logger.log("\"content\":{");
        logger.log("\"timestamp\":" + getCurrentTime());
        logger.log("}");
        logger.log("}");
        // end json format
        logger.log("]");
        logger.close();

        long end = System.currentTimeMillis();
        simulationTime += end - start;
        // Log simulation time in milliseconds
        System.out.println(simulationTime);
    }

    /* Main loop of the simulation */
    public static void simulationMain() {
        // Initial block height, we stop at END_BLOCK_HEIGHT
        int currentBlockHeight = 1;

        // Iterate over tasks and handle
        while (getTask() != null) {
            if (getTask() instanceof AbstractMintingTask) {
                AbstractMintingTask task = (AbstractMintingTask) getTask();
                if (task.getParent().getHeight() == currentBlockHeight) {
                    currentBlockHeight++;
                }
                if (currentBlockHeight > getEndBlockHeight()) {
                    break;
                }
                // Log every 100 blocks and at the second block
                // TODO use constants here
                if (currentBlockHeight % 100 == 0 || currentBlockHeight == 2) {
                    writeGraph(currentBlockHeight);
                    System.out.println("height : " + currentBlockHeight);
                }
            }
            // Execute task
            runTask();
        }
    }

    // TODO 以下の初期生成はシナリオを読み込むようにする予定
    // ノードを参加させるタスクを作る(ノードの参加と，リンクの貼り始めるタスクは分ける)
    // シナリオファイルで上の参加タスクをTimer入れていく．

    // TRANSLATED FROM ABOVE STATEMENT
    // The following initial generation will load the scenario
    // Create a task to join the node (separate the task of joining the node and the
    // task of
    // starting to paste the link)
    // Add the above participating tasks with a timer in the scenario file.

    /**
     * Populate the list using the distribution.
     *
     * @param distribution the distribution
     * @param facum        whether the distribution is cumulative distribution
     * @return array list
     */
    // TODO explanation on facum etc.
    public static ArrayList<Integer> makeRandomListFollowDistribution(double[] distribution, boolean facum) {
        ArrayList<Integer> list = new ArrayList<>();
        int index = 0;

        if (facum) {
            for (; index < distribution.length; index++) {
                while (list.size() <= getNumOfNodes() * distribution[index]) {
                    list.add(index);
                }
            }
            while (list.size() < getNumOfNodes()) {
                list.add(index);
            }
        } else {
            double acumulative = 0.0;
            for (; index < distribution.length; index++) {
                acumulative += distribution[index];
                while (list.size() <= getNumOfNodes() * acumulative) {
                    list.add(index);
                }
            }
            while (list.size() < getNumOfNodes()) {
                list.add(index);
            }
        }

        Collections.shuffle(list, random);
        return list;
    }

    /**
     * Populate the list using the rate.
     *
     * @param rate the rate of true
     * @return array list
     */
    public static ArrayList<Boolean> makeRandomList(float rate) {
        ArrayList<Boolean> list = new ArrayList<Boolean>();
        for (int i = 0; i < getNumOfNodes(); i++) {
            list.add(i < getNumOfNodes() * rate);
        }
        Collections.shuffle(list, random);
        return list;
    }

    /**
     * Generates a random mining power expressed as Hash Rate, and is the number of
     * mining (hash calculation) executed per millisecond.
     *
     * @return the number of hash calculations executed per millisecond.
     */
    public static int genMiningPower() {
        double r = random.nextGaussian();

        return Math.max((int) (r * getStdevOfMiningPower() + getAverageMiningPower()), 1);
    }

    /**
     * Construct network with the provided number of nodes.
     *
     * @param numNodes the num nodes
     */
    public static void constructNetworkWithAllNodes(int numNodes) {

        // Random distribution of nodes per region
        double[] regionDistribution = getRegionDistribution();
        List<Integer> regionList = makeRandomListFollowDistribution(regionDistribution, false);

        // Random distribution of node degrees
        double[] degreeDistribution = getDegreeDistribution();
        List<Integer> degreeList = makeRandomListFollowDistribution(degreeDistribution, true);

        // List of nodes using compact block relay.
        List<Boolean> useCBRNodes = makeRandomList(getCBRUsageRate());

        // List of churn nodes.
        List<Boolean> churnNodes = makeRandomList(CHURN_NODE_RATE);

        for (int id = 1; id <= numNodes; id++) {
            // Each node gets assigned a region, its degree, mining power, routing table and
            // consensus algorithm
            Node node = new Node(
                    id, degreeList.get(id - 1) + 1, regionList.get(id - 1), genMiningPower(), TABLE,
                    ALGO, useCBRNodes.get(id - 1), churnNodes.get(id - 1));
            // Add the node to the list of simulated nodes
            addNode(node);

            logger.log("{");
            logger.log("\"kind\":\"add-node\",");
            logger.log("\"content\":{");
            logger.log("\"timestamp\":0,");
            logger.log("\"node-id\":" + id + ",");
            logger.log("\"region-id\":" + regionList.get(id - 1));
            logger.log("}");
            logger.log("},");
        }

        // Link newly generated nodes
        for (Node node : getSimulatedNodes()) {
            node.joinNetwork();
        }

        // Designates a random node (nodes in list are randomized) to mint the genesis
        // block
        getSimulatedNodes().get(0).genesisBlock();
    }

    /**
     * Network information when block height is <em>blockHeight</em>, in format:
     *
     * <p>
     * <em>nodeID_1</em>, <em>nodeID_2</em>
     *
     * <p>
     * meaning there is a connection from nodeID_1 to right nodeID_1.
     *
     * @param blockHeight the index of the graph and the current block height
     */
    // TODO use logger
    public static void writeGraph(int blockHeight) {
        try {
            FileWriter fw = new FileWriter(
                    new File(OUT_FILE_URI.resolve("./graph/" + blockHeight + ".txt")), false);
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

            for (int index = 1; index <= getSimulatedNodes().size(); index++) {
                Node node = getSimulatedNodes().get(index - 1);
                for (int i = 0; i < node.getNeighbors().size(); i++) {
                    Node neighbor = node.getNeighbors().get(i);
                    pw.println(node.getNodeID() + " " + neighbor.getNodeID());
                }
            }
            pw.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
