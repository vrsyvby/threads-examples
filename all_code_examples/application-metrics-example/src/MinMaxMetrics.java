import java.util.ArrayList;

public class MinMaxMetrics {

    private volatile long min_price;
    private volatile long max_price;
    // Add all necessary member variables

    /**
     * Initializes all member variables
     */
    public MinMaxMetrics() {
        // Add code here
        min_price=Long.MIN_VALUE;
        max_price=Long.MIN_VALUE;

    }

    /**
     * Adds a new sample to our metrics.
     */
    public void addSample(long newSample) {
        synchronized (this){
            this.min_price=Long.min(newSample,this.min_price);
            this.max_price=Long.max(newSample,this.max_price);
        }
    }

    /**
     * Returns the smallest sample we've seen so far.
     */
    public long getMin() {

        return min_price;

        // Add code here
    }

    /**
     * Returns the biggest sample we've seen so far.
     */
    public long getMax() {


        return max_price;
    }
}