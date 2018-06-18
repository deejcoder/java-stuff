package today.doingit.monitors;

import java.util.ArrayList;

public class ProducerProblem {
    static ArrayList<Integer> list = new ArrayList<Integer>();

    static class Producer implements Runnable {

        ArrayList<Integer> list;

        public Producer(ArrayList<Integer> list) {
            this.list = list;
        }

        @Override
        public void run() {
            synchronized(list) {
                for(int i = 0; i < 20; i++) {
                    /*
                        This thread should wait until
                        there are less than five items.
                        The while loop is to avoid 'spurious' wake ups.
                     */
                    while(list.size() >= 2) {
                        try {
                            System.out.println("Producer is waiting...");
                            list.wait();
                        }
                        catch(InterruptedException ie) {
                            ie.printStackTrace();
                        }
                    }

                    System.out.println("produce = " + i);
                    list.add(i);
                    //Notify consumers there are items in the list
                    list.notifyAll();
                    try {
                        Thread.sleep(500);

                    }
                    catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
        }
    }

    static class Consumer implements Runnable {

        ArrayList<Integer> list;
        public Consumer(ArrayList<Integer> list) {
            this.list = list;
        }

        @Override
        public void run() {
            synchronized (list) {
                for(int i = 0; i < 10; i++) {
                    while(list.isEmpty()) {
                        System.out.println("Consumer waiting...");
                        try {
                            list.wait();
                        }
                        catch (InterruptedException ie) {
                            ie.printStackTrace();
                        }
                    }

                    int value = list.remove(0);
                    System.out.println("Consume = " + value);
                    list.notifyAll();
                    try {
                        Thread.sleep(500);
                    }
                    catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Thread producer = new Thread(new Producer(list));
        Thread consumer = new Thread(new Consumer(list));
        Thread consumer2 = new Thread(new Consumer(list));
        producer.start();
        consumer.start();
        consumer2.start();
    }

}
