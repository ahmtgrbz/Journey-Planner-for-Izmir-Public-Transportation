public class LineStatus {
    int vehicle;
    int lineno=-1;
    int lineid;
    int direction;
    int order;

    public String toString() {
        return String.format(" Vehicle: "+vehicle+" Lineno: "+ lineno +" Distance: "+lineid +" Direction: "+direction);
    }

}
