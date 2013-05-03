package nl.mprog.evilhangman.controllers;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
/**
 * GameKeyboardListener takes care of the custom software keyboard events.
 * @author Jeroen Grootendorst
 *
 */
public class GameKeyboardListener implements OnKeyboardActionListener {

	private final Game game;

	/**
	 * Class constructor specifying a context and a game
	 * @param ctx
	 * @param game
	 */
	public GameKeyboardListener(Game game) {
		this.game = game;
	}

	public void onKey(int primaryCode, int[] keyCodes) {
		this.game.processKey(primaryCode);
	}

	public void onPress(int arg0) {
	}
	public void onRelease(int arg0) {	
	}
	public void onText(CharSequence arg0) {	
	}
	public void swipeDown() {	
	}
	public void swipeLeft() {	
	}
	public void swipeRight() {	
	}
	public void swipeUp() {	
	}
}
