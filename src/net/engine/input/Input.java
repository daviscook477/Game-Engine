package net.engine.input;

import net.engine.math.Vector2f;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * Handles input to the game
 * @author Davis
 */
public class Input
{

    public static final int LEFT_CLICK = 0;
    public static final int RIGHT_CLICK = 1;
	
	public static int NUM_KEYCODES = -1;
    public static int NUM_MOUSEBUTTONS = -1;
        
    private static KeyStates[] keyStates;
    private static MouseStates[] mouseStates;
	
    private Input() {}
        
	/**
	 * Creates the input
	 */
	public static void createInput()
	{
		try
		{
			Keyboard.create();
			Mouse.create();
		}
		catch (LWJGLException e)
		{
			e.printStackTrace();
		}
		
        //Determine how many keys the keyboard has
        NUM_KEYCODES = Keyboard.getKeyCount();
        keyStates = new KeyStates[NUM_KEYCODES];
        //Determine how many buttons the mouse has
        NUM_MOUSEBUTTONS = Mouse.getButtonCount();
        mouseStates = new MouseStates[NUM_MOUSEBUTTONS];
	}
	
	/**
	 * Cleans up the input
	 */
	public static void dispose()
	{
		Keyboard.destroy();
        Mouse.destroy();
	}
	
	/**
	 * Updates the input
	 */
	public static void update()
	{
	    for (int i = 0; i < NUM_KEYCODES; i++) //Cycle through every key
        {
	        if (keyStates[i] == null) //If the key hasn't had a state set yet
	        {
	            if (isKeyDown(i)) //Set it to have been the current state of the key
                {
	                keyStates[i] = KeyStates.WAS_DOWN;
                }
                else
                {
                    keyStates[i] = KeyStates.WAS_UP;
                }
            }
            if (isKeyDown(i)) //If the key is down right now
            {
                if (keyStates[i] == KeyStates.JUST_DOWN)
                { 
                    keyStates[i] = KeyStates.WAS_DOWN;
                }
                else
                {
                    if (keyStates[i] == KeyStates.JUST_UP || keyStates[i] == KeyStates.WAS_UP)
                    {
                        keyStates[i] = KeyStates.JUST_DOWN;
                    }
                }
            }
            else //If the key is up right now
            {
                if (keyStates[i] == KeyStates.JUST_UP)
                {
                    keyStates[i] = KeyStates.WAS_UP;
                }
                else
                {
                    if (keyStates[i] == KeyStates.JUST_DOWN || keyStates[i] == KeyStates.WAS_DOWN)
                    {
                        keyStates[i] = KeyStates.JUST_UP;
                    }
                }
            }
        }
	    for (int i = 0; i < NUM_MOUSEBUTTONS; i++) //Cycle through every mouse button
        {
            if (mouseStates[i] == null) //If the mouse button hasn't had a state set yet
            {
                if (isMouseButtonDown(i)) //Set it to have been the current state of the mouse button
                {
                    mouseStates[i] = MouseStates.WAS_CLICKED;
                }
                else
                {
                    mouseStates[i] = MouseStates.WAS_RELEASED;
                }
            }
            if (isMouseButtonDown(i)) //If the mouse is down right now
            {
                if (mouseStates[i] == MouseStates.JUST_CLICKED)
                {
                    mouseStates[i] = MouseStates.WAS_CLICKED;
                }
                else
                {
                    if (mouseStates[i] == MouseStates.JUST_RELEASED || mouseStates[i] == MouseStates.WAS_RELEASED)
                    {
                        mouseStates[i] = MouseStates.JUST_CLICKED;
                    }
                }
            }
            else //If the mouse is up right now
            {
                if (mouseStates[i] == MouseStates.JUST_RELEASED)
                {
                    mouseStates[i] = MouseStates.WAS_RELEASED;
                }
                else
                {
                    if (mouseStates[i] == MouseStates.JUST_CLICKED || mouseStates[i] == MouseStates.WAS_CLICKED)
                    {
                        mouseStates[i] = MouseStates.JUST_RELEASED;
                    }
                }
            }
        }
	}

	/**
	 * Returns if a key is currently pushed
	 * @param keyCode the key to check
	 * @return if it is pushed down
	 */
	public static boolean isKeyDown(int keyCode)
	{
		return Keyboard.isKeyDown(keyCode);
	}
	
	/**
	 * Returns true the first update after a key is pressed then false afterwards
	 * @param keyCode the key to check for
	 * @return true the first update after a key is pressed then false afterwards
	 */
	public static boolean keyJustDown(int keyCode)
	{
		if (keyCode < 0 || keyCode >= NUM_KEYCODES)
        {
		    return false;
        }
        else
        {
            return (keyStates[keyCode] == KeyStates.JUST_DOWN);
        }
	}
	
	/**
	 * Returns true the first update after a key is released then false afterwards
	 * @param keyCode the key to check for
	 * @return true the first update after a key is released then false afterwards
	 */
	public static boolean keyJustUp(int keyCode)
	{
		if (keyCode < 0 || keyCode >= NUM_KEYCODES)
        {
		    return false;
        }
        else
        {
            return (keyStates[keyCode] == KeyStates.JUST_UP);
        }
	}
        
        /**
         * Returns if a mouse button is down
         * @param mouseCode the mouse button to test for
         * @return if that mouse button is down
         */
        public static boolean isMouseButtonDown(int mouseCode)
        {
            return Mouse.isButtonDown(mouseCode);
        }
        
        /**
	 * Returns true the first update after a mouse button is pressed then false afterwards
	 * @param mouseCode the mouse button to check for
	 * @return true the first update after a mouse button is pressed then false afterwards
	 */
	public static boolean mouseButtonJustDown(int mouseCode)
	{
		if (mouseCode < 0 || mouseCode >= NUM_MOUSEBUTTONS)
        {
		    return false;
        }
        else
        {
            return (mouseStates[mouseCode] == MouseStates.JUST_CLICKED);
        }
	}
	
	/**
	 * Returns true the first update after a mouse button is released then false afterwards
	 * @param mouseCode the mouse button to check for
	 * @return true the first update after a mouse button is released then false afterwards
	 */
	public static boolean mouseButtonJustUp(int mouseCode)
	{
		if (mouseCode < 0 || mouseCode >= NUM_MOUSEBUTTONS)
        {
		    return false;
        }
        else
        {
            return (mouseStates[mouseCode] == MouseStates.JUST_RELEASED);
        }
	}
    
	/**
	 * Returns the location of the mouse
	 * @return the location of the mouse as a Vector2f
	 */
    public static Vector2f getMousePos()
    {
        return (new Vector2f(Mouse.getX(), Mouse.getY()));
    }
    
    /**
     * Sets the mouse location
     * @param pos the new location
     */
    public static void setMousePosition(Vector2f pos)
    {
    	Mouse.setCursorPosition((int) pos.x, (int) pos.y);
    }
    
    /**
     * Either shows or disables the cursor
     * @param enabled if it is enabled or disabled
     */
    public static void setCursor(boolean enabled)
    {
    	Mouse.setGrabbed(!enabled);
    }
	
}
