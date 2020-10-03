package castle.comp3021.assignment;

import castle.comp3021.assignment.mock.MockPiece;
import castle.comp3021.assignment.mock.MockPlayer;
import castle.comp3021.assignment.piece.*;
import castle.comp3021.assignment.player.ConsolePlayer;
import castle.comp3021.assignment.player.RandomPlayer;
import castle.comp3021.assignment.protocol.*;
import castle.comp3021.assignment.protocol.exception.InvalidConfigurationError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Put your additional JUnit5 tests for Bonus Task 2 in this class.
 */
public class AdditionalTests {
    /**
     * Test arguments passed into Main.main()
     */
    @ParameterizedTest
    @ValueSource(strings = {
            "9",
            "Nine, 5",
            "9, Five"
    })
    public void testMainArgumentValidation(String argString) {
        String[] args = argString.split("\\s*,\\s*");
        assertThrows(IllegalArgumentException.class, () -> Main.main(args));
    }

    /**
     * Test arguments passed into Main.createGame()
     */
    @ParameterizedTest
    @CsvSource({
            "1, 0",
            "4, 0",
            "27, 0",
            "25, -1"
    })
    public void testCreateGameArgumentValidation(int size, int numMovesProtection) {
        assertThrows(InvalidConfigurationError.class, () -> Main.createGame(size, numMovesProtection));
    }

    /**
     * Test arguments passed into JesonMor.updateScore()
     */
    @Test
    public void testUpdateScoreArgumentValidation() {
        MockPlayer mockPlayer = new MockPlayer();
        MockPiece mockPiece = new MockPiece(mockPlayer);
        Move mockMove = new Move(new Place(0, 0), new Place(1, 0));
        Configuration config = new Configuration(5, new Player[]{mockPlayer, new MockPlayer()});
        config.addInitialPiece(mockPiece, 0, 0);
        JesonMor game = new JesonMor(config);
        int oldScore = mockPlayer.getScore();
        game.updateScore(null, mockPiece, mockMove);
        assertEquals(oldScore, mockPlayer.getScore());
        game.updateScore(mockPlayer, null, mockMove);
        assertEquals(oldScore, mockPlayer.getScore());
        game.updateScore(mockPlayer, mockPiece, null);
        assertEquals(oldScore, mockPlayer.getScore());
    }

    /**
     * Test arguments passed into JesonMor.getWinner()
     */
    @Test
    public void testGetWinnerArgumentValidation() {
        MockPlayer mockPlayer = new MockPlayer();
        MockPiece mockPiece = new MockPiece(mockPlayer);
        Move mockMove = new Move(new Place(0, 0), new Place(1, 0));
        Configuration config = new Configuration(5, new Player[]{mockPlayer, new MockPlayer()});
        config.addInitialPiece(mockPiece, 0, 0);
        JesonMor game = new JesonMor(config);
        assertNull(game.getWinner(null, mockPiece, mockMove));
        assertNull(game.getWinner(mockPlayer, null, mockMove));
        assertNull(game.getWinner(mockPlayer, mockPiece, null));
    }

    /**
     * Test JesonMor.getWinner() win by capture all enemy pieces
     */
    @Test
    public void testGetWinnerCaptureAllEnemyPiece() {
        MockPlayer winner = new MockPlayer();
        MockPlayer loser = new MockPlayer();
        MockPiece capturingPiece = new MockPiece(winner);
        MockPiece capturedPiece = new MockPiece(loser);
        Move capturingMove = new Move(new Place(0, 0), new Place(1, 0));
        Configuration config = new Configuration(3, new Player[]{winner, loser}, 0);
        config.addInitialPiece(capturingPiece, 0, 0);
        config.addInitialPiece(capturedPiece, 1, 0);
        JesonMor game = new JesonMor(config);
        winner.setNextMoves(new Move[]{capturingMove});
        assertEquals(winner, game.start());
    }

    /**
     * Test arguments passed into JesonMor.getAvailableMoves()
     */
    @Test
    public void testGetAvailableMovesArgumentValidation1() {
        MockPlayer mockPlayer = new MockPlayer();
        MockPiece mockPiece = new MockPiece(mockPlayer);
        Configuration config = new Configuration(5, new Player[]{mockPlayer, new MockPlayer()});
        config.addInitialPiece(mockPiece, 0, 0);
        JesonMor game = new JesonMor(config);
        Move[] emptyMoves = new Move[0];
        assertArrayEquals(emptyMoves, game.getAvailableMoves(null));
    }

    /**
     * Test arguments passed into Archer.getAvailableMoves()
     */
    @Test
    public void testGetAvailableMovesArgumentValidation2() {
        MockPlayer mockPlayer = new MockPlayer();
        Piece archerPiece = new Archer(mockPlayer);
        Configuration config = new Configuration(5, new Player[]{mockPlayer, new MockPlayer()});
        config.addInitialPiece(archerPiece, 0, 0);
        JesonMor game = new JesonMor(config);
        Move[] emptyMoves = new Move[0];
        assertArrayEquals(emptyMoves, archerPiece.getAvailableMoves(null, null));
        assertArrayEquals(emptyMoves, archerPiece.getAvailableMoves(game, null));
        assertArrayEquals(emptyMoves, archerPiece.getAvailableMoves(game, new Place(1, 0)));
    }

