package nl.mprog.evilhangman.controllers;
import java.util.List;
import java.util.Locale;
import nl.mprog.evilhangman.R;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.Keyboard.Key;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
public class GameKeyboardView extends KeyboardView{
	private MainActivity ctx;

	/**
	 * Class constructor
	 * @param ctx
	 * @param attrs
	 */
	public GameKeyboardView(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);
		this.ctx = (MainActivity) ctx;
	}
	/**
	 * Class constructor
	 * @param ctx
	 * @param attrs
	 * @param defStyle
	 */
	public GameKeyboardView(Context ctx, AttributeSet attrs, int defStyle) {
		super(ctx, attrs, defStyle);
		this.ctx = (MainActivity) ctx;
	}	
	/**
	 * Slides up the keyboard with an animation
	 */
	public void slideUp() {
		if(this.getVisibility() != View.VISIBLE) {
			this.setVisibility(View.VISIBLE);

			Animation a = AnimationUtils.loadAnimation(this.ctx, R.anim.slide_up_keyboard);
			this.startAnimation(a);
		}
	}

	/**
	 * Slides down the keyboard with an animation
	 */
	public void slideDown() {
		if(this.getVisibility() == View.VISIBLE) {
			Animation a = AnimationUtils.loadAnimation(this.ctx, R.anim.slide_down_keyboard);
			a.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationEnd(Animation arg0) {
					GameKeyboardView.this.clearAnimation();
					GameKeyboardView.this.setVisibility(View.INVISIBLE);					
				}

				@Override
				public void onAnimationRepeat(Animation arg0) {}

				@Override
				public void onAnimationStart(Animation arg0) {}            	
			});
			this.startAnimation(a);
		}
	}

	/**
	 * Changes a visual keyboard key label with a string.
	 * @param key_press
	 * @param label
	 */
	public void setKeyboardKeyLabel(final int key_press, final String label) {
		this.ctx.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				KeyboardView keyboardView = (KeyboardView) GameKeyboardView.this.ctx.findViewById(R.id.keyboardView);
				Keyboard keyboard = keyboardView.getKeyboard();
				List<Key> keys = keyboard.getKeys();
				for(Key key : keys) {
					if(key.label.toString().toLowerCase(Locale.getDefault()).charAt(0) == key_press) {
						key.label = label;
						keyboardView.invalidateKey(keys.indexOf(key));
					}
				}
			}    		
		});
	}
	/**
	 * Returns the visual keyboard to default
	 */
	public void resetKeyboard() {
		this.ctx.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				KeyboardView keyboardView = (KeyboardView) GameKeyboardView.this.ctx.findViewById(R.id.keyboardView);
				Keyboard keyboard = new Keyboard(GameKeyboardView.this.ctx, R.layout.custom_keyboard);
				keyboardView.setKeyboard(keyboard);    	
			}    		
		});
	}
}
