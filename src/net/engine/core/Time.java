package net.engine.core;

/**
 * Timer that provides simple time functions
 * @author Davis
 *
 */
public class Time
{
	
	public static final long SECOND = 1000000000L;

	private static double delta;
        
	/**
	 * This class should not hava instances
	 */
    private Time() {}
	
    /**
     * Gets time in nano seconds
     * @return
     */
	public static long getTime()
	{
		return System.nanoTime();
	}
	
	/**
	 * Gets the delta
	 * @return the delta
	 */
	public static double getDelta()
	{
		return delta;
	}
	
	/**
	 * Sets the delta
	 * @param delta the new delta
	 */
	public static void setDelta(double delta)
	{
		Time.delta = delta;
	}
	
}
