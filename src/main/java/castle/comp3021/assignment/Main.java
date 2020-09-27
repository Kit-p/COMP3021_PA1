package castle.comp3021.assignment;

import castle.comp3021.assignment.piece.Archer;
import castle.comp3021.assignment.piece.Knight;
import castle.comp3021.assignment.player.ConsolePlayer;
import castle.comp3021.assignment.player.RandomPlayer;
import castle.comp3021.assignment.protocol.Configuration;
import castle.comp3021.assignment.protocol.Game;
import castle.comp3021.assignment.protocol.Piece;
import castle.comp3021.assignment.protocol.Player;
import castle.comp3021.assignment.protocol.exception.InvalidConfigurationError;

public class Main {
    /**
     * Create and initialize a game.
     * Student should first construct a {@link Configuration} object with two players.
     * Then add initial pieces to gameboard in the {@link Configuration} object.
     * After that, use this {@link Configuration} object to construct a {@link JesonMor} game object
     * This method should return the created {@link JesonMor} object.
     * <p>
     * The initialized gameboard should comply to the requirements of the assignment.
     * The user player should be put
     * first in the player array in the configuration and user player should moves first.
     *
     * <strong>Attention: The code in this method is only an example of using {@link Configuration} to initialize
     * gameboard, Students should remove them and implement on their own.</strong>
     *
     * @param size               size of gameboard
     * @param numMovesProtection number of moves with capture protection
     * @return the game object
     */
    public static Game createGame(int size, int numMovesProtection) {
        // TODO student implementation
        // validate arguments
        if (size < 3) {
            throw new InvalidConfigurationError("size of gameboard must be at least 3");
        } else if (size % 2 == 0) {
            throw new InvalidConfigurationError("size of gameboard must be an odd number");
        } else if (size > 26) {
            throw new InvalidConfigurationError("size of gameboard is at most 26");
        }
        if (numMovesProtection < 0) {
            throw new InvalidConfigurationError("number of moves with capture protection cannot be negative");
        }

        Player userPlayer = new ConsolePlayer("White");
        Player computerPlayer = new RandomPlayer("Black");
        Player[] players = new Player[]{userPlayer, computerPlayer};
        Configuration configuration = new Configuration(size, players, numMovesProtection);

        for (int x = 0; x < size; x++) {
            Piece userPiece, computerPiece;
            if (x % 2 == 0) {
                // create Knight on even number x-coordinate
                userPiece = new Knight(userPlayer);
                computerPiece = new Knight(computerPlayer);
            } else {
                // create Archer on odd number x-coordinate
                userPiece = new Archer(userPlayer);
                computerPiece = new Archer(computerPlayer);
            }
            configuration.addInitialPiece(userPiece, x, 0);
            configuration.addInitialPiece(computerPiece, x, size - 1);
        }

        return new JesonMor(configuration);
    }

    public static void main(String[] args) {
        // start the game
        var helper = "two integer arguments are required specifying size of gameboard and number of moves with capturing protection ";
        if (args.length < 2) {
            throw new IllegalArgumentException(helper);
        }
        int size;
        int numMovesProtection;
        try {
            size = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("the first argument is not a number");
        }
        try {
            numMovesProtection = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("the second argument is not a number");
        }
        createGame(size, numMovesProtection).start();
    }
}
