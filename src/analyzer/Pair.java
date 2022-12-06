package analyzer;

public class Pair<Integer,Double> implements Comparable<Pair<Integer,Double>>{
    public int value;
    public double weight;
    public Pair(int t, double w){
        this.value = t;
        this.weight = w;
    }

    @Override
    public int compareTo(Pair o) {
        if (this.weight == o.weight) {
            return 0;
        } else if (this.weight > o.weight) {
            return 1;
        } else {
            return -1;
        }
    }
}
