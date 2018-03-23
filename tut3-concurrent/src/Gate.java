public class Gate extends Thread {

    private int id;
    private String name;
    private GateTypes type;
    private Counter people;

    public Gate(int id, String name, GateTypes type, Counter people) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.people = people;
    }

    public void run() {

        int visitors = 0;
        while(visitors <= 10) {

            switch(type) {
                case ENTRY: {
                    if(people.increment(this.name)) {
                        visitors++;
                    }
                    break;
                }

                case EXIT: {
                    if(people.decrement(this.name)) {
                        visitors++;
                    }
                    break;
                }
            }

            Simulate.HWinterrupt();
        }
    }
}

class Simulate {
    public static void HWinterrupt() {
        if (Math.random() < 0.5)
            try{
                Thread.sleep(200);
            } catch(InterruptedException ie) {};
    }
}
