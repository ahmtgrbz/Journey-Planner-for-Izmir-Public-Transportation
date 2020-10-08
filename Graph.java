import static java.lang.Integer.parseInt;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Graph {
    public static Set<Node> nodes = new HashSet<>();

    static HashMap<Integer, Node> Stoptututcu = new HashMap<Integer, Node>();
    static HashMap<Integer, Node> Komsututucu = new HashMap<Integer, Node>();
    static private String aktarmadurag�= "Aktarma yok";


    public static void addNode(Node n) {
        nodes.addAll(Arrays.asList(n));
    }

    public static void addEdge(Node a, Node b, int weight,String Name) {
        nodes.add(a);
        nodes.add(b);
        a.nodeedges.add(new Edge(a, b, weight,Name));
    }

    public static void tripaddEdge(Node a, Node b,int LineId,int Direction,int Order) {
        nodes.add(a);
        nodes.add(b);
        for (Node node : nodes) {
            if (node.equals(a)) {
                for(Node node1 : nodes){
                    if(node1.equals(b)){
                        for (Edge edge: node.nodeedges){
                            if (edge.equals(new Edge(a,b,LineId,Direction,Order)));{
                                edge.line.lineid = LineId;
                                edge.line.direction= Direction;
                                edge.line.order = Order;
                                edge.distance=625;//mesafe yok ise;
                                return;
                            }
                        }
                    }
                }
            }
        }
      return;
    }

    public static  void dstupdateEdge(Node a, Node b,int distance){

        for (Node node : nodes) {
            if (node.equals(a)) {
                for(Node node1 : nodes){
                    if(node1.equals(b)){
                        for (Edge edge: node.nodeedges){
                            if (edge.equals(new Edge(a,b,distance))){
                                edge.distance=distance;
                                node.nodeedges.add(new Edge(a,b,distance));
                                return;
                            }
                        }
                    }
                }
            }
        }
        return;
    }

    public static void linetxtupdateEdge(int LineId,int LineNo,String Name,int VehicleTypeId){

        for (Node node:nodes) {
            for (Edge edge:node.nodeedges) {
                if (edge.line.lineid == LineId){
                    edge.line.lineno=LineNo;
                    edge.edgename= Name;
                    edge.line.vehicle=VehicleTypeId;

                    return;
                }
            }
        }
    }


    public static void printEdges() {

        for (Node node : nodes) {
            LinkedList<Edge> edges = node.nodeedges;

            if (edges.isEmpty()) {
                System.out.println("Node " + node.name + " has no edges.");
                continue;
            }
            System.out.print("Node " + node.name + " has edges to: ");

            for (Edge edge : edges) {
                System.out.print(edge.Target.name + "(" + edge.distance + ") ");
            }
            System.out.println();
        }
    }

    public static boolean hasEdge(Node source, Node destination) {
        LinkedList<Edge> edges = source.nodeedges;
        for (Edge edge : edges) {
            if (edge.Target == destination) {
                return true;
            }
        }
        return false;
    }

    public static void resetNodesVisited() {
        for (Node node : nodes) {
            node.unvisit();
        }
    }

    public static void main(String[] args) throws IOException,FileNotFoundException {
        String stoplist = "src\\Stop.txt";
        String dst = "src\\distance.txt";
        String trp = "src\\Trip.txt";
        String linetxt = "src\\Line.txt";
        String line;
        Graph graphWeighted = new Graph();
        String Dual = null;
        int i = 0;

        try {
        BufferedReader FileBr1 = new BufferedReader(new FileReader(stoplist));
        FileBr1.readLine();
        while ((line = FileBr1.readLine()) != null) {

            String[] stp = line.split(";");
            int VehicleTypeId = parseInt(stp[4]);//duralardaki ara� tipini �ekiyorum belki ileride kullan�rsam diye.
            int StopId1 = parseInt(stp[0]);
            String StopName = stp[1];
            Stoptututcu.put(StopId1, new Node(StopId1, StopName,VehicleTypeId));// Dosyadan bilgileri �eker �ekmez bir hasmape (int-node) olarak verileri indexliyorum.
            addNode(Stoptututcu.get(StopId1));//new Node(StopId1,StopName) indexledi�im node'a ula�mak i�in get fonksiyonu ve durakidsini(ket) �a��r�p nodu grafi�e ekletiyorum.

            String NeighborStopswithdistance = stp[5]; // kom�ular� ve uzunluklar�n� temiz �ekilde elde etmek i�in bir ka� i�lem yap�yorum.
            if (NeighborStopswithdistance.equals("NULL")) {

            } else {
                String dist[] = NeighborStopswithdistance.split(".");
                for (int j = 0; j < dist.length; j++) {
                    dist[j] = Dual;
                    String trans[] = Dual.split(":");
                    int b1;
                    int b2;
                    b1 = parseInt(trans[0]);
                    b2 = parseInt(trans[1]);
                    String name = StopName + "'in komsusu" + trans[0];
                    Komsututucu.put(b1, new Node(b1, name));// elde etti�im kom�u idleri ve uzakl�klar� yine bir hashmap i�inde sakl�yorum.(Kom�ularda bir node)
                    addNode(Komsututucu.get(b1)); //new Node(b1,name)//Nodelar� mapa ekliyorum.
                    addEdge(Stoptututcu.get(StopId1), Komsututucu.get(b1), b2, "-1");//kom�ular aras� mesafeyi edge olarak ekliyorum.
                }
            }
        }
        line = null;
        FileBr1.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException FileBr1");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("NumberFormatException FileBr1 ");
        }
        try {

        BufferedReader FileBr3 = new BufferedReader(new FileReader(trp));
        FileBr3.readLine();
        while ((line = FileBr3.readLine()) != null) {
            String[] sre = line.split(";",4);
            int tmpLineId = parseInt(sre[0]);
            int tmpDirection = parseInt(sre[1]);
            int tmpOrder = parseInt(sre[2]);
            int tmpStopId = parseInt(sre[3]);
            if((line = FileBr3.readLine()) != null){
                sre = line.split(";",4);
                int LineId = parseInt(sre[0]);
                int Direction = parseInt(sre[1]);
                int Order = parseInt(sre[2]);
                int StopId = parseInt(sre[3]);
                if(tmpLineId == LineId){

                    Graph.tripaddEdge((Stoptututcu.get(tmpStopId)),(Stoptututcu.get(StopId)),LineId,Direction,Order);
                }
            }

        }
        line = null;
        FileBr3.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException FileBr3");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("NumberFormatException FileBr3");
        }

        try {
        BufferedReader FileBr2 = new BufferedReader(new FileReader(dst));
        FileBr2.readLine();
        while ((line = FileBr2.readLine()) != null) {
            String[] total = line.split(";",3);
            int OriginStopId = parseInt(total[0]);
            int DestinationStopId = parseInt(total[1]);
            int Distance = parseInt(total[2]);

            dstupdateEdge(Stoptututcu.get(OriginStopId), Stoptututcu.get(DestinationStopId), Distance);


        }
        line = null;
        FileBr2.close();
        } catch (IOException e) {
        e.printStackTrace();
        System.out.println("IOException FileBr2");
        } catch (NumberFormatException e) {
        e.printStackTrace();
        System.out.println("NumberFormatException FileBr2");
    }

        try {
        BufferedReader FileBr4 = new BufferedReader(new FileReader(linetxt));
        FileBr4.readLine();
        while ((line = FileBr4.readLine()) != null) {
            String[] linea = line.split(";",4);
            int LineId = parseInt(linea[0]);
            int LineNo= parseInt(linea[1]);
            String Name = linea[2];
            int VehicleTypeId = parseInt(linea[3]);
            linetxtupdateEdge(LineId,LineNo,Name,VehicleTypeId);


        }
        line = null;
        FileBr4.close();

        Searchpath(10036,40120,2);
        Graph.printEdges();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException FileBr4");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("NumberFormatException FileBr4");
        }

    }
    public static void Searchpath(int x, int y, int state) throws IOException {

        if (state <= 2) {
            for (Node node : nodes) {
                if (node.stopid == x) {
                    for (Node node1 : nodes) {
                        if (node1.stopid == y) {
                            DijkstraShortestPath(node, node1,state);
                            return;
                        }
                    }
                }
            }
        }else{
            System.out.println("check your state value 1:Fewer stops,2:Minimum distance");
        return;
        }

    }





    public static void DijkstraShortestPath(Node start, Node end, int state)  {




        /* Belirli bir noda hangi noddan geldi�imizi tutar (hangi edge yardm� edgelerin start-stop tutmas�ndan yararlan�yoruz.)
         yap�s� node,node,her bir d���mde geldi�imiz bir alt nodu bir mapde takip ediyoruz b�ylece paht'de detayl� g�r�� kazan�yoruz.temp tutmak gibi.*/
        HashMap<Node, Node> tracker = new HashMap<>();
        Set<Edge> edgefortransfer= new LinkedHashSet<Edge>();
        tracker.put(start, null);
        // Algoritmay� �al��t�rmak i�in kulland���m yap� map yap�s�(uzakl�klar� ve nodelar� tutuyor) nodelar� buna ekleyip hesaplamalar� ve tercihleri bu yap� �zerinden yapaca��z.
        HashMap<Node, Integer> shortestPath = new HashMap<>();

        // ba�lang�cta start nodu hari� hepsini sonsuz yap�yoruz ba�lang�c� ise 0 olarak ayarl�yoruz ve shortestPath'a ekliyoruz.

        for (Node node : nodes) {
            if (node == start)
                shortestPath.put(start, 0);
            else shortestPath.put(node, Integer.MAX_VALUE);
        }
        if(state==1) { //FEWER STOPS i�in !!!

            // Algoritma eklemeden sonra ba�l�yor. Ba�lang�� node'unda ba�lant�s� olan t�m edgelerin �zerinden ge�iyorum.
            for (Edge edge : start.nodeedges) {
                shortestPath.put(edge.Target, 1);
                tracker.put(edge.Target, start);
            }
        }

            try { //** try
        if(state==2) { //M�N�MUM D�STANCE i�in !!!

        // Algoritma eklemeden sonra ba�l�yor. Ba�lang�� node'unda ba�lant�s� olan t�m edgelerin �zerinden ge�iyorum.
            for (Edge edge : start.nodeedges) {
                shortestPath.put(edge.Target, edge.distance);
                tracker.put(edge.Target, start);
            }
        }

        start.visit();//ziyaret etti�imi i�aretlemem gerekir ki biradaha kar��t�r�p �st�nden ge�meyeyim.  start nodunu i�aretliyorum bir flag asl�nda.(boolean true oluyor.)

        //while d�ng�s� ziyaret edilmemi� (�uanda bulundu�umuz node dan ula�abilece�imiz.)herhangi bir edge veya noda sahipse d�ng� �al��maya devam eder.
        while (true) {
            Node currentNode = closestUnvisited(shortestPath);

            //e�er herhangi yeni bir noda ula�amazsak ve en ba�ta parametre olarak verdi�imiz end node'a gelemediysek ba�lang�� ve end aras�nda ba�lant� yoktur.
            if (currentNode == null) {
                System.out.println(start.name + " ve " + end.name+"aras�nda bir yol bulunamam��t�r.");
                System.out.println("L�tfen ba�ka bir biti� noktas� se�in.");
                return;
            }

            // e�er sona geldiysek end noda ula�t�ysak yolu yazd�r�r�z ve sonu� al�r�z.a�a� yap�s� gibi child -> parent ili�kisi var.
            if (currentNode == end) {
                System.out.println(start.name+ " ile " + end.name + " aras�ndaki en k�sa yol :");
                int counter=0;
                int linenos = 0;
                Node child = end;
                String path = end.name;
                while (true) {

                    Node parent = tracker.get(child);           //Aktarma verilerini almak i�in d���nd���m b�l�m.
                    for (Edge edge:child.nodeedges){            //child -> parent gezdi�im i�in child ile parent aras�nda olan edgeleri bir listeye koyuyorum.
                        if(edge.Start.equals(parent)){          //listeye koyman�n yan� s�ra 1 defaya mahsus olmak �zere line no al�yorum ama sonda y�r�me olma ihtimali
                            edgefortransfer.add(edge);         // olmas�ndan dolay� ve 2 tane arka arkaya y�r�me olmayaca��ndan dolay�(olursa zaten sonu� olarak kabul etmeyece�iz)
                            if(edgefortransfer.size()<=2 && edge.line.lineno != -1){     // garanti olmas� ad�na <= lik kullanarak if blo�una 2 tane line no al�yorum.
                                linenos=edge.line.lineno;
                            }
                        }
                    }
                    if (parent == null) {
                        break;
                    }

                    path = parent.name + " " + path;
                    child = parent;
                    counter++;
                }

                int takeornot=0;//aktarma say�m�n s�n�rlay�c�s�
                for (Edge edge:edgefortransfer) {
                    if(parseInt(edge.edgename) == -1){
                        continue;
                    }
                    if(edge.line.lineno == linenos) {
                        if (takeornot == 0) {
                            System.out.println(path);
                            System.out.println(" Path1: " + path + edge.line.lineno);
                            System.out.println("Origin Stop: " + start.name);
                            System.out.println("Destination Stop:" + end.name);
                            System.out.println("Line:" + edge.line.lineno + " " + edge.edgename + " (Direction - " + edge.line.direction + " )");
                            System.out.println("Toplam gidilecek durak say�s�:" + (edgefortransfer.size() + 1));//y�r�meyide ekledim varsa,oda durak de�i�tirme.
                            System.out.println("Yolun uzunlugu: " + shortestPath.get(end));
                            continue;
                        }

                    }else{
                        if (takeornot < 1) {
                        takeornot++;
                        System.out.println("Aktarma");
                        System.out.println("Line:"+ edge.line.lineno+ " " + edge.edgename + " (Direction - " + edge.line.direction + " )");
                        System.out.println("Origin Stop: " + start.name);
                        System.out.println("Destination Stop:" + end.name);
                        }
                    }
                }
                takeornot=0;
                return;
            }
            currentNode.visit();

            //yukar�daki iflere girmiyor ise mevcut nodumuza edge ile ba�l�  olan t�m ziyaret edilmemi� nodelardan ge�iyoruz ve mevcut node'muzdan ge�en en k�sa yol
            // de�erinin daha �nce sahip oldu�umuzdan daha iyi(k���k) olup olmad���n� kontrol ediyoruz.
            for (Edge edge : currentNode.nodeedges) {
                if (edge.Target.isVisited())
                    continue;

                if (shortestPath.get(currentNode)
                        + edge.distance
                        < shortestPath.get(edge.Target)) {
                    shortestPath.put(edge.Target,
                            shortestPath.get(currentNode) + edge.distance);
                    tracker.put(edge.Target, currentNode);
                }
            }
        }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private static Node closestUnvisited(HashMap<Node, Integer> shortestPathMap) {

        Integer shortestDistance = Integer.MAX_VALUE;
        Node closestReachableNode = null;
        for (Node node : nodes) {
            if (node.isVisited())
                continue;

            Integer currentDistance = shortestPathMap.get(node);
            if (currentDistance == Integer.MAX_VALUE)
                continue;

            if (currentDistance < shortestDistance) {
                shortestDistance = currentDistance;

                closestReachableNode = node;

            }
        }
        return closestReachableNode;
    }




}