    /**
     * Test arguments passed into Knight.getAvailableMoves()
     */
    @Test
    public void testGetAvailableMovesArgumentValidation3() {
        MockPlayer mockPlayer = new MockPlayer();
        Piece knightPiece = new Knight(mockPlayer);
        Configuration config = new Configuration(5, new Player[]{mockPlayer, new MockPlayer()});
        config.addInitialPiece(knightPiece, 0, 0);
        JesonMor game = new JesonMor(config);
        Move[] emptyMoves = new Move[0];
        assertArrayEquals(emptyMoves, knightPiece.getAvailableMoves(null, null));
        assertArrayEquals(emptyMoves, knightPiece.getAvailableMoves(game, null));
        assertArrayEquals(emptyMoves, knightPiece.getAvailableMoves(game, new Place(1, 0)));
    }

    /**
     * Test arguments passed into ConsolePlayer.nextMove()
     */
    @Test
    public void testNextMoveArgumentValidation1() {
        ConsolePlayer userPlayer = new ConsolePlayer("UserPlayer");
        MockPiece mockPiece = new MockPiece(userPlayer);
        Configuration config = new Configuration(5, new Player[]{userPlayer, new MockPlayer()});
        config.addInitialPiece(mockPiece, 0, 0);
        JesonMor game = new JesonMor(config);
        assertThrows(Exception.class, () -> userPlayer.nextMove(null, null));
        assertThrows(Exception.class, () -> userPlayer.nextMove(game, null));
        assertThrows(Exception.class, () -> userPlayer.nextMove(game, new Move[0]));
    }

    /**
     * Test arguments passed into RandomPlayer.nextMove()
     */
    @Test
    public void testNextMoveArgumentValidation2() {
        RandomPlayer computerPlayer = new RandomPlayer("RandomPlayer");
        MockPiece mockPiece = new MockPiece(computerPlayer);
        Configuration config = new Configuration(5, new Player[]{computerPlayer, new MockPlayer()});
        config.addInitialPiece(mockPiece, 0, 0);
        JesonMor game = new JesonMor(config);
        assertThrows(Exception.class, () -> computerPlayer.nextMove(null, null));
        assertThrows(Exception.class, () -> computerPlayer.nextMove(game, null));
        assertThrows(Exception.class, () -> computerPlayer.nextMove(game, new Move[0]));
    }

    /**
     * Test Configuration.addInitialPiece() add piece at central place
     */
    @Test
    public void testAddInitialPieceAddAtCentralPlace() {
        MockPlayer mockPlayer = new MockPlayer();
        MockPiece mockPiece = new MockPiece(mockPlayer);
        Configuration config = new Configuration(3, new Player[]{mockPlayer, new MockPlayer()});
        Place centralPlace = config.getCentralPlace();
        assertThrows(InvalidConfigurationError.class, () -> config.addInitialPiece(mockPiece, centralPlace));
    }

    /**
     * Test Configuration.addInitialPiece() add piece outside boundary
     */
    @Test
    public void testAddInitialPieceAddOutsideBoundary() {
        MockPlayer mockPlayer = new MockPlayer();
        MockPiece mockPiece = new MockPiece(mockPlayer);
        Configuration config = new Configuration(3, new Player[]{mockPlayer, new MockPlayer()});
        assertThrows(InvalidConfigurationError.class, () -> config.addInitialPiece(mockPiece,99, 0));
        assertThrows(InvalidConfigurationError.class, () -> config.addInitialPiece(mockPiece, 0, 99));
    }

    /**
     * Test Game.getPlayers()
     */
    @Test
    public void testGetPlayers() {
        Configuration config = new Configuration(3, new Player[]{new MockPlayer(), new MockPlayer()});
        JesonMor game = new JesonMor(config);
        assertEquals(config.getPlayers(), game.getPlayers());
    }

    /**
     * Test ConsolePlayer.nextMove()
     */
    @Test
    public void testNextMove() {
        String data = "a 1 - > a 2\r\nz1->z2\r\na27->a28\r\na1->z2\r\na1->a28\r\na1->a3\r\na1->a2\r\n";
        InputStream stdin = System.in;
        try {
            System.setIn(new ByteArrayInputStream(data.getBytes()));
            var player1 = new MockPlayer(Color.PURPLE);
            var player2 = new ConsolePlayer("RandomPlayer");
            var config = new Configuration(3, new Player[]{player1, player2});
            var piece1 = new MockPiece(player1);
            var piece2 = new MockPiece(player2);
            config.addInitialPiece(piece1, 2, 2);
            config.addInitialPiece(piece2, 0, 0);
            var game = new JesonMor(config);
            var move = player2.nextMove(game, game.getAvailableMoves(player2));
            assertEquals(new Move(0, 0, 0, 1), move);
        } finally {
            System.setIn(stdin);
        }
    }
}