package castle.comp3021.assignment.piece;

import castle.comp3021.assignment.protocol.*;

import java.util.ArrayList;

/**
 * Knight piece that moves similar to knight in chess.
 * Rules of move of Knight can be found in wikipedia (https://en.wikipedia.org/wiki/Knight_(chess)).
 *
 * @see <a href='https://en.wikipedia.org/wiki/Knight_(chess)'>Wikipedia</a>
 */
public class Knight extends Piece {
    public Knight(Player player) {
        super(player);
    }

    @Override
    public char getLabel() {
        return 'K';
    }

    /**
     * Returns an array of moves that are valid given the current place of the piece.
     * Given the {@link Game} object and the {@link Place} that current knight piece locates, this method should
     * return ALL VALID {@link Move}s according to the current {@link Place} of this knight piece.
     * All the returned {@link Move} should have source equal to the source parameter.
     * <p>
     * Hint: you should consider corner cases when the {@link Move} is not valid on the gameboard.
     * Several tests are provided and your implementation should pass them.
     * <p>
     * <strong>Attention: Student should make sure all {@link Move}s returned are valid.</strong>
     *
     * @param game   the game object
     * @param source the current place of the piece
     * @return an array of available moves
     */
    @Override
    public Move[] getAvailableMoves(Game game, Place source) {
        // TODO student implementation
        ArrayList<Move> allAvailableMoves = new ArrayList<>();
        int sourceX = source.x();
        int sourceY = source.y();
        int shortEdge = 1;
        int longEdge = 2;
        int coefficientA = -1;
        int coefficientB = -1;
        for (int i = 0; i < 4; i++) {
            int destinationX = sourceX + coefficientA * shortEdge;
            int destinationY = sourceY + coefficientB * longEdge;
            Move move = new Move(source, destinationX, destinationY);
            if (validateMove(game, move)) {
                allAvailableMoves.add(move);
            }
            destinationX = sourceX + coefficientA * longEdge;
            destinationY = sourceY + coefficientB * shortEdge;
            move = new Move(source, destinationX, destinationY);
            if (validateMove(game, move)) {
                allAvailableMoves.add(move);
            }
            if (i % 2 == 0) {
                coefficientA *= -1;
            } else {
                coefficientB *= -1;
            }
        }
        return allAvailableMoves.toArray(new Move[0]);
    }

    /**
     * Validate a move
     * This is a utility method private to this class.
     * This method must be called in this.getAvailableMoves().
     * It will check boundaries of the gameboard, move protection and the rules specific to Knight.
     *
     * @param game the game object
     * @param move the move to be validated
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

        Piece capturedPiece = game.getPiece(destinationX, destinationY);
        if (capturedPiece != null && (numMoves < configuration.getNumMovesProtection()
                || capturedPiece.getPlayer().equals(this.getPlayer()))) {
            return false;
        }

        // validate move
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

        return true;
    }
}
