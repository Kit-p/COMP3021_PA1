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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
        Configuration config = new Configuration(3, new Player[]{winner, loser}, 2);
        config.addInitialPiece(capturingPiece, 0, 0);
        config.addInitialPiece(capturedPiece, 1, 0);
        JesonMor game = new JesonMor(config);
        winner.setNextMoves(new Move[]{
                new Move(new Place(0, 0), new Place(0, 1)),
                new Move(new Place(0, 1), new Place(1, 1))
        });
        loser.setNextMoves(new Move[]{
                new Move(new Place(1, 0), new Place(1, 1))
        });
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
        assertArrayEquals(emptyMoves, archerPiece.getAvailableMoves(game, new Place(-1, 0)));
        assertArrayEquals(emptyMoves, archerPiece.getAvailableMoves(game, new Place(0, -1)));
        assertArrayEquals(emptyMoves, archerPiece.getAvailableMoves(game, new Place(99, 0)));
        assertArrayEquals(emptyMoves, archerPiece.getAvailableMoves(game, new Place(0, 99)));
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
        assertArrayEquals(emptyMoves, knightPiece.getAvailableMoves(game, new Place(-1, 0)));
        assertArrayEquals(emptyMoves, knightPiece.getAvailableMoves(game, new Place(0, -1)));
        assertArrayEquals(emptyMoves, knightPiece.getAvailableMoves(game, new Place(99, 0)));
        assertArrayEquals(emptyMoves, knightPiece.getAvailableMoves(game, new Place(0, 99)));
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
     * Test ConsolePlayer.nextMove() input validation
     */
    @Test
    public void testNextMoveInputValidation() {
        String data = "a1- >a2\r\nZ27 ->    Y36\r\na1->B3\r\na1->c2\r\n";
        InputStream stdin = System.in;
        try {
            System.setIn(new ByteArrayInputStream(data.getBytes()));
            ConsolePlayer consolePlayer = new ConsolePlayer("ConsolePlayer");
            Knight knight = new Knight(consolePlayer);
            Configuration config = new Configuration(3, new Player[]{consolePlayer, new MockPlayer()});
            config.addInitialPiece(knight, 0, 0);
            config.addInitialPiece(new MockPiece(consolePlayer), 0, 1);
            JesonMor game = new JesonMor(config);
            Move chosenMove = consolePlayer.nextMove(game, game.getAvailableMoves(consolePlayer));
            assertEquals(new Move(new Place(0, 0), new Place(2, 1)), chosenMove);
        } finally {
            System.setIn(stdin);
        }
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
     * Test Move functions
     */
    @Test
    public void testMove() throws CloneNotSupportedException {
        Place source1 = new Place(1, 0);
        Place source2 = new Place(0, 1);
        Place destination1 = new Place(0, 0);
        Place destination2 = new Place(1, 1);
        Move move1 = new Move(source1, destination1);
        Move move2 = move1.clone();
        Move move3 = new Move(source2, destination1);
        Move move4 = new Move(source2, destination2);
        assertTrue(move1.equals(move1));
        assertTrue(move1.equals(move2));
        assertEquals(move1.hashCode(), move2.hashCode());
        assertEquals(move1.toString(), move2.toString());
        assertFalse(move2.equals(move3));
        assertNotEquals(move2.hashCode(), move3.hashCode());
        assertNotEquals(move2.toString(), move3.toString());
        assertFalse(move3.equals(move4));
        assertNotEquals(move3.hashCode(), move4.hashCode());
        assertNotEquals(move3.toString(), move4.toString());
        assertFalse(move4.equals(null));
        assertFalse(move4.equals(source2));
    }

    /**
     * Test Player functions
     */
    @Test
    public void testPlayer() throws CloneNotSupportedException {
        Player player1 = new ConsolePlayer("Name");
        Player player2 = player1.clone();
        Player player3 = new RandomPlayer("Name");
        Player player4 = new RandomPlayer("AnotherName");
        assertTrue(player1.equals(player1));
        assertTrue(player1.equals(player2));
        assertEquals(player1.hashCode(), player2.hashCode());
        assertEquals(player1.toString(), player2.toString());
        assertFalse(player2.equals(player3));
        assertEquals(player2.hashCode(), player3.hashCode());
        assertEquals(player2.toString(), player3.toString());
        assertFalse(player3.equals(player4));
        assertNotEquals(player3.hashCode(), player4.hashCode());
        assertNotEquals(player3.toString(), player4.toString());
        assertFalse(player4.equals(null));
    }

    /**
     * Test MoveRecord functions
     */
    @Test
    public void testMoveRecord() throws CloneNotSupportedException {
        MockPlayer mockPlayer = new MockPlayer();
        Move move = new Move(new Place(0, 0), new Place(0, 1));
        MoveRecord moveRecord1 = new MoveRecord(mockPlayer, move);
        MoveRecord moveRecord2 = moveRecord1.clone();
        MoveRecord moveRecord3 = new MoveRecord(mockPlayer, new Move(new Place(1, 0), new Place(0, 0)));
        MoveRecord moveRecord4 = new MoveRecord(new MockPlayer(), move);
        assertEquals(mockPlayer, moveRecord1.getPlayer());
        assertEquals(move, moveRecord1.getMove());
        assertTrue(moveRecord1.equals(moveRecord1));
        assertTrue(moveRecord1.equals(moveRecord2));
        assertFalse(moveRecord1.equals(moveRecord3));
        assertFalse(moveRecord1.equals(moveRecord4));
        assertFalse(moveRecord3.equals(moveRecord4));
        assertFalse(moveRecord1.equals(null));
        assertFalse(moveRecord1.equals(mockPlayer));
        assertEquals(moveRecord1.hashCode(), moveRecord2.hashCode());
        assertEquals(moveRecord1.toString(), moveRecord2.toString());
    }

    /**
     * Test private validateMove() functions
     */
    @Test
    public void testValidateMove1() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ConsolePlayer player1 = new ConsolePlayer("player1");
        RandomPlayer player2 = new RandomPlayer("player2");
        Knight p1Knight = new Knight(player1);
        Place p1KnightPlace = new Place(2, 2);
        Knight p2Knight = new Knight(player2);
        Place p2KnightPlace = new Place(0, 1);
        Archer p1Archer1 = new Archer(player1);
        Place p1Archer1Place = new Place(2, 0);
        Archer p1Archer2 = new Archer(player1);
        Place p1Archer2Place = new Place(3, 0);
        Archer p2Archer1 = new Archer(player2);
        Place p2Archer1Place = new Place(2, 3);
        Archer p2Archer2 = new Archer(player2);
        Place p2Archer2Place = new Place(2, 4);
        Configuration config = new Configuration(5, new Player[]{player1, player2}, 2);
        config.addInitialPiece(p1Knight, new Place(1, 0));
        config.addInitialPiece(p2Knight, p2KnightPlace);
        config.addInitialPiece(p1Archer1, p1Archer1Place);
        config.addInitialPiece(p1Archer2, p1Archer2Place);
        config.addInitialPiece(p2Archer1, p2Archer1Place);
        config.addInitialPiece(p2Archer2, p2Archer2Place);
        JesonMor game = new JesonMor(config);
        game.movePiece(new Move(new Place(1, 0), p1KnightPlace));
        Method jesonMor = JesonMor.class.getDeclaredMethod("validateMove", Player.class, Piece.class, Move.class);
        jesonMor.setAccessible(true);
        Method knight = Knight.class.getDeclaredMethod("validateMove", Game.class, Move.class);
        knight.setAccessible(true);
        Method archer = Archer.class.getDeclaredMethod("validateMove", Game.class, Move.class);
        archer.setAccessible(true);
        Method consolePlayer = ConsolePlayer.class.getDeclaredMethod("validateMove", Game.class, Move.class);
        consolePlayer.setAccessible(true);
        Method randomPlayer = RandomPlayer.class.getDeclaredMethod("validateMove", Game.class, Move.class);
        randomPlayer.setAccessible(true);

        assertFalse((boolean) jesonMor.invoke(game, player1, p1Knight, null));
        assertFalse((boolean) jesonMor.invoke(game, player2, p1Knight, new Move(p1KnightPlace, p2KnightPlace)));
        assertFalse((boolean) jesonMor.invoke(game, null, null, new Move(0, 0, 1, 1)));
        assertFalse((boolean) jesonMor.invoke(game, null, null, new Move(p1KnightPlace, p2KnightPlace)));
        assertFalse((boolean) knight.invoke(p1Knight, null, null));
        assertFalse((boolean) knight.invoke(p1Knight, game, null));
        assertFalse((boolean) archer.invoke(p1Archer1, null, null));
        assertFalse((boolean) archer.invoke(p1Archer1, game, null));
        assertFalse((boolean) consolePlayer.invoke(player1, null, null));
        assertFalse((boolean) consolePlayer.invoke(player1, game, null));
        assertFalse((boolean) randomPlayer.invoke(player2, null, null));
        assertFalse((boolean) randomPlayer.invoke(player2, game, null));

        assertFalse((boolean) jesonMor.invoke(game, player1, p1Knight, new Move(new Place(0, 4), new Place(4, 0))));
        assertFalse((boolean) knight.invoke(p1Knight, game, new Move(new Place(0, 4), new Place(4, 0))));
        assertFalse((boolean) archer.invoke(p1Archer1, game, new Move(new Place(0, 4), new Place(4, 0))));
        assertFalse((boolean) consolePlayer.invoke(player1, game, new Move(new Place(0, 4), new Place(4, 0))));
        assertFalse((boolean) randomPlayer.invoke(player2, game, new Move(new Place(0, 4), new Place(4, 0))));

        Place[] invalidXPlaces = new Place[]{new Place(99, 0), new Place(-1, 0)};
        Place[] invalidYPlaces = new Place[]{new Place(0, 99), new Place(0, -1)};

        for (int i = 0; i < invalidXPlaces.length && i < invalidYPlaces.length; i++) {
            assertFalse((boolean) jesonMor.invoke(game, player1, p1Knight, new Move(invalidXPlaces[i], p2KnightPlace)));
            assertFalse((boolean) jesonMor.invoke(game, player1, p1Knight, new Move(invalidYPlaces[i], p2KnightPlace)));
            assertFalse((boolean) jesonMor.invoke(game, player1, p1Knight, new Move(p1KnightPlace, invalidXPlaces[i])));
            assertFalse((boolean) jesonMor.invoke(game, player1, p1Knight, new Move(p1KnightPlace, invalidYPlaces[i])));
            assertFalse((boolean) knight.invoke(p1Knight, game, new Move(invalidXPlaces[i], p2KnightPlace)));
            assertFalse((boolean) knight.invoke(p1Knight, game, new Move(invalidYPlaces[i], p2KnightPlace)));
            assertFalse((boolean) knight.invoke(p1Knight, game, new Move(p1KnightPlace, invalidXPlaces[i])));
            assertFalse((boolean) knight.invoke(p1Knight, game, new Move(p1KnightPlace, invalidYPlaces[i])));
            assertFalse((boolean) archer.invoke(p1Archer1, game, new Move(invalidXPlaces[i], p2Archer1Place)));
            assertFalse((boolean) archer.invoke(p1Archer1, game, new Move(invalidYPlaces[i], p2Archer1Place)));
            assertFalse((boolean) archer.invoke(p1Archer1, game, new Move(p1Archer1Place, invalidXPlaces[i])));
            assertFalse((boolean) archer.invoke(p1Archer1, game, new Move(p1Archer1Place, invalidYPlaces[i])));
            assertFalse((boolean) consolePlayer.invoke(player1, game, new Move(invalidXPlaces[i], p2KnightPlace)));
            assertFalse((boolean) consolePlayer.invoke(player1, game, new Move(invalidYPlaces[i], p2KnightPlace)));
            assertFalse((boolean) consolePlayer.invoke(player1, game, new Move(p1KnightPlace, invalidXPlaces[i])));
            assertFalse((boolean) consolePlayer.invoke(player1, game, new Move(p1KnightPlace, invalidYPlaces[i])));
            assertFalse((boolean) randomPlayer.invoke(player2, game, new Move(invalidXPlaces[i], p1KnightPlace)));
            assertFalse((boolean) randomPlayer.invoke(player2, game, new Move(invalidYPlaces[i], p1KnightPlace)));
            assertFalse((boolean) randomPlayer.invoke(player2, game, new Move(p2KnightPlace, invalidXPlaces[i])));
            assertFalse((boolean) randomPlayer.invoke(player2, game, new Move(p2KnightPlace, invalidYPlaces[i])));
        }

        assertFalse((boolean) jesonMor.invoke(game, player1, p1Knight, new Move(p1KnightPlace, p1KnightPlace)));
        assertFalse((boolean) knight.invoke(p1Knight, game, new Move(p1KnightPlace, p1KnightPlace)));
        assertFalse((boolean) archer.invoke(p1Archer1, game, new Move(p1Archer1Place, p1Archer1Place)));
        assertFalse((boolean) consolePlayer.invoke(player1, game, new Move(p1KnightPlace, p1KnightPlace)));
        assertFalse((boolean) randomPlayer.invoke(player2, game, new Move(p2KnightPlace, p2KnightPlace)));
    }

    /**
     * Test private validateMove() functions
     */
    @Test
    public void testValidateMove2() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ConsolePlayer player1 = new ConsolePlayer("player1");
        RandomPlayer player2 = new RandomPlayer("player2");
        Knight p1Knight = new Knight(player1);
        Place p1KnightPlace = new Place(2, 2);
        Knight p2Knight = new Knight(player2);
        Place p2KnightPlace = new Place(0, 1);
        Archer p1Archer1 = new Archer(player1);
        Place p1Archer1Place = new Place(2, 0);
        Archer p1Archer2 = new Archer(player1);
        Place p1Archer2Place = new Place(3, 0);
        Archer p2Archer1 = new Archer(player2);
        Place p2Archer1Place = new Place(2, 3);
        Archer p2Archer2 = new Archer(player2);
        Place p2Archer2Place = new Place(2, 4);
        Configuration config = new Configuration(5, new Player[]{player1, player2}, 2);
        config.addInitialPiece(p1Knight, new Place(1, 0));
        config.addInitialPiece(p2Knight, p2KnightPlace);
        config.addInitialPiece(p1Archer1, p1Archer1Place);
        config.addInitialPiece(p1Archer2, p1Archer2Place);
        config.addInitialPiece(p2Archer1, p2Archer1Place);
        config.addInitialPiece(p2Archer2, p2Archer2Place);
        JesonMor game = new JesonMor(config);
        game.movePiece(new Move(new Place(1, 0), p1KnightPlace));
        Method jesonMor = JesonMor.class.getDeclaredMethod("validateMove", Player.class, Piece.class, Move.class);
        jesonMor.setAccessible(true);
        Method knight = Knight.class.getDeclaredMethod("validateMove", Game.class, Move.class);
        knight.setAccessible(true);
        Method archer = Archer.class.getDeclaredMethod("validateMove", Game.class, Move.class);
        archer.setAccessible(true);
        Method consolePlayer = ConsolePlayer.class.getDeclaredMethod("validateMove", Game.class, Move.class);
        consolePlayer.setAccessible(true);
        Method randomPlayer = RandomPlayer.class.getDeclaredMethod("validateMove", Game.class, Move.class);
        randomPlayer.setAccessible(true);

        assertFalse((boolean) jesonMor.invoke(game, player1, p1Knight, new Move(p1KnightPlace, 2, 1)));
        assertFalse((boolean) jesonMor.invoke(game, player1, p1Archer1, new Move(p1Archer1Place, 3, 1)));
        assertFalse((boolean) knight.invoke(p1Knight, game, new Move(p1KnightPlace, 2, 1)));
        assertFalse((boolean) archer.invoke(p1Archer1, game, new Move(p1Archer1Place, 3, 1)));
        assertFalse((boolean) consolePlayer.invoke(player1, game, new Move(p1KnightPlace, 2, 1)));
        assertFalse((boolean) consolePlayer.invoke(player1, game, new Move(p1Archer1Place, 3, 1)));
        assertFalse((boolean) randomPlayer.invoke(player2, game, new Move(p2KnightPlace, 0, 0)));
        assertFalse((boolean) randomPlayer.invoke(player2, game, new Move(p2Archer1Place, 1, 2)));

        assertTrue((boolean) jesonMor.invoke(game, player1, p1Knight, new Move(p1KnightPlace, 0, 3)));
        assertFalse((boolean) jesonMor.invoke(game, player1, p1Knight, new Move(p1KnightPlace, 1, 4)));
        assertTrue((boolean) jesonMor.invoke(game, player1, p1Archer2, new Move(p1Archer2Place, 3, 3)));
        assertFalse((boolean) jesonMor.invoke(game, player1, p1Archer2, new Move(p1Archer2Place, 0, 0)));
        assertTrue((boolean) knight.invoke(p1Knight, game, new Move(p1KnightPlace, 0, 3)));
        assertFalse((boolean) knight.invoke(p1Knight, game, new Move(p1KnightPlace, 1, 4)));
        assertTrue((boolean) archer.invoke(p1Archer2, game, new Move(p1Archer2Place, 3, 3)));
        assertFalse((boolean) archer.invoke(p1Archer2, game, new Move(p1Archer2Place, 0, 0)));
        assertTrue((boolean) consolePlayer.invoke(player1, game, new Move(p1KnightPlace, 0, 3)));
        assertFalse((boolean) consolePlayer.invoke(player1, game, new Move(p1KnightPlace, 1, 4)));
        assertTrue((boolean) consolePlayer.invoke(player1, game, new Move(p1Archer2Place, 3, 3)));
        assertFalse((boolean) consolePlayer.invoke(player1, game, new Move(p1Archer2Place, 0, 0)));
        assertTrue((boolean) randomPlayer.invoke(player2, game, new Move(p2KnightPlace, 1, 3)));
        assertTrue((boolean) randomPlayer.invoke(player2, game, new Move(p2Archer2Place, 3, 4)));
        assertFalse((boolean) randomPlayer.invoke(player2, game, new Move(p2Archer2Place, 2, 1)));

        game.movePiece(new Move(p1Archer2Place, 3, 1));
        game.movePiece(new Move(new Place(3, 1), p1Archer2Place));

        assertTrue((boolean) jesonMor.invoke(game, player1, p1Knight, new Move(p1KnightPlace, p2KnightPlace)));
        assertFalse((boolean) jesonMor.invoke(game, player1, p1Knight, new Move(p1KnightPlace, p1Archer2Place)));
        assertTrue((boolean) jesonMor.invoke(game, player1, p1Archer1, new Move(p1Archer1Place, p2Archer1Place)));
        assertFalse((boolean) jesonMor.invoke(game, player1, p1Archer1, new Move(p1Archer1Place, p2Archer2Place)));
        assertFalse((boolean) jesonMor.invoke(game, player1, p1Archer1, new Move(p1Archer1Place, p1KnightPlace)));
        assertTrue((boolean) knight.invoke(p1Knight, game, new Move(p1KnightPlace, p2KnightPlace)));
        assertFalse((boolean) knight.invoke(p1Knight, game, new Move(p1KnightPlace, p1Archer2Place)));
        assertTrue((boolean) archer.invoke(p1Archer1, game, new Move(p1Archer1Place, p2Archer1Place)));
        assertFalse((boolean) archer.invoke(p1Archer1, game, new Move(p1Archer1Place, p2Archer2Place)));
        assertFalse((boolean) archer.invoke(p1Archer1, game, new Move(p1Archer1Place, p1KnightPlace)));
        assertTrue((boolean) consolePlayer.invoke(player1, game, new Move(p1KnightPlace, p2KnightPlace)));
        assertFalse((boolean) consolePlayer.invoke(player1, game, new Move(p1KnightPlace, p1Archer2Place)));
        assertTrue((boolean) consolePlayer.invoke(player1, game, new Move(p1Archer1Place, p2Archer1Place)));
        assertFalse((boolean) consolePlayer.invoke(player1, game, new Move(p1Archer1Place, p2Archer2Place)));
        assertFalse((boolean) consolePlayer.invoke(player1, game, new Move(p1Archer1Place, p1KnightPlace)));
        assertTrue((boolean) randomPlayer.invoke(player2, game, new Move(p2KnightPlace, p1KnightPlace)));
        assertFalse((boolean) randomPlayer.invoke(player2, game, new Move(p2KnightPlace, p2Archer1Place)));
        assertTrue((boolean) randomPlayer.invoke(player2, game, new Move(p2Archer2Place, p1KnightPlace)));
        assertFalse((boolean) randomPlayer.invoke(player2, game, new Move(p2Archer2Place, p1Archer1Place)));
        assertFalse((boolean) randomPlayer.invoke(player2, game, new Move(p2Archer2Place, p2Archer1Place)));
    }

    /**
     * Test private validateMove() functions
     */
    @Test
    public void testValidateMove3() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ConsolePlayer player1 = new ConsolePlayer("player1");
        RandomPlayer player2 = new RandomPlayer("player2");
        Knight p1Knight = new Knight(player1);
        Place p1KnightPlace = new Place(2, 3);
        Knight p2Knight = new Knight(player2);
        Place p2KnightPlace = new Place(2, 2);
        Archer p1Archer1 = new Archer(player1);
        Place p1Archer1Place = new Place(0, 2);
        Archer p1Archer2 = new Archer(player1);
        Place p1Archer2Place = new Place(4, 2);
        Archer p2Archer1 = new Archer(player2);
        Place p2Archer1Place = new Place(0, 0);
        Archer p2Archer2 = new Archer(player2);
        Place p2Archer2Place = new Place(3, 2);
        Configuration config = new Configuration(5, new Player[]{player1, player2}, 1);
        config.addInitialPiece(p1Knight, p1KnightPlace);
        config.addInitialPiece(p2Knight, new Place(1, 0));
        config.addInitialPiece(p1Archer1, p1Archer1Place);
        config.addInitialPiece(p1Archer2, p1Archer2Place);
        config.addInitialPiece(p2Archer1, p2Archer1Place);
        config.addInitialPiece(p2Archer2, p2Archer2Place);
        JesonMor game = new JesonMor(config);
        game.movePiece(new Move(new Place(1, 0), p2KnightPlace));
        Method jesonMor = JesonMor.class.getDeclaredMethod("validateMove", Player.class, Piece.class, Move.class);
        jesonMor.setAccessible(true);
        Method knight = Knight.class.getDeclaredMethod("validateMove", Game.class, Move.class);
        knight.setAccessible(true);
        Method archer = Archer.class.getDeclaredMethod("validateMove", Game.class, Move.class);
        archer.setAccessible(true);
        Method consolePlayer = ConsolePlayer.class.getDeclaredMethod("validateMove", Game.class, Move.class);
        consolePlayer.setAccessible(true);
        Method randomPlayer = RandomPlayer.class.getDeclaredMethod("validateMove", Game.class, Move.class);
        randomPlayer.setAccessible(true);

        assertFalse((boolean) jesonMor.invoke(game, player2, p2Knight, new Move(p2KnightPlace, 3, 4)));
        assertFalse((boolean) jesonMor.invoke(game, player2, p2Knight, new Move(p2KnightPlace, 4, 3)));
        assertTrue((boolean) jesonMor.invoke(game, player2, p2Knight, new Move(p2KnightPlace, 1, 0)));
        assertTrue((boolean) jesonMor.invoke(game, player2, p2Knight, new Move(p2KnightPlace, 0, 1)));
        assertTrue((boolean) jesonMor.invoke(game, player2, p2Archer2, new Move(p2Archer2Place, p1Archer1Place)));
        assertFalse((boolean) jesonMor.invoke(game, player2, p2Archer2, new Move(p2Archer2Place, 1, 2)));
        assertTrue((boolean) jesonMor.invoke(game, player1, p1Archer2, new Move(p1Archer2Place, p2KnightPlace)));
        assertFalse((boolean) jesonMor.invoke(game, player1, p1Archer2, new Move(p1Archer2Place, 1, 2)));
        assertTrue((boolean) jesonMor.invoke(game, player2, p2Archer1, new Move(p2Archer1Place, 4, 0)));
        assertFalse((boolean) jesonMor.invoke(game, player2, p2Archer1, new Move(p2Archer1Place, 0, 4)));
        assertFalse((boolean) knight.invoke(p2Knight, game, new Move(p2KnightPlace, 3, 4)));
        assertFalse((boolean) knight.invoke(p2Knight, game, new Move(p2KnightPlace, 4, 3)));
        assertTrue((boolean) knight.invoke(p2Knight, game, new Move(p2KnightPlace, 1, 0)));
        assertTrue((boolean) knight.invoke(p2Knight, game, new Move(p2KnightPlace, 0, 1)));
        assertTrue((boolean) archer.invoke(p2Archer2, game, new Move(p2Archer2Place, p1Archer1Place)));
        assertFalse((boolean) archer.invoke(p2Archer2, game, new Move(p2Archer2Place, 1, 2)));
        assertTrue((boolean) archer.invoke(p1Archer2, game, new Move(p1Archer2Place, p2KnightPlace)));
        assertFalse((boolean) archer.invoke(p1Archer2, game, new Move(p1Archer2Place, 1, 2)));
        assertTrue((boolean) archer.invoke(p2Archer1, game, new Move(p2Archer1Place, 4, 0)));
        assertFalse((boolean) archer.invoke(p2Archer1, game, new Move(p2Archer1Place, 0, 4)));
        assertTrue((boolean) consolePlayer.invoke(player1, game, new Move(p1Archer2Place, p2KnightPlace)));
        assertFalse((boolean) consolePlayer.invoke(player1, game, new Move(p1Archer2Place, 1, 2)));
        assertFalse((boolean) randomPlayer.invoke(player2, game, new Move(p2KnightPlace, 3, 4)));
        assertFalse((boolean) randomPlayer.invoke(player2, game, new Move(p2KnightPlace, 4, 3)));
        assertTrue((boolean) randomPlayer.invoke(player2, game, new Move(p2KnightPlace, 1, 0)));
        assertTrue((boolean) randomPlayer.invoke(player2, game, new Move(p2KnightPlace, 0, 1)));
        assertTrue((boolean) randomPlayer.invoke(player2, game, new Move(p2Archer2Place, p1Archer1Place)));
        assertFalse((boolean) randomPlayer.invoke(player2, game, new Move(p2Archer2Place, 1, 2)));
        assertTrue((boolean) randomPlayer.invoke(player2, game, new Move(p2Archer1Place, 4, 0)));
        assertFalse((boolean) randomPlayer.invoke(player2, game, new Move(p2Archer1Place, 0, 4)));
    }
}