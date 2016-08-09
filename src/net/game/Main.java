package net.game;

import net.engine.core.Engine;

/**
 * The main method
 * @author Davis
 *
 */
public class Main
{

	/**
	 * Starts the application
	 * @param args the command-line arguments
	 */
	public static void main(String[] args)
	{
		Game theGame = new Game();
		
		Engine.getInstance().start(theGame);
	}
	
}
