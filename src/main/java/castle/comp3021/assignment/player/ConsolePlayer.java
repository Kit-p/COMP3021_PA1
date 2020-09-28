package castle.comp3021.assignment.player;

import castle.comp3021.assignment.piece.Archer;
import castle.comp3021.assignment.piece.Knight;
import castle.comp3021.assignment.protocol.*;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The player that makes move according to user input from console.
 */
public class ConsolePlayer extends Player {
    public ConsolePlayer(String name, Color color) {
        super(name, color);
    }

    public ConsolePlayer(String name) {
        this(name, Color.GREEN);
    }

    /**
     * Choose a move from available moves.
     * This method will be called by {@link Game} object to get the move that the player wants to make when it is the
     * player's turn.
     * <p>
     * {@link ConsolePlayer} returns a move according to user's input in the console.
     * The console input format should conform the format described in the assignment description.
     * (e.g. {@literal a1->b3} means move the {@link Piece} at {@link Place}(x=0,y=0) to {@link Place}(x=1,y=2))
     * Note that in the {@link Game}.board, the index starts from 0 in both x and y dimension, while in the console
     * display, x dimension index starts from 'a' and y dimension index starts from 1.
     * <p>
     * Hint: be sure to handle invalid input to avoid invalid {@link Move}s.
     * <p>
     * <strong>Attention: Student should make sure the {@link Move} returned is valid.</strong>
     * <p>
     * <strong>Attention: {@link Place} object uses integer as index of x and y-axis, both starting from 0 to
     * facilitate programming.
     * This is VERY different from the coordinate used in console display.</strong>
     *
     * @param game           the current game object
     * @param availableMoves available moves for this player to choose from.
     * @return the chosen move
     */
    @Override
    public @NotNull Move nextMove(Game game, Move[] availableMoves) {
        // TODO student implementation
        Scanner sc = new Scanner(System.in);
        String input = null;
        Pattern pattern = Pattern.compile("^\\s*([a-z])(\\d+)\\s*->\\s*([a-z])(\\d+)\\s*$");
        int sourceX, sourceY, destinationX, destinationY;
        Move move = null;
        Configuration configuration = game.getConfiguration();
        int size = configuration.getSize();
        String invalidInputText = "[Invalid Move]: ";
        String invalidInputCause = null;

        while (true) {
            System.out.print("[" + this.name + "] Make a Move: ");
            input = sc.nextLine();
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                sourceX = matcher.group(1).charAt(0) - 'a';
                sourceY = Integer.parseInt(matcher.group(2)) - 1;
                destinationX = matcher.group(3).charAt(0) - 'a';
                destinationY = Integer.parseInt(matcher.group(4)) - 1;
                move = new Move(sourceX, sourceY, destinationX, destinationY);
                if (destinationX >= size || destinationY >= size
                        || sourceX >= size || sourceY >= size
                        || destinationX < 0 || destinationY < 0
                        || sourceX < 0 || sourceY < 0) {
                    invalidInputCause = "place is out of boundary of gameboard";
                } else if (Arrays.stream(availableMoves).noneMatch(move::equals) || !validateMove(game, move)) {
                    invalidInputCause = "piece move rule is violated";
                } else {
                    break;
                }
            } else {
                invalidInputCause = "Incorrect format";
            }

            System.out.println(invalidInputText + invalidInputCause);
        }

        return move;
    }

    /**
     * Validate a move
     * This is a utility method private to this class.
     * This method must be called this.nextMove().
     * It will check the boundaries of the gameboard, move protection, the rules specific to the piece, and the invoking player.
     *
     * @param game   the game object
     * @param move   the move to be validated
     * @return a boolean to indicate the validity of the move
     */
    private boolean validateMove(Game game, Move move) {
        if (game == null || move == null) {
            return false;
        }

        Configuration configuration = game.getConfiguration();
        int numMoves = game.getNumMoves();
        Place source = move.getSource();
        int sourceX = source.x();
        int sourceY = source.y();
        Place destination = move.getDestination();
        int destinationX = destination.x();
        int destinationY = destination.y();
        int distanceX = Math.abs(destinationX - sourceX);
        int distanceY = Math.abs(destinationY - sourceY);

        // validate coordinates
        int size = configuration.getSize();
        if (destinationX >= size || destinationY >= size
                || sourceX >= size || sourceY >= size
                || destinationX < 0 || destinationY < 0
                || sourceX < 0 || sourceY < 0) {
            return false;
        }
        if (distanceX == 0 && distanceY == 0) {
            return false;
        }

        Piece piece = game.getPiece(sourceX, sourceY);
        if (piece == null || !piece.getPlayer().equals(this)) {
            return false;
        }

        Piece capturedPiece = game.getPiece(destinationX, destinationY);
        if (capturedPiece != null && (numMoves < configuration.getNumMovesProtection()
                || capturedPiece.getPlayer().equals(this))) {
            return false;
        }

        // validate move
        if (piece instanceof Knight) {
            boolean isMoveHorizontal = (distanceX == 2 && distanceY == 1);
            boolean isMoveVertical = (distanceX == 1 && distanceY == 2);
            int blockingX, blockingY;
            if (isMoveHorizontal) {
                blockingX = (sourceX + destinationX) / 2;
                blockingY = sourceY;
            } else if (isMoveVertical) {
                blockingX = sourceX;
                blockingY = (sourceY + destinationY) / 2;
            } else {
                return false;
            }
            if (game.getPiece(blockingX, blockingY) != null) {
                return false;
            }
        } else if (piece instanceof Archer) {
            boolean isMoveHorizontal = (distanceY == 0);
            boolean isMoveVertical = (distanceX == 0);
            int start, end;
            int inBetweenPieceCount = 0;
            if (isMoveHorizontal) {
                if (sourceX < destinationX) {
                    start = sourceX;
                    end = destinationX;
                } else {
                    start = destinationX;
                    end = sourceX;
                }
                for (int x = start + 1; x < end; x++) {
                    if (game.getPiece(x, sourceY) != null) {
                        inBetweenPieceCount++;
                    }
                    if (inBetweenPieceCount > 1) {
                        break;
                    }
                }
            } else if (isMoveVertical) {
                if (sourceY < destinationY) {
                    start = sourceY;
                    end = destinationY;
                } else {
                    start = destinationY;
                    end = sourceY;
                }
                for (int y = start + 1; y < end; y++) {
                    if (game.getPiece(sourceX, y) != null) {
                        inBetweenPieceCount++;
                    }
                    if (inBetweenPieceCount > 1) {
                        break;
                    }
                }
            } else {
                return false;
            }
            if (capturedPiece != null && inBetweenPieceCount != 1) {
                return false;
            } else if (capturedPiece == null && inBetweenPieceCount != 0) {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }
}
