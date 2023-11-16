public class BettingProcessorTest {
    public static void main(String[] args) {
        // Create an instance of BettingProcessor
        BettingProcessor bettingProcessor = new BettingProcessor();

        // Process player data
        bettingProcessor.processPlayerData("src/main/resources/player_data.txt");

        // Process match data
        bettingProcessor.processMatchData("src/main/resources/match_data.txt");

        // Write results to a temporary file
        bettingProcessor.writeResults("src/main/resources/temp_results.txt");

        // Print the content of the temporary file
        System.out.println("Results:");
        FileUtil.printFileContent("src/main/resources/temp_results.txt");
    }
}
