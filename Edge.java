import java.util.Objects;

public class Edge implements Comparable<Edge> {
    Node Start;
    Node Target;
    String edgename;//-1 olmasý onlarýn komþu olduðunu gösterir.
    int distance;
    LineStatus line;

   public Edge(Node start,Node target){
       this.Start=start;
       this.Target=target;
       this.distance=625;
   }
    public Edge(Node s,Node t,int distance){
        this.Start=s;
        this.Target=t;
        edgename = "adsýz";
        this.distance=distance;
    }
    public Edge(Node s,Node t,int distance,String Name){
        this.Start=s;
        this.Target=t;
        edgename = Name;
        this.distance=distance;
    }
      public Edge(Node Start,Node Target,int lineid,int direction,int order){
        this.Start=Start;
        this.Target=Target;
        edgename = "adsýz";
        this.distance=625;
        line.direction=direction;
        line.lineid=lineid;
        line.order=order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;
        Edge edge = (Edge) o;
        return Start.equals(edge.Start) &&
                Target.equals(edge.Target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Start, Target, distance);
    }

    public String toString() {
        return String.format("("+Start.name+" ---> "+ Target.name +", Distance:"+distance+line+")");
    }
    public int compareTo(Edge otherEdge) {
        if (this.distance > otherEdge.distance) {
            return 1;
        }
        else return -1;
    }



}
