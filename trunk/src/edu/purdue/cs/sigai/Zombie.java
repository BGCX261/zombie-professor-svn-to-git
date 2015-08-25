package edu.purdue.cs.sigai;

public class Zombie extends Sprite {

	public Zombie(int row, int col, TileBoard board)
	{
		super(row,col,board);
		super.tile.setZombie(true);
	}
	
	@Override
	public void update() {
		Tile newTile = tile.getPrev();
		
		//zombies must occupy a space, and cannor enter a wall or another zombie
		if (newTile != null && !newTile.isWall() && !newTile.hasZombie()) {
			tile.setZombie(false);
			newTile.setZombie(true);
			tile = newTile;
		}
			
	}
}
