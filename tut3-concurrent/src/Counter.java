public class Counter {

    //must be volatile to allow threads to share
    volatile int count;

    public Counter() {
        count = 0;
    }

    //Counter.increment & decrement should be kept as minimal as possible
    synchronized boolean increment(String name) {
        count++;
        System.out.println(name + " increasing..., new value: " + count);
        return true;
    }

    synchronized boolean decrement(String name) {
        if(count >= 1) {
            count--;
            System.out.println(name + " decreasing..., new value: " + count);
            return true;
        }
        return false;
    }
}
