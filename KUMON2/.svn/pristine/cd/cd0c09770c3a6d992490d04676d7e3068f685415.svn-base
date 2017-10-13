package kumon2014.markcontroltool;

import java.io.Serializable;
import java.util.Date;

public class Stopwatch  implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private long 	start = 0;
	private long 	stop = 0;
	private long 	pass = 0;
	public 	boolean IsRunning = false;
	
	public Stopwatch()
	{
		start = 0;
		stop = 0;
		pass = 0;
		IsRunning = false;
	}
	public void Start()
	{
		if(IsRunning == false) {
			start = new Date().getTime();
			stop = 0;
			IsRunning = true;
		}
	}
	public void Stop() 
	{
		if(IsRunning == true) {
			stop = new Date().getTime();
			pass += (stop - start);
			IsRunning = false;
		}
	}
	public long getTime()
	{
		return pass;
	}
}
