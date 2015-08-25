package edu.purdue.cs.sigai;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Game extends Activity  implements OnClickListener {
	
	private TileBoard board;
	private GameView gameView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        board = new TileBoard(30, 30, 2);  //row, col
        
        
      //give all the buttons action listeners
        Button buttonUp = (Button) findViewById( R.id.up);
        buttonUp.setOnClickListener(this);
        
        Button buttonDown = (Button) findViewById( R.id.down);
        buttonDown.setOnClickListener(this);
        
        Button buttonLeft = (Button) findViewById( R.id.left);
        buttonLeft.setOnClickListener(this);
        
        Button buttonRight = (Button) findViewById( R.id.right);
        buttonRight.setOnClickListener(this);
        
        /*
        Button buttonLook = (Button) findViewById(R.id.look);
        buttonLook.setOnClickListener(this);
        
        Button buttonUse = (Button) findViewById(R.id.use);
        buttonUse.setOnClickListener(this);
        */
        
        gameView = (GameView) findViewById(R.id.view);
        gameView.setGame(this);
        
        synchronized(this) {
        	notifyAll();
        }
    }
    
    
    public void onClick (View v) {
    	final Player player = board.getPlayer();
    	
    	// NOTE: This code only works correctly only if the user presses only one button at
		//		 a time. Not suitable for serious gameplay. Must rewrite.
		switch (v.getId()) {
    	 case R.id.up:
    		//move up
    		player.setDir(Player.UP);   // NOTE: here, the convention is +col down, +row right (Can be easily changed if desired)
    		break;
    	case R.id.down:
    		//move down
    		player.setDir(Player.DOWN);
    		break;
    	case R.id.left:
    		//move left
    		player.setDir(Player.LEFT);
    		break;
    	case R.id.right:
    		//move right
    		player.setDir(Player.RIGHT);
    		break;
    	/*
    	case R.id.use: 
    		//test win notification (currently has no real function)
    		endGame("you used it!");
    	case R.id.look:
    		return;
    		*/
    	}
		
		board.update();
		
		//check win condition
		if (player.getTile().isWin()) {
			endGame("Congratulations, you have won!");
		}
			
        synchronized(this) {
			board.dijk(player.getTile());
			
			//check loose condition
			if (player.getTile().hasZombie()) {
				endGame("You lose.  Please try again.");
			}
        	notifyAll();
        }
	}
    
    public TileBoard getBoard()
    {
    	return board;
    }
    
    //Message for victory or loss
    private void endGame (String message) {
    	new AlertDialog.Builder(this) 
			.setMessage(message)
			.setPositiveButton("New Map", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					board = new TileBoard(30, 30, 2);
					myNotify();
				}
			})
			.setNegativeButton("Main Menu", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//stop the renderer thread
					gameView.finish();
					finish();
				}
			})
			.show();
    }
    
    private void myNotify () {
    	synchronized (this) {
			notifyAll();
    	}
    }
}