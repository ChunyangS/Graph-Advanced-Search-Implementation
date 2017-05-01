import java.util.*;
import java.io.*;


public class Search {
	public static void main(String[] args) throws FileNotFoundException{
		File input = new File("input.txt");
		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(new FileInputStream(input))));
		String searchMethod = in.next();
		String origin = in.next();
		String destination = in.next();
		int numOfActiveLines = in.nextInt();
		
		
		Graph graph = new Graph(origin,destination);
		
		for(int i = 0; i < numOfActiveLines; i++){
			graph.addEdge(in.next(), in.next(), in.nextInt());
		}
		
		int numOfIntersection = in.nextInt();
		
		for(int i = 0;i < numOfIntersection; i++){
			graph.addVertex(in.next(),in.nextInt());
		}
		in.close();
		
		
		//graph.printGraph(); //debug for integrity of graph reading process.
		
		if(searchMethod.equals("BFS")){
			searchWithBfs_New(graph);
		}
		else if(searchMethod.equals("DFS")){
			searchWithDfs_New(graph);
		}
		else if(searchMethod.equals("UCS")){
			searchWithUcs(graph);
		}
		else if(searchMethod.equals("A*")){
			searchWithAStar(graph);
		}
	}
	
	
	
	private static void searchWithBfs_New(Graph graph) throws FileNotFoundException{
		Queue<Node> frontier = new LinkedList<Node>();
		Queue<Node> explored = new LinkedList<Node>();
		Node root = initial(graph);
		frontier.add(root);
		while(!frontier.isEmpty()){
			Node current = frontier.poll();
			//System.out.println(current.intersection);
			//System.out.println(goalCheck(current,graph));
			if(goalCheck(current,graph)){
				outPutPath(current);
				return;
			}
			explored.add(current);
			//System.out.println("here");
			//System.out.println(getChildren(current,graph).size());
			for(Node child: getChildren(current,graph)){
				frontier.add(child);
				//System.out.println(frontier.size());
			}	
		}
	}
	
	private static void searchWithDfs_New(Graph graph) throws FileNotFoundException{
		Stack<Node> frontier = new Stack<Node>();
		Queue<Node> explored = new LinkedList<Node>();
		Node root = initial(graph);
		frontier.add(root);
		while(!frontier.isEmpty()){
			Node current = frontier.pop();
			//System.out.println(current.intersection);
			//System.out.println(goalCheck(current,graph));
			if(goalCheck(current,graph)){
				outPutPath(current);
				return;
			}
			explored.add(current);
			//System.out.println("here");
			//System.out.println(getChildren(current,graph).size());
			for(Node child: getChildren(current,graph)){
				frontier.add(child);
				//System.out.println(frontier.size());
			}	
		}
	}
	
	public static void searchWithUcs(Graph graph){
		Queue queue = new LinkedList<Node>();
		
	}
	
	public static void searchWithAStar(Graph graph){
		
	
	}
	
	public static HashMap<String,Integer> getG(Graph graph){
		HashMap<String, Integer> gMap = new HashMap<String, Integer>();
		for(Pair sunPath: graph.vertexs){
			gMap.put(sunPath.start, sunPath.pathCost);
		}
		return gMap;
	}
	
	public static ArrayList<Node> getChildren(Node root,Graph graph){
		ArrayList<Node> childNodes = new ArrayList<Node>();
		for(Pair edge: graph.edges){
			if(edge.start.equals(root.intersection)){
				childNodes.add(new Node(root,edge.end,edge.pathCost,graph));
			}
		}
		return childNodes;
	}
	
	public static Node initial(Graph graph){
		Node root = new Node(null,graph.origin,0,graph);
		return root;
	}
	
	public static boolean goalCheck(Node current, Graph graph){
		if(current.intersection.equals(graph.destination)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static void outPutPath(Node goal)throws FileNotFoundException{
		File outFile = new File("ouput.txt");
		PrintWriter outPut = new PrintWriter(outFile);
		Stack<Node> path = new Stack<Node>();
		while(goal != null){
			path.add(goal);
			goal = goal.parent;
		}
		int cost = 0;
		while(!path.isEmpty()){
			Node writeCurrent = path.pop();
			cost += writeCurrent.pathCost;
			outPut.println(writeCurrent.intersection+" "+cost);
			//System.out.println(writeCurrent.intersection+" "+cost);
		}
		outPut.close();
		
	}
	
	public static class Graph{
		private String origin;
		private String destination;
		private ArrayList<Pair> edges = new ArrayList<Pair>();
		private ArrayList<Pair> vertexs =new ArrayList<Pair>();
		
		public Graph(String origin,String destination){	
			this.origin = origin;
			this.destination = destination;
			this.edges = new ArrayList<Pair>();
			this.vertexs = new ArrayList<Pair>();
		}
		
		public void addEdge(String start,String end,int pathCost){
			Pair pair = new Pair(start,end,pathCost);
			this.edges.add(pair);
		}
		public void addVertex(String vertex,int sunCost){
			Pair pair = new Pair(vertex, null,sunCost);
			this.vertexs.add(pair);
		}
		public void printGraph(){
			System.out.println(origin);
			System.out.println(destination);
			for(Pair pair : edges){
				System.out.println(pair.toString());
			}
			for(Pair vertex: vertexs){
				System.out.println(vertex.toString());
			}
		}
	}
	
	public static class Pair{
		private String start;
		private String end;
		private int pathCost;
		
		public Pair(String start,String end,int pathCost){
			this.start = start;
			this.end = end;
			this.pathCost = pathCost;
		}
		
		public String toString(){
			return start+" "+end+" "+Integer.toString(pathCost);
		}
		
		
	}
	
	public static class Node{
		private String intersection;
		private int pathCost;
		private Node parent;
		private int depth;
		private int sunPathCost;
		private LinkedList<Node> children = new LinkedList<Node>();
		
		
		public Node(Node parent, String intersection,int pathCost,Graph graph){
			this.parent = parent;
			this.intersection = intersection;
			this.pathCost = pathCost;
			this.sunPathCost = getG(graph).get(intersection);
		}
		
		
		public Node(String intersection, int sunPathCost){
			this.intersection = intersection;
			this.sunPathCost = sunPathCost;
		}
		
		public void setChildren(Node child){
			children.add(child);
		}
		
		public void setParent(Node parent){
			this.parent = parent;
		}
		
			
	}

}

