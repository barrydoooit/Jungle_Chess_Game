package hk.edu.polyu.comp.comp2021.jungle.GUI;

import hk.edu.polyu.comp.comp2021.jungle.ConnectionAndStorage.Storage;
import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.*;
import hk.edu.polyu.comp.comp2021.jungle.model.Pieces.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

/**
 * Construct a gameboard interface
 */
public class Mainboard {
    private static final Dimension BROAD_PANEL_DIMENSION = new Dimension(622, 800);
    private static final Dimension BGR_DIMENSION=new Dimension(1424,565);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(89, 89);
    private static final Dimension FRAME_DIMENSION=new Dimension(1424,800);
    private static final Dimension HINT_PANEL_DIMENSION=new Dimension(401,800);
    private static final Dimension WARNING_DIMENSION=new Dimension(300,200);
    private static final int[] SPECIAL_TILE_ID={2,4,10,3,60,58,52,59};
    private static final int[] RIVER_TILES ={22,23,25,26,29,30,32,33,36,37,39,40};
    private static final String DEFAULT_PIECE_IMAGES_PATH = "src/hk/edu/polyu/comp/comp2021/jungle/GUI/images/pieces/";

    private GameBoard chessBoard;
    private final JFrame jf;
    private final BoardPanel boardPanel;
    private Piece movedPiece=Piece.RAW_PIECE;
    private int startTile=-1;
    private int endTile=-1;
    private JLabel factionNow=new JLabel("");
    private JLabel selectedPiece=new JLabel("");
    private boolean gameSaved = false;

