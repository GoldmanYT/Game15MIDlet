import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;


public class Game15MIDlet extends MIDlet implements CommandListener {
    private Display display;
    private Game15Canvas gameCanvas;
    private Command newGameCommand;

    public void startApp() {
        if (gameCanvas == null) {
            gameCanvas = new Game15Canvas((byte) 4);
            newGameCommand = new Command("Новая игра", Command.SCREEN, 1);

            gameCanvas.addCommand(newGameCommand);
            gameCanvas.setCommandListener(this);
        }

        display = Display.getDisplay(this);
        display.setCurrent(gameCanvas);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command command, Displayable d) {
        if (command == newGameCommand) {
            gameCanvas.newGame();
        }
    }
}