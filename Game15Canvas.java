import java.util.Random;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class Game15Canvas {
    private static final byte UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
    private static final boolean GAME_ACTIVE = false, GAME_WIN = true;
    private boolean gameState;
    private final byte size;
    private final byte[][] field;
    private byte emptyRow, emptyCol;
    private static Random random;

    private int cellSize;
    private int offsetX;
    private int offsetY;

    Game15Canvas(byte size) {
        this.size = size;
        field = new byte[size][size];
        random = new Random();
        newGame();
    }

    void newGame() {
        for (byte row = 0; row < size; row++) {
            for (byte col = 0; col < size; col++) {
                field[row][col] = (byte) (row + col * size + 1);
            }
        }
        field[size - 1][size - 1] = 0;
        emptyCol = (byte) (size - 1);
        emptyRow = (byte) (size - 1);
        gameState = GAME_ACTIVE;
        shuffle();
    }

    void move(byte direction) {
        // TODO: исправить
        switch (direction) {
            case UP: {
                if (emptyRow >= size - 1) return;
                field[emptyRow][emptyCol] = field[emptyCol][emptyRow - 1];
                return;
            }
            case RIGHT: {
                if (emptyCol <= 0) return;
                field[emptyRow][emptyCol] = field[emptyCol + 1][emptyRow];
                return;
            }
            case DOWN: {
                if (emptyRow <= 0) return;
                field[emptyRow][emptyCol] = field[emptyCol][emptyRow + 1];
                return;
            }
            case LEFT: {
                if (emptyCol >= size - 1) return;
                field[emptyRow][emptyCol] = field[emptyCol - 1][emptyRow];
                return;
            }
        }
        field[emptyCol][emptyRow] = 0;
    }

    void shuffle() {
        for (int i = 0; i < size * size; i++) {
            move((byte) random.nextInt(4));
        }
    }

    void checkWin() {
        for (byte x = 0; x < size; x++) {
            for (byte y = 0; y < size; y++) {
                if (field[x][y] != x + y + 1 || field[x][y] == 0) return;
            }
        }
        gameState = GAME_WIN;
    }

    protected void sizeChanged(int width, int height) {
        cellSize = Math.min(width, height) / size;
        offsetX = (width - cellSize * size) / size;
        offsetY = (height - cellSize * size) / size;
    }

    protected void paint(Graphics g) {
        g.setColor(0x000000);
        g.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE));

        Font font = g.getFont();

    }

    protected void keyPressed(int keyCode) {
        if (gameState != GAME_ACTIVE) return;

        switch (keyCode) {
            case KEY_UP, KEY_NUM2: {
                move(UP);
            }
            case KEY_RIGHT, KEY_NUM6: {
                move(RIGHT);
            }
            case KEY_DOWN, KEY_NUM8: {
                move(DOWN);
            }
            case KEY_LEFT, KEY_NUM4: {
                move(LEFT);
            }
        }
        repaint();
    }
}

public class Game15MIDlet extends MIDlet implements CommandListener {
    private Display display;
    private Game15Canvas gameCanvas;
    private Command exitCommand;
    private Command newGameCommand;

    public void startApp() {
        if (gameCanvas == null) {
            gameCanvas = new Game15Canvas((byte) 4);
            exitCommand = new Command("Выход", Command.EXIT, 1);
            newGameCommand = new Command("Новая игра", Command.SCREEN, 2);

            gameCanvas.addCommand(exitCommand);
            gameCanvas.addCommand(newGameCommand);
            gameCanvas.setCommand(this);
        }

        display = Display.getDisplay(this);
        display.setCurrent(gameCanvas);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command command, Displayable d) {
        if (command == exitCommand) {
            destroyApp(true);
            notifyDestroyed();
        } else if (command == newGameCommand) {
            gameCanvas.newGame();
        }
    }
}