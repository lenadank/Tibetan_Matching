package tau.cs.wolf.tibet.percentage_apbt.misc;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;

public class BoundedExecutor {
    private final Executor exec;
    private final Semaphore semaphore;

    public BoundedExecutor(Executor exec, int bound) {
        this.exec = exec;
        this.semaphore = new Semaphore(bound);
    }

    public void submitTask(final Runnable command) throws InterruptedException, RejectedExecutionException {
    	System.out.println("_____ aquiring semaphore" );
    	semaphore.acquire();
        try {
            exec.execute(new Runnable() {
                public void run() {
                	System.out.println("_____ running submitted task" );
                    try {
                        command.run();
                    } 
                    finally {
                    	System.out.println("_____ submitted task ended" );
                        semaphore.release();
                    }
                }
            });
        } catch (RejectedExecutionException e) {
            semaphore.release();
            throw e;
        }
    }
}