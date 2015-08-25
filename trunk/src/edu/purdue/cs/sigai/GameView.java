package edu.purdue.cs.sigai;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
	private RenderThread render;
	private Game game;
	
	private class RenderThread extends Thread
	{
		private final SurfaceHolder surfHolder;
		private final Game game;
		private Boolean isDone;
		private Drawable playerBmp;
		private Drawable wallBmp;
		private Drawable zombieBmp;
		
		public RenderThread(SurfaceHolder surfHolder, Game game)
		{
			this.surfHolder = surfHolder;
			this.game = game;
			isDone = false;
			
			// Initialize bitmaps
			Resources rsrc = game.getResources();
			playerBmp = rsrc.getDrawable(R.drawable.character1);
			wallBmp = rsrc.getDrawable(R.drawable.wall);
			zombieBmp = rsrc.getDrawable(R.drawable.zombie);
		}
		
		public void run()
		{
			while (!getIsDone()) {
				render();
				
			}
		}
		
		public boolean getIsDone()
		{
			synchronized (isDone) {
				return isDone;
			}
		}
		
		public void finish()
		{
			synchronized (isDone) {
				isDone = true;
			}
			
		}
		
		public void render()
		{
			final Canvas can = surfHolder.lockCanvas();
			
			if (game == null) return;
			
			synchronized (game) {
				// Do drawing code here
				final Tile[][] tiles = game.getBoard().getBoard();
				
				clearScreen(can);
				
				// Draw all the background tiles
				for (int row = 0; row < tiles.length; row++)
					for (int col = 0; col < tiles[row].length; col++)
						drawTile(can, tiles[row][col]);
				
				drawPlayer(can, game.getBoard().getPlayer());
				if (game.getBoard().getNumZombs() > 0) 
					drawZombie(can, game.getBoard().getZombie1());
				if (game.getBoard().getNumZombs() > 1) 
					drawZombie(can, game.getBoard().getZombie2());
				
				surfHolder.unlockCanvasAndPost(can);
				
				try {
					game.wait();
				} catch (InterruptedException e) { }
			}
		}
		
		private void clearScreen(Canvas can)
		{
			final Rect screen = surfHolder.getSurfaceFrame();
			Paint paint = new Paint();
			// Clear the screen
			paint.setColor(Color.BLACK);
			can.drawRect(screen,paint);
		}
		
		private void drawTile(Canvas can, Tile t)
		{
			Paint paint = new Paint();
			final int tileX = TileBoard.tileWidth, tileY = tileX;
			
			if (t.isWall()) {
				wallBmp.setBounds(tileX*t.getCol(), tileY*t.getRow(), tileX*(t.getCol()+1), tileY*(t.getRow()+1));
				wallBmp.draw(can);
				return;
			}
				
			if (t.getWeight() == Tile.INF)
				paint.setColor(Color.RED);
			else
				paint.setColor(Color.argb(255, 0, 0, (t.getWeight()*5 <= 255) ? t.getWeight()*5 : 255));
			can.drawRect(tileX*t.getCol(), tileY*t.getRow(), tileX*(t.getCol()+1), tileY*(t.getRow()+1), paint);
		}
		
		private void drawPlayer(Canvas can, Player p)
		{
			final int tileX = TileBoard.tileWidth, tileY = tileX;
			final Tile t = p.getTile();
			playerBmp.setBounds(tileX*t.getCol(), tileY*t.getRow(), tileX*(t.getCol()+1), tileY*(t.getRow()+1));
			playerBmp.draw(can);
		}
		
		private void drawZombie(Canvas can, Zombie z)
		{
			final int tileX = TileBoard.tileWidth, tileY = tileX;
			final Tile t = z.getTile();
			zombieBmp.setBounds(tileX*t.getCol(), tileY*t.getRow(), tileX*(t.getCol()+1), tileY*(t.getRow()+1));
			zombieBmp.draw(can);
		}
	}
	
	public GameView(Context context)
	{
		super(context);
		getHolder().addCallback(this);
	}
	public GameView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		getHolder().addCallback(this);
	}
	public GameView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		getHolder().addCallback(this);
	}
	
	public void setGame(Game game)
	{
		this.game = game;
	}
	
	public void finish () {
		render.finish();
		
		//Prevent deadlock
		synchronized (game) { game.notifyAll(); }

		try {
			render.join();
		} catch (InterruptedException e) {}
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		
	}
	@Override
	public void surfaceCreated(SurfaceHolder hold) {
		render = new RenderThread(getHolder(), game);
		render.start();
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder hold) {
		render.finish();
		try {
			render.join();
		} catch (InterruptedException e) { }
		render = null;
	}
	
	
	private void errorMessage (String message) {
    	new AlertDialog.Builder(game) 
			.setMessage(message)
			.setPositiveButton("OK", null)
			.show();
    }
}
