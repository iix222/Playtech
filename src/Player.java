/**
 * @author Igor Grišin
 * @Date 11/14/2023
 */
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Player {
    UUID id;
    long balance;
    int betsPlaced;
    int betsWon;
    List<Action> actions;

    public Player(UUID id) {
        this.id = id;
        this.balance = 0;
        this.betsPlaced = 0;
        this.betsWon = 0;
        this.actions = new ArrayList<>();
    }

    public void deposit(int amount) {
        balance += amount;
    }

    public boolean withdraw(int amount) {
        if (amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public void placeBet(int amount, boolean won) {
        balance += (won ? amount : -amount);
        betsPlaced++;
        if (won) {
            betsWon++;
        }
    }

    public BigDecimal getWinRate() {
        if (betsPlaced == 0) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(betsWon)
                .divide(new BigDecimal(betsPlaced), 2, RoundingMode.HALF_UP);
    }

    public boolean hasIllegalAction() {
        return actions.stream().anyMatch(Action::isIllegal);
    }

    public Action getFirstIllegalAction() {
        return actions.stream()
                .filter(Action::isIllegal)
                .findFirst()
                .orElse(null);
    }

    public void addIllegalAction(String operation, UUID matchId, int coinNumber, String side) {
        actions.add(new Action(operation, matchId, coinNumber, side));
    }
}
