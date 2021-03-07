package hk.edu.polyu.comp.comp2021.jungle.ConnectionAndStorage;

import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.Faction;
import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.GameBoard;
import hk.edu.polyu.comp.comp2021.jungle.model.Pieces.*;

import java.io.*;
import java.util.Properties;

/**
 * Used for save and load of the game
 */
public class Storage {
    /**
     * default storage file
     */
    public static final String DEFAULT_STORAGE_FILE = "gameProcess/save.properties";

    /**
     *
     * @param gameBoard the current gameBoard
     * @param filePath file path to save the game in
     * @throws IOException throw IO exception
     */
    public static void store(final GameBoard gameBoard, String filePath) throws IOException{
        try (OutputStream output = new FileOutputStream(filePath)) {
            Properties prop = new Properties();
            for (Piece piece : gameBoard.getPiecesDistribution()) {
                prop.put(piece.toString(), piece.getPieceFaction().toString() + piece.getPiecePosition());
            }
            prop.put("Faction", gameBoard.getCurrentPlayer().toString());
            prop.store(output, null);
        }
    }
    /**
     *
     * @param filePath file path to load the game from
     * @param gameBoard the current gameBoard
     * @return a gameBoard referring to the loaded file
     * @throws IOException if file is not able to be loaded
     * @throws RuntimeException if file format is illegal
     */
    public static GameBoard load(String filePath, GameBoard gameBoard) throws IOException, RuntimeException{
        try (InputStream input = new FileInputStream(filePath)) {
            Properties prop = new Properties();
            prop.load(input);
            if(gameBoard!=null) {
                store(gameBoard, DEFAULT_STORAGE_FILE);
                System.out.println("Former game saved in "+DEFAULT_STORAGE_FILE);
            }
            final GameBoard.GameBoardBuilder builder = new GameBoard.GameBoardBuilder();
            prop.forEach((k, v) -> {
                if (k.toString().equals("Faction")) {
                    builder.setMoveMaker(v.toString().equals("X") ? Faction.X : Faction.Y);
                } else {
                    int piecePosition = Integer.parseInt(v.toString().substring(1));
                    Faction pieceFaction = v.toString().substring(0, 1).equals("X") ? Faction.X : Faction.Y;
                    switch (k.toString().toUpperCase().charAt(0)) {
                        case 'E':
                            builder.setPiece(new Elephant(piecePosition, pieceFaction));
                            break;
                        case 'W':
                            builder.setPiece(new Wolf(piecePosition, pieceFaction));
                            break;
                        case 'L':
                            builder.setPiece(new Leopard(piecePosition, pieceFaction));
                            break;
                        case 'R':
                            builder.setPiece(new Rat(piecePosition, pieceFaction));
                            break;
                        case 'C':
                            builder.setPiece(new Cat(piecePosition, pieceFaction));
                            break;
                        case 'D':
                            builder.setPiece(new Dog(piecePosition, pieceFaction));
                            break;
                        case 'T':
                            builder.setPiece(new Tiger(piecePosition, pieceFaction));
                            break;
                        case 'I':
                            builder.setPiece(new Lion(piecePosition, pieceFaction));
                    }
                }

            });
            return builder.build();
        }
    }
}
