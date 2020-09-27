package castle.comp3021.assignment.player;

import castle.comp3021.assignment.piece.Archer;
import castle.comp3021.assignment.piece.Knight;
import castle.comp3021.assignment.protocol.*;
import org.jetbrains.annotations.NotNull;

/**
 * A computer player that makes a move randomly.
 */
public class RandomPlayer extends Player {
    public RandomPlayer(String name, Color color) {
        super(name, color);
    }

    public RandomPlayer(String name) {
        this(name, Color.BLUE);
    }

    /**
     * Choose a move from available moves.
     * This method will be called by {@link Game} object to get the move that the player wants to make when it is the
     * player's turn.
     * <p>
     * {@link RandomPlayer} chooses a move from available ones randomly.
     * <p>
     * <strong>Attention: Student should make sure the {@link Move} returned is valid.</strong>
     *
     * @param game           the current game object
     * @param availableMoves available moves for this player to choose from.
     * @return the chosen move
     */
    @Override
    public @NotNull Move nextMove(Game game, Move[] availableMoves) {
        // TODO student implementation
        int index;
        while (true) {
            index = (int)(Math.random() * availableMoves.length);
            if (validateMove(game, availableMoves[index])) {
                break;
            }
        }
        return availableMoves[index];
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
        Piece piece = game.getPiece(sourceX, sourceY);

        if (piece == null || !piece.getPlayer().equals(this)) {
            return false;
        }

        Piece capturedPiece = game.getPiece(destinationX, destinationY);
        if (capturedPiece != null && (numMoves < configuration.getNumMovesProtection()
                || capturedPiece.getPlayer().equals(this))) {
            return false;
        }

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
                for (int x = start; x < end; x++) {
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
                for (int y = start; y < end; y++) {
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
