/**
 * @author Igor Grišin
 * @Date 11/14/2023
 */

import java.io.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;


public class BettingProcessor {
    private final Map<UUID, Player> players = new HashMap<>();
    private final Map<UUID, Match> matches = new HashMap<>();
    private int casinoBalance = 0;

    private static final Logger logger = Logger.getLogger(BettingProcessor.class.getName());

    public void processPlayerData(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                UUID playerId = UUID.fromString(parts[0]);
                String operation = parts[1];
                UUID matchId = parts.length > 2 && !parts[2].isEmpty() ? UUID.fromString(parts[2]) : null;
                int coinNumber = Integer.parseInt(parts[3]);

                Player player = players.computeIfAbsent(playerId, Player::new);

                processOperation(player, operation, matchId, coinNumber, parts);
            }
        } catch (IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading player data file", e);
        }
    }

    public void processMatchData(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                UUID matchId = UUID.fromString(parts[0]);
                double rateA = Double.parseDouble(parts[1]);
                double rateB = Double.parseDouble(parts[2]);
                String result = parts[3];

                matches.put(matchId, new Match(matchId, rateA, rateB, result));
            }
        } catch (IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error reading match data file", e);
        }
    }

    public void writeResults(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writeLegitimatePlayers(writer);
            writeIllegitimatePlayers(writer);
            writeCasinoBalance(writer);
        } catch (IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error writing results file", e);
        }
    }

    private void writeLegitimatePlayers(PrintWriter writer) {
        writer.println("LEGITIMATE PLAYERS");
        players.values().stream()
                .sorted(Comparator.comparing(p -> p.id.toString()))
                .forEach(player -> writer.println(player.id + " " + player.balance + " " + player.getWinRate()));
        writer.println();
    }

    private void writeIllegitimatePlayers(PrintWriter writer) {
        writer.println("ILLEGITIMATE PLAYERS");
        players.values().stream()
                .filter(Player::hasIllegalAction)
                .sorted(Comparator.comparing(p -> p.id.toString()))
                .forEach(player -> {
                    Action firstIllegalAction = player.getFirstIllegalAction();
                    writer.println(player.id + " " + firstIllegalAction.operation + " " +
                            (firstIllegalAction.matchId != null ? firstIllegalAction.matchId : "null") +
                            " " + firstIllegalAction.coinNumber +
                            " " + (firstIllegalAction.side != null ? firstIllegalAction.side : "null"));
                });
        writer.println();
    }

    private void writeCasinoBalance(PrintWriter writer) {
        writer.println("CASINO BALANCE CHANGES");
        writer.println(casinoBalance);
    }

    private void processOperation(Player player, String operation, UUID matchId, int coinNumber, String[] parts) {
        switch (operation) {
            case "DEPOSIT" -> player.deposit(coinNumber);
            case "BET" -> {
                String side = parts[4];
                processBet(player, matchId, coinNumber, side);
            }
            case "WITHDRAW" -> player.withdraw(coinNumber);
            default -> {
                logger.log(java.util.logging.Level.WARNING, "Unknown operation: {0}", operation);
            }
        }
    }

    private void processBet(Player player, UUID matchId, int coinNumber, String side) {
        Match match = matches.get(matchId);
        if (match != null) {
            if (coinNumber <= player.balance) {
                player.placeBet(coinNumber, match.getResult().equals(side));
                updateCasinoBalance(match.getResult().equals(side) ? coinNumber : -coinNumber);
            } else {
                player.addIllegalAction("BET", matchId, coinNumber, side);
            }
        } else {
            player.addIllegalAction("BET", matchId, coinNumber, side);
        }
    }

    private void updateCasinoBalance(int change) {
        casinoBalance += change;
    }
}
