package nl.mprog.evilhangman.controllers;

import nl.mprog.evilhangman.R;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

/**
 * GameGestureDetectorListener is a simple class that detects swipe up and swipe down gestures specifically designed
 * for revealing and concealing the keyboard.
 * @author Jeroen Grootendorst
 *
 */
public class GameGestureDetectorListener implements OnGestureListener {
	

	private final MainActivity ctx;
	
	/**
	 * Class constructor specifying a context
	 * @param ctx
	 * @param game
	 */
	public GameGestureDetectorListener(MainActivity ctx) {
		this.ctx = ctx;
	}
	
	@Override
	public boolean onDown(MotionEvent arg0) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent start, MotionEvent finish, float x, float y) {
		GameKeyboardView gkv = (GameKeyboardView) this.ctx.findViewById(R.id.keyboardView);
        if (start.getRawY() < finish.getRawY()) {
        	gkv.slideDown();
        } else {
        	gkv.slideUp();
        }
        return true;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,	float arg3) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		return false;
	}

}
