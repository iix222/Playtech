/**
 * @author Igor Grišin
 * @Date 11/16/2023
 */
import java.util.UUID;

public class Action {
    String operation;
    UUID matchId;
    int coinNumber;
    String side;

    public Action(String operation, UUID matchId, int coinNumber, String side) {
        this.operation = operation;
        this.matchId = matchId;
        this.coinNumber = coinNumber;
        this.side = side;
    }

    public boolean isIllegal() {
        return operation.equals("BET") && matchId != null && side != null;
    }
}
