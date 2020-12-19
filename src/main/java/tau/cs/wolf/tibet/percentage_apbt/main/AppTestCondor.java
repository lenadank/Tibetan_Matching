package tau.cs.wolf.tibet.percentage_apbt.main;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class AppTestCondor {
	public static void main(String[] args) throws InterruptedException {
	    int sleep_time;
	    int input;
	    int failure;

	    if (args.length != 2) {
	        System.out.println("Usage: "+AppTestCondor.class.getSimpleName()+" <sleep-time> <integer>");
	        failure = 1;
	    } else {
	        sleep_time = Integer.parseInt(args[0]);
	        input      = Integer.parseInt(args[1]);

	        System.out.println(String.format("Thinking really hard for %d seconds...", sleep_time));
	        TimeUnit.SECONDS.sleep(sleep_time);
	        System.out.println("We calculated: "+ ((new Random()).nextInt(10)+1) * input);
	        failure = 0;
	    }
	    System.exit(failure);
	}
}
