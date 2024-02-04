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

import static simblock.settings.SimulatorConfigulation.getEndBlockHeight;
import static simblock.settings.SimulatorConfigulation.getNumOfNodes;
import static simblock.simulator.Timer.getCurrentTime;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import simblock.block.Block;
import simblock.logger.BasicLogger;
import simblock.node.Node;

/**
 * The type Simulator is tasked with maintaining the list of simulated nodes and
 * managing the
 * block interval. It observes and manages the arrival of new blocks at the
 * simulation level.
 */
public class Simulator {

    /**
     * A list of nodes that will be used in a simulation.
     */
    private static final ArrayList<Node> simulatedNodes = new ArrayList<>();

    /**
     * The target block interval in milliseconds.
     */
    private static long targetInterval;

    /**
     * 伝搬したブロックの数
     */
    private static long blockSum = 0;

    /**
     * 各ブロックの50%伝搬時間の平均
     */
    private static double average50PropagationTime = 0;

    /**
     * 各ブロックの90%伝搬時間の平均
     */
    private static double average90PropagationTime = 0;

    /**
     * 各ブロックの100%伝搬時間の平均
     */
    private static double average100PropagationTime = 0;

    /**
     * 各ブロックの平均伝搬時間の平均
     */
    private static double averageMeanPropagationTime = 0;

    /**
     * 各ブロックのハッシュレート重み付け平均伝搬時間の平均
     */
    private static double averageHashrateWeightedPropagationTime = 0;

    /**
     * 各マイナーのハッシュレートのリスト
     */
    private static final ArrayList<Long> hashrateList = new ArrayList<>();

    /**
     * ハッシュレートの合計
     */
    private static long hashrateSum = 0;

    /**
     * マイナーiがブロックを生成した時にマイナーjがそのブロックを受け取るまでの時間
     * row minter id, column nodes id
     */
    private static long[][] propagationTimeBetweenNodes = new long[getNumOfNodes()][getNumOfNodes()];

    /**
     * マイナーiが生成したブロックの数
     */
    private static ArrayList<Long> minerCount = new ArrayList<>();

    /**
     * Get simulated nodes list.
     *
     * @return the array list
     */
    public static ArrayList<Node> getSimulatedNodes() {
        return simulatedNodes;
    }

    /**
     * Get target block interval.
     *
     * @return the target block interval in milliseconds
     */
    public static long getTargetInterval() {
        return targetInterval;
    }

    /**
     * Sets the target block interval.
     *
     * @param interval - block interval in milliseconds
     */
    public static void setTargetInterval(long interval) {
        targetInterval = interval;
    }

    /**
     * Add node to the list of simulated nodes.
     *
     * @param node the node
     */
    public static void addNode(Node node) {
        simulatedNodes.add(node);
        hashrateList.add(node.getMiningPower());
        hashrateSum += node.getMiningPower();
        minerCount.add(0L);
    }

    /**
     * Remove node from the list of simulated nodes.
     *
     * @param node the node
     */
    @SuppressWarnings("unused")
    public static void removeNode(Node node) {
        simulatedNodes.remove(node);
    }

    /**
     * Add node to the list of simulated nodes and immediately try to add the new
     * node as a
     * neighbor to all simulated
     * nodes.
     *
     * @param node the node
     */
    @SuppressWarnings("unused")
    public static void addNodeWithConnection(Node node) {
        node.joinNetwork();
        addNode(node);
        for (Node existingNode : simulatedNodes) {
            existingNode.addNeighbor(node);
        }
    }

    /**
     * A list of observed {@link Block} instances.
     */
    private static final ArrayList<Block> observedBlocks = new ArrayList<>();

    static BasicLogger propagationLogger = BasicLogger.getLogger("simblock.propagation");

    static BasicLogger resultLogger = BasicLogger.getLogger("simblock.result");
    /**
     * A list of observed block propagation times. The map key represents the id of
     * the node that
     * has seen the
     * block, the value represents the difference between the current time and the
     * block minting
     * time, effectively
     * recording the absolute time it took for a node to witness the block.
     */
    private static final ArrayList<LinkedHashMap<Integer, Long>> observedPropagations = new ArrayList<>();

