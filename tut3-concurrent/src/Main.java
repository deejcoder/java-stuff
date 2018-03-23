


enum GateTypes {
    EXIT,
    ENTRY
}

public class Main {

    public static void main(String[] args) {

        Counter people = new Counter();

        Gate northwest = new Gate(0, "North West", GateTypes.ENTRY, people);
        Gate southwest = new Gate(1, "South West", GateTypes.EXIT, people);
        Gate southeast = new Gate(2, "South East", GateTypes.EXIT, people);
        Gate northeast = new Gate(3, "North East", GateTypes.ENTRY, people);

        northwest.start();
        southwest.start();
        southeast.start();
        northeast.start();
    }
}
