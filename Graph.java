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
    static private String aktarmaduragý= "Aktarma yok";


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
            int VehicleTypeId = parseInt(stp[4]);//duralardaki araç tipini çekiyorum belki ileride kullanýrsam diye.
            int StopId1 = parseInt(stp[0]);
            String StopName = stp[1];
            Stoptututcu.put(StopId1, new Node(StopId1, StopName,VehicleTypeId));// Dosyadan bilgileri çeker çekmez bir hasmape (int-node) olarak verileri indexliyorum.
            addNode(Stoptututcu.get(StopId1));//new Node(StopId1,StopName) indexlediðim node'a ulaþmak için get fonksiyonu ve durakidsini(ket) çaðýrýp nodu grafiðe ekletiyorum.

            String NeighborStopswithdistance = stp[5]; // komþularý ve uzunluklarýný temiz þekilde elde etmek için bir kaç iþlem yapýyorum.
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
                    Komsututucu.put(b1, new Node(b1, name));// elde ettiðim komþu idleri ve uzaklýklarý yine bir hashmap içinde saklýyorum.(Komþularda bir node)
                    addNode(Komsututucu.get(b1)); //new Node(b1,name)//Nodelarý mapa ekliyorum.
                    addEdge(Stoptututcu.get(StopId1), Komsututucu.get(b1), b2, "-1");//komþular arasý mesafeyi edge olarak ekliyorum.
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




        /* Belirli bir noda hangi noddan geldiðimizi tutar (hangi edge yardmý edgelerin start-stop tutmasýndan yararlanýyoruz.)
         yapýsý node,node,her bir düðümde geldiðimiz bir alt nodu bir mapde takip ediyoruz böylece paht'de detaylý görüþ kazanýyoruz.temp tutmak gibi.*/
        HashMap<Node, Node> tracker = new HashMap<>();
        Set<Edge> edgefortransfer= new LinkedHashSet<Edge>();
        tracker.put(start, null);
        // Algoritmayý çalýþtýrmak için kullandýðým yapý map yapýsý(uzaklýklarý ve nodelarý tutuyor) nodelarý buna ekleyip hesaplamalarý ve tercihleri bu yapý üzerinden yapacaðýz.
        HashMap<Node, Integer> shortestPath = new HashMap<>();

        // baþlangýcta start nodu hariç hepsini sonsuz yapýyoruz baþlangýcý ise 0 olarak ayarlýyoruz ve shortestPath'a ekliyoruz.

        for (Node node : nodes) {
            if (node == start)
                shortestPath.put(start, 0);
            else shortestPath.put(node, Integer.MAX_VALUE);
        }
        if(state==1) { //FEWER STOPS için !!!

            // Algoritma eklemeden sonra baþlýyor. Baþlangýç node'unda baðlantýsý olan tüm edgelerin üzerinden geçiyorum.
            for (Edge edge : start.nodeedges) {
                shortestPath.put(edge.Target, 1);
                tracker.put(edge.Target, start);
            }
        }

            try { //** try
        if(state==2) { //MÝNÝMUM DÝSTANCE için !!!

        // Algoritma eklemeden sonra baþlýyor. Baþlangýç node'unda baðlantýsý olan tüm edgelerin üzerinden geçiyorum.
            for (Edge edge : start.nodeedges) {
                shortestPath.put(edge.Target, edge.distance);
                tracker.put(edge.Target, start);
            }
        }

        start.visit();//ziyaret ettiðimi iþaretlemem gerekir ki biradaha karþýtýrýp üstünden geçmeyeyim.  start nodunu iþaretliyorum bir flag aslýnda.(boolean true oluyor.)

        //while döngüsü ziyaret edilmemiþ (þuanda bulunduðumuz node dan ulaþabileceðimiz.)herhangi bir edge veya noda sahipse döngü çalýþmaya devam eder.
        while (true) {
            Node currentNode = closestUnvisited(shortestPath);

            //eðer herhangi yeni bir noda ulaþamazsak ve en baþta parametre olarak verdiðimiz end node'a gelemediysek baþlangýç ve end arasýnda baðlantý yoktur.
            if (currentNode == null) {
                System.out.println(start.name + " ve " + end.name+"arasýnda bir yol bulunamamýþtýr.");
                System.out.println("Lütfen baþka bir bitiþ noktasý seçin.");
                return;
            }

            // eðer sona geldiysek end noda ulaþtýysak yolu yazdýrýrýz ve sonuç alýrýz.aðaç yapýsý gibi child -> parent iliþkisi var.
            if (currentNode == end) {
                System.out.println(start.name+ " ile " + end.name + " arasýndaki en kýsa yol :");
                int counter=0;
                int linenos = 0;
                Node child = end;
                String path = end.name;
                while (true) {

                    Node parent = tracker.get(child);           //Aktarma verilerini almak için düþündüðüm bölüm.
                    for (Edge edge:child.nodeedges){            //child -> parent gezdiðim için child ile parent arasýnda olan edgeleri bir listeye koyuyorum.
                        if(edge.Start.equals(parent)){          //listeye koymanýn yaný sýra 1 defaya mahsus olmak üzere line no alýyorum ama sonda yürüme olma ihtimali
                            edgefortransfer.add(edge);         // olmasýndan dolayý ve 2 tane arka arkaya yürüme olmayacaðýndan dolayý(olursa zaten sonuç olarak kabul etmeyeceðiz)
                            if(edgefortransfer.size()<=2 && edge.line.lineno != -1){     // garanti olmasý adýna <= lik kullanarak if bloðuna 2 tane line no alýyorum.
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

                int takeornot=0;//aktarma sayýmýn sýnýrlayýcýsý
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
                            System.out.println("Toplam gidilecek durak sayýsý:" + (edgefortransfer.size() + 1));//yürümeyide ekledim varsa,oda durak deðiþtirme.
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

            //yukarýdaki iflere girmiyor ise mevcut nodumuza edge ile baðlý  olan tüm ziyaret edilmemiþ nodelardan geçiyoruz ve mevcut node'muzdan geçen en kýsa yol
            // deðerinin daha önce sahip olduðumuzdan daha iyi(küçük) olup olmadýðýný kontrol ediyoruz.
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
