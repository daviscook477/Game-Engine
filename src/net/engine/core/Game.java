package net.engine.core;

/**
 * A game
 * @author Davis
 *
 */
public interface Game
{
	
	/**
	 * Renders the game
	 */
	void render();
	
	/**
	 * Updates the game
	 */
	void update();
	
	/**
	 * Updates the input
	 */
	void input();
	
}