    /**
     *
     * @param Xname String name of Player X
     * @param Yname String name of Player Y
     * @param gameBoard gameBoard for starting status
     */
    public Mainboard(String Xname,String Yname, GameBoard gameBoard) {
        this.jf = new JFrame("Jungle Game");
        jf.setSize(FRAME_DIMENSION);
        this.chessBoard=gameBoard;

        JPanel hint1 = new JPanel();
        Box b=Box.createVerticalBox();
        hint1.setPreferredSize(HINT_PANEL_DIMENSION);
        JLabel jlX=new JLabel("Faction X : "+Xname);
        JLabel jlY=new JLabel("Faction Y: "+Yname);
        JPanel x=new JPanel();
        x.setBackground(Color.white);
        x.add(jlX);
        JPanel y=new JPanel();
        y.setBackground(Color.white);
        y.add(jlY);
        factionNow.setText("Now is the turn for : "+chessBoard.getCurrentPlayer().toString());
        selectedPiece.setText("Please make a move!");
        b.add(x);
        b.add(y);
        Box.createHorizontalGlue();
        b.add(Box.createRigidArea(new Dimension(hint1.getWidth(),hint1.getHeight()/3)));
        Box.createHorizontalGlue();
        b.add(factionNow);
        b.add(selectedPiece);
        hint1.add(b);
        hint1.setBackground(Color.white);

        this.boardPanel=new BoardPanel();

        JPanel hint2 = new JPanel();
        hint2.setPreferredSize(HINT_PANEL_DIMENSION);
        hint2.setBackground(Color.white);

        Box b1=Box.createVerticalBox();
        b1.add(new JLabel("X :"));
        b1.add(new JLabel(new ImageIcon(DEFAULT_PIECE_IMAGES_PATH +"X.jpg")));
        b1.add(new JLabel("Y :"));
        b1.add(new JLabel(new ImageIcon(DEFAULT_PIECE_IMAGES_PATH +"Y.jpg")));
        hint2.add(b1);

        final JMenuBar tableMenuBar = createTableMenuBar();
        this.jf.setJMenuBar(tableMenuBar);
        jf.add(hint1,BorderLayout.WEST);
        jf.add(boardPanel,BorderLayout.CENTER);
        jf.add(hint2,BorderLayout.EAST);

        jf.dispose();
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     *
     * @param flag boolean flag of whether set jf visible
     */
    public void setVis(boolean flag){
        if(flag)
            jf.setVisible(true);
        else
            jf.dispose();

    }

    /**
     * @return the menu bar
     */
    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createStartMenu());
        tableMenuBar.add(createFileMenu());
        return tableMenuBar;
    }

    /**
     * @return the start menu
     */
    private JMenu createStartMenu(){
        final JMenu startMenu = new JMenu("Start");
        final JMenuItem newGame=new JMenuItem("New Game");
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Storage.store(chessBoard, "gameProcess/" + new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()) + ".properties");
                }catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex + "Save Failed!", "Save Failed", JOptionPane.ERROR_MESSAGE);
                }
                setVis(false);
                new Start_Menu();

            }
        });
        startMenu.add(newGame);
        final JMenuItem exitMenuItem = new JMenuItem(("Exit"));
        exitMenuItem.addActionListener(evt-> {
            try {
                Storage.store(chessBoard, "gameProcess/"+new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date())+".properties");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex + "Save Failed!", "Save Failed", JOptionPane.ERROR_MESSAGE);
            }
            System.exit(0);
        });
        startMenu.add(exitMenuItem);

        return startMenu;
    }
    /**
     * @return the file menu
     */
    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");

        final JMenuItem saveGame = new JMenuItem("Save");

        saveGame.addActionListener(evt->{
            if(!gameSaved) {
                String fileName = JOptionPane.showInputDialog(null, "Enter file name: ",
                        "Save Game", JOptionPane.INFORMATION_MESSAGE);
                if(fileName.equals(""))
                    fileName = new Date().toString();
                try {
                    Storage.store(chessBoard, "gameProcess/" + fileName + ".properties");
                    JOptionPane.showMessageDialog(null, "Game Saved!", "Game Saved", JOptionPane.PLAIN_MESSAGE);
                    gameSaved = true;
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex + "Save Failed!", "Save Failed", JOptionPane.ERROR_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(null, "Game Saved!", "Game Saved", JOptionPane.PLAIN_MESSAGE);
            }
        });
        fileMenu.add(saveGame);

        final JMenuItem loadGame = new JMenuItem("Load");
        loadGame.addActionListener(evt-> {

            File folder = new File("gameProcess");
            File[] listOfFiles = folder.listFiles();
            String[] comboItems;
            try {
                comboItems = new String[listOfFiles.length + 1];
                comboItems[0] = "--Please select a file--";
                for (int i = 1; i < listOfFiles.length + 1; ++i) {
                    comboItems[i] = listOfFiles[i - 1].getName();
                }
            } catch (NullPointerException ex) {
                comboItems = new String[]{"--Please select a file--"};
            }
            String selectedFile = JOptionPane.showInputDialog(null, "Please select a file", "Load Game",
                    JOptionPane.PLAIN_MESSAGE, null, comboItems, comboItems[0]).toString();
            try {
               chessBoard = Storage.load("gameProcess/" + selectedFile, chessBoard);
               boardPanel.drawBoard(chessBoard);
                JOptionPane.showMessageDialog(null, "Save loaded!\nCurrent game saved in save.properties", "Load game", JOptionPane.PLAIN_MESSAGE);
               factionNow.setText("Now is the turn for : "+chessBoard.getCurrentPlayer().toString());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Cannot load game from selected file!", "File load error", JOptionPane.ERROR_MESSAGE);
            }
        });
        fileMenu.add(loadGame);
        return fileMenu;
    }

    /**
     * Construct the game board panel
     */
    private class BoardPanel extends JPanel {

        private final List<TilePanel> boardTiles;

        BoardPanel() {
            super(new GridLayout(9, 7));
            this.boardTiles = new ArrayList<>();
            for (int i = 0; i < GameUtils.GAMEBOARD_TILE_NUMBER; i++) {
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BROAD_PANEL_DIMENSION);
            validate();
        }

        public void drawBoard(final GameBoard board) {
            removeAll();
            for (final TilePanel tilePanel : boardTiles) {
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }


    /**
     * Make move and renew the chessboard
     */
    private class MakeMove extends MouseAdapter {

        private int tileID;

        /**
         * @param tileID int the tileID of selected tile
         */
        MakeMove(int tileID){
            this.tileID=tileID;
        }

        @Override
        public void mouseClicked(final MouseEvent e) {
            if (startTile==-1) selectedPiece.setText("Please select a piece to make move!");
            if (isRightMouseButton(e)) {
                startTile = -1;
                endTile = -1;
                movedPiece = Piece.RAW_PIECE;
            } else if (isLeftMouseButton(e)) {
                System.out.println("this tile:"+tileID+" startTile="+startTile+" endTile= "+endTile);
                if (startTile == -1) {
                    startTile = tileID;
                    movedPiece = chessBoard.getPieceOnTile(tileID);
                    if (movedPiece == Piece.RAW_PIECE) {
                        startTile = -1;
                        selectedPiece.setText("Please select a piece to make move!");
                    }else {
                        String piecetype = "";
                        switch (movedPiece.getPieceType().toString()) {
                            case "C": {
                                piecetype = "Cat";
                                break;
                            }
                            case "D": {
                                piecetype = "Dog";
                                break;
                            }
                            case "E": {
                                piecetype = "Elephant";
                                break;
                            }
                            case "L": {
                                piecetype = "Leopard";
                                break;
                            }
                            case "I": {
                                piecetype = "Lion";
                                break;
                            }
                            case "W": {
                                piecetype = "Wolf";
                                break;
                            }
                            case "R": {
                                piecetype = "Rat";
                                break;
                            }
                            case "T": {
                                piecetype = "Tiger";
                                break;
                            }
                        }
                        selectedPiece.setText("You have selected " + piecetype);
                    }
                } else {
                    endTile = tileID;
                    try {
                        chessBoard= Step.createStep(chessBoard,movedPiece,endTile).operateStep();
                        gameSaved=false;
                        startTile = -1;
                        endTile = -1;
                        movedPiece = Piece.RAW_PIECE;
                        factionNow.setText("Now is the turn for : "+chessBoard.getCurrentPlayer().toString());
                        selectedPiece.setText("Please make your move!");
                    } catch (RuntimeException r) {
                        System.out.println("this tile:"+tileID+" startTile="+startTile+" endTile= "+endTile);
                        System.out.println(r);
                        startTile = -1;
                        endTile = -1;
                        movedPiece = Piece.RAW_PIECE;
                        selectedPiece.setText("Illegal move!");
                        return;
                    }
                }

                SwingUtilities.invokeLater(() -> {
                    if (chessBoard.evaluateWinner()==null) boardPanel.drawBoard(chessBoard);
                    else if (chessBoard.evaluateWinner()== Faction.X) {
                        System.out.println("X wins");
                        new Win(Faction.X);
                    }
                    else  {
                        System.out.println("Y wins");
                        new Win(Faction.Y);
                    }
                });
            }
        }
    }

    /**
     * Renew the frame and print out the winning message
     */
    private class Win{
        Win(Faction winner){
            jf.getContentPane().removeAll();
            jf.setBackground(Color.white);

            //if the current player wins(for online mode)
            //JLabel ic=new JLabel(new ImageIcon(defaultPieceImagesPath+"win.png"));

            //if the current player loses(for online mode)
            //JLabel ic=new JLabel(new ImageIcon(defaultPieceImagesPath+"lose.png"));

            JLabel ic=new JLabel(new ImageIcon(DEFAULT_PIECE_IMAGES_PATH +"win"+winner.toString()+".png"));

            JPanel jp=new JPanel();
            ic.setBackground(Color.white);
            jp.add(ic);
            jp.setSize(BGR_DIMENSION);
            jp.setBackground(Color.white);

            JButton ng=new Start_Menu.ImgButton("src/hk/edu/polyu/comp/comp2021/jungle/GUI/images/Start_Menu/newGame.jpg",
                    "src/hk/edu/polyu/comp/comp2021/jungle/GUI/images/Start_Menu/NewGameBG.jpg");
            JButton quit=new Start_Menu.ImgButton("src/hk/edu/polyu/comp/comp2021/jungle/GUI/images/Start_Menu/quit.jpg",
                    "src/hk/edu/polyu/comp/comp2021/jungle/GUI/images/Start_Menu/QuitBG.png");
            ng.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    jf.dispose();
                    new Start_Menu();
                }
            });
            quit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(1);
                }
            });
            JPanel btnPanel=new JPanel();
            btnPanel.setBackground(Color.white);
            btnPanel.add(ng);
            btnPanel.add(quit);

            jf.add(jp,BorderLayout.CENTER);
            jf.add(btnPanel,BorderLayout.SOUTH);

            jf.revalidate();
            jf.repaint();
        }
    }

    /**
     * Construct each tile on the game board
     */
    protected class TilePanel extends JPanel {

        private final int tileID;

        /**
         *
         * @param boardPanel the JPanel contains all tiles
         * @param tileID the ID of the tile represented by THIS panel
         */
        TilePanel(final BoardPanel boardPanel,
                  final int tileID) {
            super(new GridBagLayout());
            this.tileID = tileID;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            assignTilePieceIcon(chessBoard);
            addMouseListener(new MakeMove(this.tileID));
        }

        /**
         * @param board Gameboard that contains current information of each pieces
         */
        private void assignTilePieceIcon(final GameBoard board) {
            this.removeAll();
            ArrayList<Integer> a=new ArrayList<>();
            for (int i:SPECIAL_TILE_ID) a.add(i);
            ArrayList<Integer> b=new ArrayList<>();
            for (int i: RIVER_TILES) b.add(i);
            String path="";
            if (board.getPieceOnTile(this.tileID)==Piece.RAW_PIECE || !a.contains(this.tileID) || !b.contains(this.tileID))
                path= DEFAULT_PIECE_IMAGES_PATH +"blank.png";
            if (board.getPieceOnTile(this.tileID)==Piece.RAW_PIECE || a.contains(this.tileID) || b.contains(this.tileID)) {
                if (this.tileID==SPECIAL_TILE_ID[0] ||this.tileID==SPECIAL_TILE_ID[1]||this.tileID==SPECIAL_TILE_ID[2] ){path= DEFAULT_PIECE_IMAGES_PATH +"trap.jpg";}
                if (this.tileID==SPECIAL_TILE_ID[3] || this.tileID==SPECIAL_TILE_ID[7]){path= DEFAULT_PIECE_IMAGES_PATH +"den.png";}
                if (this.tileID==SPECIAL_TILE_ID[4] ||this.tileID==SPECIAL_TILE_ID[5]||this.tileID==SPECIAL_TILE_ID[6]){path= DEFAULT_PIECE_IMAGES_PATH +"trap.jpg";}
                if (b.contains(this.tileID)) {path= DEFAULT_PIECE_IMAGES_PATH +"river.png";}
            }
            if (board.getPieceOnTile(this.tileID)!=Piece.RAW_PIECE){
                path = DEFAULT_PIECE_IMAGES_PATH + board.getPieceOnTile(this.tileID).getPieceType().toString() +
                        board.getPieceOnTile(this.tileID).getPieceFaction().toString().substring(0, 1) + ".jpg";
            }
            try {
                final BufferedImage image =
                        ImageIO.read(new File(path));
                add(new JLabel(new ImageIcon(image)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * create a border for each tile
         */
        private void assignTileColor() {
            this.setBackground(Color.white);
        }

        /**
         * @param board the current chess board that contains information of all pieces
         */
        public void drawTile(final GameBoard board){
            assignTileColor();
            assignTilePieceIcon(board);
            validate();
            repaint();
        }
    }
}
