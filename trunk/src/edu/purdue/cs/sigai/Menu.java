package edu.purdue.cs.sigai;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Menu extends Activity implements OnClickListener {
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout );
        
        //create event listeners for the buttons
        Button buttonNewGame = (Button) findViewById( R.id.newgame);
        buttonNewGame.setOnClickListener(this);
        
        Button buttonStory = (Button) findViewById (R.id.story);
        buttonStory.setOnClickListener(this);
        
        Button buttonQuit = (Button) findViewById( R.id.quit);
        buttonQuit.setOnClickListener(this);
        
        
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.newgame:
			Intent i = new Intent(this, Game.class);
			startActivity(i);
			break;
			
		case R.id.quit:
			finish();
			break;
			
		case R.id.story:
			new AlertDialog.Builder(this) 
			.setMessage(R.string.story)
			.setPositiveButton("Close", null)
			.show();
		}

	}

}
