/**
 * @author Igor Grišin
 * @Date ${DATE}
 */
public class Main {
    public static void main(String[] args) {
        BettingProcessor processor = new BettingProcessor();
        processor.processPlayerData("player_data.txt");
        processor.processMatchData("match_data.txt");
        processor.writeResults("results.txt");
    }
}