    /**
     * Handle the arrival of a new block. For every observed block, propagation
     * information is
     * updated, and for a new
     * block propagation information is created.
     *
     * @param block the block
     * @param node  the node
     */
    public static void arriveBlock(Block block, Node node) {
        // If block is already seen by any node
        if (observedBlocks.contains(block)) {
            // Get the propagation information for the current block
            LinkedHashMap<Integer, Long> propagation = observedPropagations.get(
                    observedBlocks.indexOf(block));
            // Update information for the new block
            propagation.put(node.getNodeID(), getCurrentTime() - block.getTime());
        } else {
            // If the block has not been seen by any node and there is no memory allocated
            // TODO move magic number to constant
            if (observedBlocks.size() > 10) {
                // After the observed blocks limit is reached, log and remove old blocks by FIFO
                // principle
                printPropagation(observedBlocks.get(0), observedPropagations.get(0));
                observedBlocks.remove(0);
                observedPropagations.remove(0);
            }
            // If the block has not been seen by any node and there is additional memory
            LinkedHashMap<Integer, Long> propagation = new LinkedHashMap<>();
            propagation.put(node.getNodeID(), getCurrentTime() - block.getTime());
            // Record the block as seen
            observedBlocks.add(block);
            // Record the propagation time
            observedPropagations.add(propagation);
        }
    }

    /**
     * Print propagation information about the propagation of the provided block in
     * the format:
     *
     * <p>
     * <em>node_ID, propagation_time</em>
     *
     * <p>
     * <em>propagation_time</em>: The time from when the block of the block ID is
     * generated to
     * when the
     * node of the <em>node_ID</em> is reached.
     *
     * @param block       the block
     * @param propagation the propagation of the provided block as a list of
     *                    {@link Node} IDs and
     *                    propagation times
     */
    public static void printPropagation(Block block, LinkedHashMap<Integer, Long> propagation) {
        // Print block and its height
        // TODO block does not have a toString method, what is printed here
        // System.out.println(block + ":" + block.getHeight());
        // for (Map.Entry<Integer, Long> timeEntry : propagation.entrySet()) {
        // System.out.println(timeEntry.getKey() + "," + timeEntry.getValue());
        // }
        // System.out.println();

        blockSum++;

        // ノードIDごとに列が揃えたpropagation time
        long[] nodePropagationTimes = new long[getNumOfNodes()];

        int minerIndex = block.getMinter().getNodeID() - 1;
        minerCount.set(minerIndex, minerCount.get(minerIndex) + 1);

        int index = 1;
        for (Map.Entry<Integer, Long> timeEntry : propagation.entrySet()) {
            int nodeIndex = timeEntry.getKey() - 1;
            long propagationTime = timeEntry.getValue();

            nodePropagationTimes[nodeIndex] = propagationTime;

            propagationTimeBetweenNodes[minerIndex][nodeIndex] += (double) propagationTime;

            if (index == (int) (getNumOfNodes() * 0.5)) {
                average50PropagationTime += (double) propagationTime;
            } else if (index == (int) (getNumOfNodes() * 0.9)) {
                average90PropagationTime += (double) propagationTime;
            } else if (index == getNumOfNodes()) {
                average100PropagationTime += (double) propagationTime;
            }
            averageMeanPropagationTime += (double) propagationTime / getNumOfNodes();
            averageHashrateWeightedPropagationTime += (double) propagationTime * hashrateList.get(nodeIndex)
                    / hashrateSum;

            index++;
        }

        // ノードIDごとに列を揃えたpropagation timeをcsvファイルにプリントする
        for (int i = 0; i < nodePropagationTimes.length; i++) {
            if (i < nodePropagationTimes.length - 1) {
                propagationLogger.print(Long.toString(nodePropagationTimes[i]) + ",");
            } else {
                propagationLogger.println(Long.toString(nodePropagationTimes[i]));
            }
        }

    }

    /**
     * Print propagation information about all blocks, internally relying on
     * {@link Simulator#printPropagation(Block, LinkedHashMap)}.
     */
    public static void printAllPropagation() {
        for (int i = 0; i < observedBlocks.size(); i++) {
            printPropagation(observedBlocks.get(i), observedPropagations.get(i));
        }
    }

