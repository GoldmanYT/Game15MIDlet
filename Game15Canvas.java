import java.util.Random;

import javax.microedition.lcdui.*;

public class Game15Canvas extends Canvas {
    private static final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
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
        for (byte col = 0; col < size; col++) {
            for (byte row = 0; row < size; row++) {
                field[row][col] = (byte) (col + row * size + 1);
            }
        }
        field[size - 1][size - 1] = 0;
        emptyCol = (byte) (size - 1);
        emptyRow = (byte) (size - 1);
        gameState = GAME_ACTIVE;
        shuffle();
        repaint();
    }

    void move(int direction) {
        switch (direction) {
            case UP: {
                if (emptyRow < size - 1) {
                	field[emptyRow][emptyCol] = field[emptyRow + 1][emptyCol];
                	field[emptyRow + 1][emptyCol] = 0;
                	emptyRow++;
                }
            	break;
            }
            case RIGHT: {
                if (emptyCol > 0) {
                	field[emptyRow][emptyCol] = field[emptyRow][emptyCol - 1];
                	field[emptyRow][emptyCol - 1] = 0;
                	emptyCol--;
                }
            	break;
            }
            case DOWN: {
                if (emptyRow > 0) {
                	field[emptyRow][emptyCol] = field[emptyRow - 1][emptyCol];
                	field[emptyRow - 1][emptyCol] = 0;
                	emptyRow--;
                }
            	break;
            }
            case LEFT: {
                if (emptyCol < size - 1) {
                	field[emptyRow][emptyCol] = field[emptyRow][emptyCol + 1];
                	field[emptyRow][emptyCol + 1] = 0;
                	emptyCol++;
                }
            	break;
            }
        }
    }

    void shuffle() {
        for (int i = 0; i < 2 * size; i++) {
        	int r = random.nextInt();
        	for (int j = 0; j < 16; j++) {
        		move(r & 0x3);
        		r >>>= 2;
        	}
        }
    }

    void checkWin() {
        for (byte row = 0; row < size; row++) {
            for (byte col = 0; col < size; col++) {
                if (!(field[row][col] == col + row * size + 1 || field[row][col] == 0)) {
                	return; 
                }
            }
        }
        gameState = GAME_WIN;
    }

    protected void sizeChanged(int width, int height) {
        cellSize = Math.min(width, height) / size;
        offsetX = (width - cellSize * size) / 2;
        offsetY = (height - cellSize * size) / 2;
    }

    protected void paint(Graphics g) {
        g.setColor(0xFFFFFF);
        g.fillRect(0, 0, getWidth(), getHeight());
    	
        g.setColor(0x000000);
        for (byte row = 0; row <= size; row++) {
            g.drawLine(offsetX, offsetY + row * cellSize,
            		offsetX + size * cellSize, offsetY + row * cellSize);
            g.drawLine(offsetX + row * cellSize, offsetY,
         		   offsetX + row * cellSize, offsetY + size * cellSize);
        }
        
        Font font = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
        g.setFont(font);
        int height = font.getHeight();
        
        String s;
        for (byte row = 0; row < size; row++) {
            for (byte col = 0; col < size; col++) {
            	byte val = field[row][col];
            	if (val != 0) {
            		s = String.valueOf(val);
            		g.drawString(s,
            				offsetX + col * cellSize + cellSize / 2,
            				offsetY + row * cellSize + cellSize / 2 - height / 2,
            				Graphics.TOP | Graphics.HCENTER);
            	}
            }
        }
        checkWin();
        if (gameState == GAME_WIN) {
        	g.drawString("Победа!", getWidth() / 2, 0, Graphics.TOP | Graphics.HCENTER);
        }
    }

    protected void keyPressed(int keyCode) {
        if (gameState != GAME_ACTIVE) return;
        
        int moveDirection = -1;

        switch (keyCode) {
            case KEY_NUM2: {
            	moveDirection = UP;
                break;
            }
            case KEY_NUM6: {
            	moveDirection = RIGHT;
                break;
            }
            case KEY_NUM8: {
            	moveDirection = DOWN;
                break;
            }
            case KEY_NUM4: {
            	moveDirection = LEFT;
                break;
            }
        }
        
        if (moveDirection == -1) {
            int gameAction = getGameAction(keyCode);
            switch (gameAction) {
                case Canvas.UP: {
                	moveDirection = UP;
                    break;
                }
                case Canvas.RIGHT: {
                	moveDirection = RIGHT;
                    break;
                }
                case Canvas.DOWN: {
                	moveDirection = DOWN;
                    break;
                }
                case Canvas.LEFT: {
                	moveDirection = LEFT;
                    break;
                }
            }        	
        }

        move(moveDirection);
        
        repaint();
    }
}