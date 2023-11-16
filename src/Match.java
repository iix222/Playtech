/**
 * @author Igor Grišin
 * @Date 11/14/2023
 */
import java.util.UUID;

public class Match {
    UUID id;
    double rateA;
    double rateB;
    String result;

    public Match(UUID id, double rateA, double rateB, String result) {
        this.id = id;
        this.rateA = rateA;
        this.rateB = rateB;
        this.result = result;
    }

    public String getResult() {
        return result;
    }


}
