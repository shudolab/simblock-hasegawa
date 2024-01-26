package simblock.settings;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class SimulatorConfigulation {
    private static Properties properties = new Properties();

    public SimulatorConfigulation() {

    }

    public static void readProperties(String filePath) {
        // プロパティファイルの読み込み
        try (FileReader reader = new FileReader(filePath)) {
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The number of nodes participating in the blockchain network.
     */
    public static int getNumOfNodes() {
        return Integer.parseInt(properties.getProperty("NUM_OF_NODES"));
    }

    /**
     * The block height when a simulation ends.
     */
    public static int getEndBlockHeight() {
        return Integer.parseInt(properties.getProperty("END_BLOCK_HEIGHT"));
    }

    /**
     * The usage rate of compact block relay (CBR) protocol.
     */
    public static float getCBRUsageRate() {
        return Float.parseFloat(properties.getProperty("CBR_USAGE_RATE"));
    }

    /**
     * The expected value of block generation interval. The difficulty of mining is
     * automatically
     * adjusted by this value and the sum of mining power. (unit: millisecond)
     */
    public static long getInterval() {
        return Long.parseLong(properties.getProperty("INTERVAL"));
    }

    /**
     * The average mining power of each node. Mining power corresponds to Hash Rate
     * in Bitcoin, and
     * is the number of mining (hash calculation) executed per millisecond.
     */
    public static int getAverageMiningPower() {
        return Integer.parseInt(properties.getProperty("AVERAGE_MINING_POWER"));
    }

    /**
     * The mining power of each node is determined randomly according to the normal
     * distribution
     * whose average is AVERAGE_MINING_POWER and standard deviation is
     * STDEV_OF_MINING_POWER.
     */
    public static int getStdevOfMiningPower() {
        return Integer.parseInt(properties.getProperty("STDEV_OF_MINING_POWER"));
    }

    public static double getGossipProbability() {
        return Double.parseDouble(properties.getProperty("GOSSIP_PROBABILITY"));
    }

    public static double getGossipPenaltyRate() {
        return Double.parseDouble(properties.getProperty("GOSSIP_PENALTY_RATE"));
    }
}
