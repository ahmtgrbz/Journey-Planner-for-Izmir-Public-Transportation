public class pathoutput {
    String path;
    int totalcost;
    int counter;
    String aktarmaguragi;

    public pathoutput(String path,int totalcost,int counter,String aktarmaguragi){
        this.path=path;
        this.totalcost=totalcost;
        this.counter=counter;
        this.aktarmaguragi=aktarmaguragi;



    }

    @Override
    public String toString() {
        return "pathoutput{" +
                "path='" + path + '\'' +
                ", totalcost=" + totalcost +
                ", counter=" + counter +
                ", aktarmaguragi='" + aktarmaguragi + '\'' +
                '}';
    }
}