    public static void printResult() {
        average50PropagationTime /= blockSum;
        average90PropagationTime /= blockSum;
        average100PropagationTime /= blockSum;
        averageMeanPropagationTime /= blockSum;
        averageHashrateWeightedPropagationTime /= blockSum;
        resultLogger.print("{\n");
        resultLogger.print("\"average-50-propagation-time\" :" + average50PropagationTime + ",\n");
        resultLogger.print("\"average-90-propagation-time\" :" + average90PropagationTime + ",\n");
        resultLogger.print("\"average-100-propagation-time\" :" + average100PropagationTime + ",\n");
        resultLogger.print("\"average-mean-propagation-time\" :" + averageMeanPropagationTime + ",\n");
        resultLogger
                .print("\"average-hashrate-weighted-propagation-time\" :" + averageHashrateWeightedPropagationTime
                        + ",\n");
        double[] winningRateList = { 0, 0.5, 1.0 };
        for (double winningRate : winningRateList) {
            ArrayList<Double> fairnessList = calculateFairness(winningRate);
            double sumFairness = 0;
            double maxFairness = -Double.MAX_VALUE;
            double minFairness = Double.MAX_VALUE;
            int maxFairnessIndex = 0;
            int minFairnessIndex = 0;

            for (int i = 0; i < fairnessList.size(); i++) {
                if (fairnessList.get(i) > 0) {
                    sumFairness += fairnessList.get(i);
                }
                if (fairnessList.get(i) > maxFairness) {
                    maxFairness = fairnessList.get(i);
                    maxFairnessIndex = i;
                }
                if (fairnessList.get(i) < minFairness) {
                    minFairness = fairnessList.get(i);
                    minFairnessIndex = i;
                }
            }
            maxFairness *= hashrateSum / hashrateList.get(maxFairnessIndex);
            minFairness *= hashrateSum / hashrateList.get(minFairnessIndex);

            resultLogger.print("\"sum-base-fairness-win" + winningRate + "\" :" + sumFairness + ",\n");
            resultLogger.print("\"max-base-fairness-win" + winningRate + "\" :" + maxFairness + ",\n");
            resultLogger.print("\"min-base-fairness-win" + winningRate + "\" :" + minFairness + ",\n");
            resultLogger.print("\"fairness-list-win" + winningRate + "\" :" + fairnessList + ",\n");
        }

        resultLogger.print("\"hashrate-list\" :" + hashrateList + ",\n");
        resultLogger.print("\"block-generation-count-list\" :" + minerCount);
        resultLogger.print("\n}");
    }

    private static ArrayList<Double> calculateFairness(double winningRate) {
        // マイナーiがブロックを生成した時にマイナーjがそのブロックを受け取るまでの時間の平均を計算
        for (int i = 0; i < propagationTimeBetweenNodes.length; i++) {
            if (minerCount.get(i) == 0)
                continue;
            for (int j = 0; j < propagationTimeBetweenNodes[i].length; j++) {
                propagationTimeBetweenNodes[i][j] /= minerCount.get(i);
            }
        }

        // 各マイナーについて、ほかのノードへのハッシュレート重み付け伝搬時間の和
        ArrayList<Long> hWPropSumList = new ArrayList<>();
        for (int i = 0; i < propagationTimeBetweenNodes.length; i++) {
            long hWPropSum = 0;
            for (int j = 0; j < propagationTimeBetweenNodes[i].length; j++) {
                hWPropSum += propagationTimeBetweenNodes[i][j] * hashrateList.get(j);
            }
            hWPropSum /= (double) hashrateSum;
            hWPropSumList.add(hWPropSum);
        }

        // 各マイナーがブロックを生成する確率、更新のため直近の結果を保持する2次元配列とする
        ArrayList<ArrayList<Double>> generateRate = new ArrayList<>();
        // ハッシュレートの割合で初期化
        for (int i = 0; i < minerCount.size(); i++) {
            ArrayList<Double> tmp = new ArrayList<>();
            tmp.add((double) hashrateList.get(i) / hashrateSum);
            tmp.add((double) hashrateList.get(i) / hashrateSum);
            generateRate.add(tmp);
        }

        // 定常分布を求める
        int loopCount = 1000;
        for (int i = 0; i < loopCount; i++) {
            for (int j = 0; j < getNumOfNodes(); j++) {
                double updateFactor = 0;
                for (int k = 0; k < getNumOfNodes(); k++) {
                    updateFactor += generateRate.get(k).get((i + 1) % 2)
                            * (targetInterval - propagationTimeBetweenNodes[k][j] + hWPropSumList.get(k));
                }
                updateFactor *= hashrateList.get(j);
                updateFactor /= targetInterval * hashrateSum;
                generateRate.get(j).set(i % 2, updateFactor);
            }
        }

        // fairnessを計算
        ArrayList<Double> fairnessList = new ArrayList<>();
        for (int i = 0; i < getNumOfNodes(); i++) {
            double fairness = generateRate.get(i).get(loopCount % 2)
                    * (targetInterval - (1 - winningRate) * hWPropSumList.get(i));
            for (int j = 0; j < getNumOfNodes(); j++) {
                fairness += generateRate.get(j).get(loopCount % 2) * (1 - winningRate)
                        * ((double) hashrateList.get(i) / hashrateSum)
                        * propagationTimeBetweenNodes[j][i];
            }
            fairness /= targetInterval;
            fairness -= (double) hashrateList.get(i) / hashrateSum;
            fairnessList.add(fairness);
        }

        return fairnessList;

    }
}
