package nl.mprog.evilhangman.controllers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nl.mprog.evilhangman.R;
import nl.mprog.evilhangman.models.Highscore;
import android.os.Bundle;
import android.app.Activity;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import nl.mprog.evilhangman.controllers.DatabaseHandler;

public class HighscoreActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_highscore);
		// Show the Up button in the action bar.
		setupActionBar();
		showHighscores();
	}

	@SuppressWarnings("deprecation")
	private void showHighscores() {
		ScrollView sv = new ScrollView(this);
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		sv.addView(ll);

		Highscore highScore = new Highscore(this);
		List<Highscore> AllHighscores = highScore.getHighscores();
		
		
//		Log.w("highscores size", ""+AllHighscores.size());

		if (AllHighscores.size() == 0){
			TextView tv = new TextView(this);
			tv.setMaxWidth(150);
			tv.setText("No Highscores available at this time, try to win a game!");
			ll.addView(tv);
		} else {
			Collections.sort(AllHighscores, new Comparator<Highscore>(){

				@Override
				public int compare(Highscore lhs, Highscore rhs) {
					// TODO Auto-generated method stub
					return lhs.getScore().compareTo(rhs.getScore());
				}
			});
			
			TextView highScoresView = null;        
			
			for(int i=0; i<AllHighscores.size(); i++){
				Log.w("HighscoreID", "i="+i+", "+AllHighscores.get(i).getName());
				highScoresView = new TextView(this);
				highScoresView.setText("Name: "+ AllHighscores.get(i).getName()+", Score: "+AllHighscores.get(i).getScore()+", Word: "+AllHighscores.get(i).getWord());
//				tv.setText("Name: "+ AllHighscores.get(i).getName()+", Score: "+AllHighscores.get(i).getScore()+", Word: "+AllHighscores.get(i).getWord());
				highScoresView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				ll.addView(highScoresView);
				highScoresView = null;
				
			}
		}
		
		this.setContentView(sv);





	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}



	//	@Override
	//	public boolean onCreateOptionsMenu(Menu menu) {
	//		// Inflate the menu; this adds items to the action bar if it is present.
	//		getMenuInflater().inflate(R.menu.game_menu, menu);
	//		return true;
	//	}

	//	@Override
	//	public boolean onOptionsItemSelected(MenuItem item) {
	//		switch (item.getItemId()) {
	//		case android.R.id.home:
	//			// This ID represents the Home or Up button. In the case of this
	//			// activity, the Up button is shown. Use NavUtils to allow users
	//			// to navigate up one level in the application structure. For
	//			// more details, see the Navigation pattern on Android Design:
	//			//
	//			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
	//			//
	////			NavUtils.navigateUpFromSameTask(this);
	//			return true;
	//		}
	//		return super.onOptionsItemSelected(item);
	//	}

}
