@auto
import java.util.LinkedList;
import java.util.Objects;

public class Node {
    String name;
    int stopid;
    private boolean visited;
    LinkedList<Edge> nodeedges;
    int vehicletype;
    Node(int id, String Stopname) {
        this.stopid = id;
        this.name = Stopname;
        this.visited = false;
        this.nodeedges = new LinkedList<>();
    }
    Node(int id, String Stopname,int vehicletype ) {
        this.stopid = id;
        this.name = Stopname;
        this.visited = false;
        this.nodeedges = new LinkedList<>();
        this.vehicletype= vehicletype ;
    }


    boolean isVisited() {return visited;}
    void visit() {visited = true;}
    void unvisit() {visited = false;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
            Node node = (Node) o;
        return stopid == node.stopid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stopid);
    }
    public String toString() {
        return String.format("Name:"+name+" stopid: "+ stopid);
    }

}

