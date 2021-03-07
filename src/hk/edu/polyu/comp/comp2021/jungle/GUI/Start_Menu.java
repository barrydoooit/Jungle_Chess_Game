package hk.edu.polyu.comp.comp2021.jungle.GUI;
import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.GameBoard;
import hk.edu.polyu.comp.comp2021.jungle.ConnectionAndStorage.Storage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * Construct the start menu of the game
 */
public class Start_Menu {
    private static final Dimension FRAME_DIMENSION=new Dimension(1424,800);
    private static final Dimension BGR_DIMENSION=new Dimension(1424,565);
    private JFrame jf=new JFrame("Jungle Game");

    /**
     * origional Start Menu
     */
    public Start_Menu(){
        //set size and bounds
        jf.setSize(FRAME_DIMENSION);

        //set layout
        jf.setLayout(new BorderLayout());

        //set the background picture panel
        JPanel jp=new bgPanel("src/hk/edu/polyu/comp/comp2021/jungle/GUI/images/Start_Menu/background.png");
        jp.setSize(BGR_DIMENSION);
        jp.setBackground(Color.white);

        //set the button panel layout
        JPanel jpBtn=btnPanel();

        //layout the image and the button
        Container c=jf.getContentPane();
        c.add(jp,BorderLayout.CENTER);
        c.add(jpBtn,BorderLayout.SOUTH);

        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Construct the button panel to choose from
     * @return JPanel that contains all the buttons
     */
    public JPanel btnPanel(){
        //set the button panel layout
        JPanel jpBtn=new JPanel();
        jpBtn.setBackground(Color.white);
        jpBtn.setLayout(new BoxLayout(jpBtn,BoxLayout.PAGE_AXIS));
        jpBtn.add(Box.createHorizontalGlue());

        //create the button
        JButton ng=new ImgButton("src/hk/edu/polyu/comp/comp2021/jungle/GUI/images/Start_Menu/newGame.jpg",
                "src/hk/edu/polyu/comp/comp2021/jungle/GUI/images/Start_Menu/NewGameBG.jpg");
        JButton og=new ImgButton("src/hk/edu/polyu/comp/comp2021/jungle/GUI/images/Start_Menu/OldGame.jpg",
                "src/hk/edu/polyu/comp/comp2021/jungle/GUI/images/Start_Menu/OldGameBG.png");
        JButton quit=new ImgButton("src/hk/edu/polyu/comp/comp2021/jungle/GUI/images/Start_Menu/quit.jpg",
                "src/hk/edu/polyu/comp/comp2021/jungle/GUI/images/Start_Menu/QuitBG.png");

        jpBtn.add(ng);
        ng.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TypeInFrame(GameBoard.setOriginalGameBoard()).setVisible(true);
            }
        });

        jpBtn.add(og);
        og.addActionListener(evt-> {
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
            try {
                String selectedFile = JOptionPane.showInputDialog(null, "Please select a file", "Load Game",
                        JOptionPane.PLAIN_MESSAGE, null, comboItems, comboItems[0]).toString();
                try {
                    GameBoard gameBoard = Storage.load("gameProcess/" + selectedFile, null);
                    new TypeInFrame(gameBoard).removeJOGFromComboBox().setVisible(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Cannot load game from selected file!", "File load error", JOptionPane.ERROR_MESSAGE);
                }
            }catch(NullPointerException ignored){

            }
        });

        quit.addActionListener(evt-> {
            System.exit(1);
        });

        jpBtn.add(quit);
        return jpBtn;
    }

    /**
     * Initialize the image button
     */
    static class ImgButton extends JButton {

        /**
         * @param fileLocation String that contains the image file location
         * @param rollOver String that contains the image when the mouse is rolled over
         */
        public ImgButton(String fileLocation,String rollOver) {
            this.setBorderPainted(false);
            ImageIcon ngBtn = new ImageIcon(fileLocation);
            this.setIcon(ngBtn);
            this.setMargin(new Insets(0, 0, 0, 0));
            this.setContentAreaFilled(false);
            this.setBorder(BorderFactory.createRaisedBevelBorder());
            this.setAlignmentX(Component.CENTER_ALIGNMENT);
            this.setRolloverIcon(new ImageIcon(rollOver));
        }
    }

    /**
     * Construct an image background
     */
    static class bgPanel extends JPanel{
        private Image image;

        /**
         * @param fileLocation String that contains the image file location
         */
        public bgPanel(String fileLocation){
            Image img=new ImageIcon(fileLocation).getImage();
            this.image=img;
        }

        @Override
        protected void paintComponent(Graphics g){
            g.drawImage(image,0,0,this.getWidth(),this.getHeight(),this);
        }
    }

    /**
     * Construct the new frame to type in player name
     */
    class TypeInFrame extends JFrame {

        /**
         * the window for info input
         */
        private JComboBox jComboBoxGameMode;
        private GameBoard gameBoard;
        private final int[] FONT_SIZE = {14,18};
        private final int I240 =240,I130=130,I200=200,I20=20,I30=30,I67=67,I45=45,I100=100;
        /**
         * a frame for user input of game mode settings
         * @param gameBoard start gameBoard
         */
        TypeInFrame(GameBoard gameBoard) {
            this.gameBoard = gameBoard;
            JPanel inputPanel = new JPanel();
            jComboBoxGameMode = new JComboBox<>(new String[]{"Play Offline", "Create Online Game", "Join Online Game"});
            JLabel jLabelGameMode = new JLabel();
            JButton jButtonStartGame = new JButton();
            JLabel jLabelFirst = new JLabel();
            JLabel jLabelSecond = new JLabel();
            JLabel jLabelThird = new JLabel();
            JTextField jTextFieldFirst = new JTextField();
            JTextField jTextFieldSecond = new JTextField();
            JTextField jTextFieldThird = new JTextField();

            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);
            jComboBoxGameMode.addItemListener(evt -> {
                if (evt.getItem().toString().equals("Play Offline")) {
                    jLabelFirst.setText("Player X Name: ");
                    jLabelSecond.setText("Player Y Name: ");
                    jLabelThird.setText("......");
                    jTextFieldThird.setText("");
                    jTextFieldThird.setEditable(false);
                } else if (evt.getItem().toString().equals("Create Online Game")) {
                    jLabelFirst.setText("Player Name:");
                    jLabelSecond.setText("Port Number: ");
                    int freePortNum;
                    try {
                        ServerSocket testor = new ServerSocket(0);
                        freePortNum = testor.getLocalPort();
                        testor.close();
                    }catch(IOException ex){
                        final int PORT = 50000;
                        freePortNum = PORT;
                    }
                    jTextFieldSecond.setText(Integer.toString(freePortNum));
                    jLabelThird.setText("Your IP address: ");
                    try {
                        jTextFieldThird.setText(InetAddress.getLocalHost().getHostAddress().trim());
                    }catch(UnknownHostException ex){
                        jTextFieldThird.setText("Unknown IP address");
                    }
                    jTextFieldThird.setEditable(false);
                } else if (evt.getItem().toString().equals("Join Online Game")) {
                    jTextFieldThird.setEditable(true);
                    jLabelFirst.setText("Player Name:");
                    jLabelSecond.setText("IP address: ");
                    jLabelThird.setText("Port Number");
                    jTextFieldThird.setText("");
                }
            });
            jLabelGameMode.setFont(new java.awt.Font("Tahoma", Font.BOLD, FONT_SIZE[1]));
            jLabelGameMode.setText("Game Mode:");
            jButtonStartGame.setFont(new java.awt.Font("Tahoma", Font.BOLD, FONT_SIZE[1]));
            jButtonStartGame.setText("Start Game");
            jButtonStartGame.addActionListener(evt -> {
                try {
                    if (Objects.requireNonNull(jComboBoxGameMode.getSelectedItem()).toString().equals("Play Offline")) {
                        if (jTextFieldFirst.getText().equals("") || jTextFieldSecond.getText().equals(""))
                            return;
                        new Mainboard(jTextFieldFirst.getText(), jTextFieldSecond.getText(), gameBoard).setVis(true);
                    } else if (Objects.requireNonNull(jComboBoxGameMode.getSelectedItem()).toString().equals("Create Online Game")) {
                        if (jTextFieldFirst.getText().equals("") || jTextFieldSecond.getText().equals(""))
                            return;
                        new OnlineMainBoard(jTextFieldFirst.getText(), gameBoard, true,
                                "", Integer.parseInt(jTextFieldSecond.getText())).setVis(true);
                    } else if (Objects.requireNonNull(jComboBoxGameMode.getSelectedItem()).toString().equals("Join Online Game")) {
                        if (jTextFieldFirst.getText().equals("") || jTextFieldSecond.getText().equals("") || jTextFieldThird.getText().equals(""))
                            return;
                        new OnlineMainBoard(jTextFieldFirst.getText(), gameBoard, false,
                                jTextFieldSecond.getText(), Integer.parseInt(jTextFieldThird.getText())).setVis(true);
                    }
                    jf.dispose();
                    this.dispose();
                }catch(Exception e){
                    System.out.println(e);
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Connection Failed!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }


            });
            jLabelFirst.setFont(new java.awt.Font("Tahoma", Font.BOLD, FONT_SIZE[0]));
            jLabelFirst.setText("Player X Name:");
            jLabelSecond.setFont(new java.awt.Font("Tahoma", Font.BOLD, FONT_SIZE[0]));
            jLabelSecond.setText("Player Y Name:");
            jLabelThird.setFont(new java.awt.Font("Tahoma", Font.BOLD, FONT_SIZE[0]));
            jLabelThird.setText("....");
            jTextFieldFirst.setFont(new java.awt.Font("Tahoma", Font.BOLD, FONT_SIZE[0]));
            jTextFieldSecond.setFont(new java.awt.Font("Tahoma", Font.BOLD, FONT_SIZE[0]));
            jTextFieldThird.setFont(new java.awt.Font("Tahoma", Font.BOLD, FONT_SIZE[0]));
            // <editor-fold defaultstate="collapsed" desc="Generated Code">
            GroupLayout inputPanelLayout = new GroupLayout(inputPanel);
            inputPanel.setLayout(inputPanelLayout);
            inputPanelLayout.setHorizontalGroup(
                    inputPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(inputPanelLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(inputPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jButtonStartGame, GroupLayout.DEFAULT_SIZE, I240, Short.MAX_VALUE)
                                            .addComponent(jLabelGameMode, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jComboBoxGameMode, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGap(I100, I100, I100)
                                    .addGroup(inputPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                            .addGroup(inputPanelLayout.createSequentialGroup()
                                                    .addComponent(jLabelThird, GroupLayout.PREFERRED_SIZE, I130, GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jTextFieldThird, GroupLayout.PREFERRED_SIZE, I200, GroupLayout.PREFERRED_SIZE))
                                            .addGroup(inputPanelLayout.createSequentialGroup()
                                                    .addComponent(jLabelFirst, GroupLayout.PREFERRED_SIZE, I130, GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jTextFieldFirst, GroupLayout.PREFERRED_SIZE, I200, GroupLayout.PREFERRED_SIZE))
                                            .addGroup(inputPanelLayout.createSequentialGroup()
                                                    .addComponent(jLabelSecond, GroupLayout.PREFERRED_SIZE, I130, GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jTextFieldSecond, GroupLayout.PREFERRED_SIZE, I200, GroupLayout.PREFERRED_SIZE)))
                                    .addContainerGap(I20, Short.MAX_VALUE))
            );
            inputPanelLayout.setVerticalGroup(
                    inputPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(inputPanelLayout.createSequentialGroup()
                                    .addGap(I67, I67, I67)
                                    .addGroup(inputPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabelFirst, GroupLayout.PREFERRED_SIZE, I30, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextFieldFirst, GroupLayout.PREFERRED_SIZE, I30, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabelGameMode, GroupLayout.PREFERRED_SIZE, I30, GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, I30, Short.MAX_VALUE)
                                    .addGroup(inputPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(jComboBoxGameMode, GroupLayout.PREFERRED_SIZE, I30, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabelSecond, GroupLayout.PREFERRED_SIZE, I30, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextFieldSecond, GroupLayout.PREFERRED_SIZE, I30, GroupLayout.PREFERRED_SIZE))
                                    .addGroup(inputPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                            .addGroup(inputPanelLayout.createSequentialGroup()
                                                    .addGap(I30, I30, I30)
                                                    .addGroup(inputPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                            .addComponent(jLabelThird, GroupLayout.PREFERRED_SIZE, I30, GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(jTextFieldThird, GroupLayout.PREFERRED_SIZE, I30, GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(inputPanelLayout.createSequentialGroup()
                                                    .addGap(I45, I45, I45)
                                                    .addComponent(jButtonStartGame, GroupLayout.PREFERRED_SIZE, I45, GroupLayout.PREFERRED_SIZE)))
                                    .addGap(I45, I45, I45))
            );
            // </editor-fold>
            this.add(inputPanel);
            pack();
        }

        /**
         * remove the join online game option from th JComboBox
         * @return the typeInFrame itself
         */
        public TypeInFrame removeJOGFromComboBox() {
            jComboBoxGameMode.removeItem("Join Online Game");
            return this;
        }
    }

}
