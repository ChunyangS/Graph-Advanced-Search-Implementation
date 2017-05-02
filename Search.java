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
			graph.addEdge(in.next(), in.next(), Integer.parseInt(in.next()));
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
			System.out.println(frontier.size());
			//System.out.println(current.intersection);
			//System.out.println(goalCheck(current,graph));
			if(goalCheck(current,graph)){
				outPutPathStep(current);
				return;
			}
			//System.out.println("here");
			//System.out.println(getChildren(current,graph).size());
			for(Node child: getChildren(current,graph)){
				if(child.stateCheck(frontier) && child.stateCheck(explored)){
					frontier.add(child);
					//System.out.println(frontier.size());
				}
				else if(!child.stateCheck(frontier)){
					if(child.getSameFromFrontier(frontier).depth > child.depth){
						frontier.remove(child.getSameFromFrontier(frontier));
						frontier.add(child);
					}
				}
				else if(!child.stateCheck(explored)){
					if(child.getSameFromFrontier(explored).depth > child.depth){
						explored.remove(child.getSameFromFrontier(explored));
						frontier.add(child);
					}
				}
			}
			explored.add(current);
			//System.out.println(current.intersection);
			//System.out.println(explored.size());
		}
	}
	
	private static void searchWithDfs_New(Graph graph) throws FileNotFoundException{
		Stack<Node> frontier = new Stack<Node>();
		Queue<Node> explored = new LinkedList<Node>();
		Node root = initial(graph);
		frontier.add(root);
		while(!frontier.isEmpty()){
			Node current = frontier.pop();
			System.out.println(current.intersection);
			//System.out.println(goalCheck(current,graph));
			if(goalCheck(current,graph)){
				outPutPathStep(current);
				return;
			}
			//System.out.println("here");
			//System.out.println(getChildren(current,graph).size());
			Queue<Node> children = getChildren(current,graph);
			for(Node child: reverse(toArrayList(children))){
				if(child.stateCheck(frontier) && child.stateCheck(explored)){//loop detections
					frontier.add(child);
					//System.out.println(frontier.size());
				}
				else if(!child.stateCheck(frontier)){
					if(child.getSameFromFrontier(frontier).depth > child.depth){
						frontier.remove(child.getSameFromFrontier(frontier));
						frontier.add(child);
					}
				}
				else if(!child.stateCheck(explored)){
					if(child.getSameFromFrontier(explored).depth > child.depth){
						explored.remove(child.getSameFromFrontier(explored));
						frontier.add(child);
					}
				}
			}
			explored.add(current);
			//System.out.println(current.intersection);
		}
	}
	
	public static void searchWithUcs(Graph graph) throws FileNotFoundException {
		Queue<Node> frontier = new LinkedList<Node>();
		Queue<Node> explored = new LinkedList<Node>();
		
		Node root = initial(graph);
		frontier.add(root);
		while(!frontier.isEmpty()){
			Node current = frontier.poll();
			if(goalCheck(current,graph)){
				outPutPath(current);
				return;
			}
			Queue<Node> children = getChildren(current,graph);
			while(!children.isEmpty()){
				Node child = children.poll();
				if(child.stateCheck(frontier) && child.stateCheck(explored)){
					frontier.add(child);
				}
				else if(!child.stateCheck(frontier)){
					if(child.getSameFromFrontier(frontier).gValue > child.gValue){
						frontier.remove(child.getSameFromFrontier(frontier));
						frontier.add(child);
					}
				}
				else if(!child.stateCheck(explored)){
					if(child.getSameFromFrontier(explored).gValue > child.gValue){
						explored.remove(child.getSameFromFrontier(explored));
						frontier.add(child);
					}
				}
			}
			explored.add(current);
			frontier = ucsSortFrontier(frontier);
			for(Node node :frontier){
				System.out.print(node.intersection);
			}
			System.out.println("  ");
			
		}
		
		
	}
	
	public static void searchWithAStar(Graph graph) throws FileNotFoundException{
		Queue<Node> frontier = new LinkedList<Node>();
		Queue<Node> explored = new LinkedList<Node>();
		
		Node root = initial(graph);
		frontier.add(root);
		while(!frontier.isEmpty()){
			Node current = frontier.poll();
			if(goalCheck(current,graph)){
				outPutPath(current);
				return;
			}
			Queue<Node> children = getChildren(current,graph);
			while(!children.isEmpty()){
				Node child = children.poll();
				if(child.stateCheck(frontier) && child.stateCheck(explored)){
					frontier.add(child);
				}
				else if(!child.stateCheck(frontier)){//loop detections
					if(child.getSameFromFrontier(frontier).gValue > child.gValue){
						frontier.remove(child.getSameFromFrontier(frontier));
						frontier.add(child);
					}
				}
				else if(!child.stateCheck(explored)){
					if(child.getSameFromFrontier(explored).gValue > child.gValue){
						explored.remove(child.getSameFromFrontier(explored));
						frontier.add(child);
					}
				}
			}
			explored.add(current);
			frontier = aStarSortFrontier(frontier);
		}
	}
	
	public static Queue<Node> ucsSortFrontier(Queue<Node> frontier){
		if(frontier.size() <=1){
			return frontier;
		}
		Queue<Node> left = new LinkedList<Node>();
		Queue<Node> right = new LinkedList<Node>();
		int index = 0;
		for(Node node:frontier){
			if(index%2 == 0){
				left.add(node);
				index ++;
			}
			else if(index%2 == 1){
				right.add(node);
			}
		}
		left = ucsSortFrontier(left);
		right = ucsSortFrontier(right);
		return mergeUCS(left,right,frontier);
	}
	
	public static Queue<Node> mergeUCS(Queue<Node> left,Queue<Node> right,Queue<Node> frontier){
		Queue<Node> result = new LinkedList<Node>();
		ArrayList<Node> reference = toArrayList(frontier);
		while(!left.isEmpty() && !right.isEmpty()){
			if(left.peek().gValue < right.peek().gValue){
				result.add(left.poll());
			}
			else if(left.peek().gValue == right.peek().gValue){
				if(reference.indexOf(left.peek()) < reference.indexOf(right.peek()) ){
					System.out.println(reference.indexOf(left.peek()));
					result.add(left.poll());
				}
				else if(reference.indexOf(left.peek()) > reference.indexOf(right.peek())){
					result.add(right.poll());//deep tie breaking
				}
			}
			else{
				result.add(right.poll());
			}
		}
		while(!left.isEmpty()){
			result.add(left.poll());
		}
		while(!right.isEmpty()){
			result.add(right.poll());
		}
		return result;
	}
	//sort part of merge sort that sort the frontier for A* search
	public static Queue<Node> aStarSortFrontier(Queue<Node> frontier){
		if(frontier.size() <=1){
			return frontier;
		}
		Queue<Node> left = new LinkedList<Node>();
		Queue<Node> right = new LinkedList<Node>();
		int index = 0;
		for(Node node:frontier){
			if(index%2 == 0){
				left.add(node);
				index ++;
			}
			else if(index%2 == 1){
				right.add(node);
			}
		}
		left = aStarSortFrontier(left);
		right = aStarSortFrontier(right);
		return mergeAStar(left,right,frontier);
	}
        //Implement merge sort to sort the frontier based on A* weight score, excute on nlogn	
	public static Queue<Node> mergeAStar(Queue<Node> left,Queue<Node> right,Queue<Node> frontier){
		Queue<Node> result = new LinkedList<Node>();
		ArrayList<Node> reference = toArrayList(frontier);
		while(!left.isEmpty() && !right.isEmpty()){
			if(left.peek().hueristic < right.peek().hueristic){
				result.add(left.poll());
			}
			else if(left.peek().hueristic == right.peek().hueristic){
				if(reference.indexOf(left.peek()) < reference.indexOf(right.peek()) ){
					System.out.println(reference.indexOf(left.peek()));
					result.add(left.poll());
				}
				else if(reference.indexOf(left.peek()) > reference.indexOf(right.peek())){
					result.add(right.poll());//deep tie breaking
				}
			}
			else{
				result.add(right.poll());
			}
		}
		while(!left.isEmpty()){
			result.add(left.poll());
		}
		while(!right.isEmpty()){
			result.add(right.poll());
		}
		return result;
	}
	//obtain Map that stores heristic information
	public static HashMap<String,Integer> getG(Graph graph){
		HashMap<String, Integer> gMap = new HashMap<String, Integer>();
		for(Pair sunPath: graph.vertexs){
			gMap.put(sunPath.start, sunPath.pathCost);
		}
		return gMap;
	}
	//utility to reverse
	public static ArrayList<Node> reverse(ArrayList<Node> nodes){
		for(int m = 0, n = nodes.size() - 1; m < n; m++) {
	        nodes.add(m, nodes.remove(n));
	    }
	    return nodes;
	}
	//utility to get all children level nodes
	public static Queue<Node> getChildren(Node root,Graph graph){
		Queue<Node> childNodes = new LinkedList<Node>();
		for(Pair edge: graph.edges){
			if(edge.start.equals(root.intersection)){
				childNodes.add(new Node(root,edge.end,edge.pathCost,graph));
			}
		}
		return childNodes;
	}
	//utility that change Queue to ArrayList
	public static ArrayList<Node> toArrayList(Queue<Node> nodes){
		ArrayList<Node> result = new ArrayList<Node>();
		for(Node node :nodes){
			result.add(node);
		}
		return result;
	}
	
	//Node(Node parent, String intersection,int pathCost,int depth,int gValue,Graph graph)
	public static Node initial(Graph graph){
		Node root = new Node(null,graph.origin,0,0,0,graph);
		return root;
	}
	//check if goal is find
	public static boolean goalCheck(Node current, Graph graph){
		if(current.intersection.equals(graph.destination)){
			return true;
		}
		else{
			return false;
		}
	}
	//write output file containing each step information
	public static void outPutPathStep(Node goal)throws FileNotFoundException{
		File outFile = new File("output.txt");
		PrintWriter outPut = new PrintWriter(outFile);
		Stack<Node> path = new Stack<Node>();
		while(goal != null){
			path.add(goal);
			goal = goal.parent;
		}
		//int cost = 0;
		while(!path.isEmpty()){
			Node writeCurrent = path.pop();
			//cost += writeCurrent.pathCost;
			outPut.println(writeCurrent.intersection+" "+writeCurrent.depth);
			System.out.println(writeCurrent.intersection+" "+writeCurrent.depth+" "+writeCurrent.gValue+ " "+writeCurrent.hueristic);
		}
		outPut.close();
		
	}
	//write output that contain only graph path
	public static void outPutPath(Node goal)throws FileNotFoundException{
		File outFile = new File("output.txt");
		PrintWriter outPut = new PrintWriter(outFile);
		Stack<Node> path = new Stack<Node>();
		while(goal != null){
			path.add(goal);
			goal = goal.parent;
		}
		//int cost = 0;
		while(!path.isEmpty()){
			Node writeCurrent = path.pop();
			//cost += writeCurrent.pathCost;
			outPut.println(writeCurrent.intersection+" "+writeCurrent.gValue);
			System.out.println(writeCurrent.intersection+" "+writeCurrent.gValue+ " "+writeCurrent.hueristic);
		}
		outPut.close();
		
	}
	
	//data structure that stores input graph
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
	//Basic unit data structure for search
	public static class Node{
		private String intersection;
		private int pathCost;
		private Node parent;
		private int depth;
		private int sunPathCost;
		private int gValue;
		private int hueristic;
		private LinkedList<Node> children = new LinkedList<Node>();
		
		
		public Node(Node parent, String intersection,int pathCost,Graph graph){
			this.parent = parent;
			this.intersection = intersection;
			this.pathCost = pathCost;
			this.sunPathCost = getG(graph).get(intersection);
			this.depth = this.parent.depth +1;
			this.gValue = this.parent.gValue + this.pathCost;
			this.hueristic = this.gValue + this.sunPathCost;
		}
		
		
		public Node(Node parent, String intersection,int pathCost,int depth,int gValue,Graph graph){
			this.parent = parent;
			this.intersection = intersection;
			this.pathCost = pathCost;
			this.sunPathCost = getG(graph).get(intersection);
			this.depth = depth;
			this.gValue = gValue;
			this.hueristic = this.gValue + this.sunPathCost;
		}
		
		public boolean stateCheck(Collection<Node> nodes){
			boolean check = true;
			for(Node node:nodes){
				if(this.intersection.equals(node.intersection)){
					check = false;
				}
			}
			return check;
		}
		
		
		
		public Node getSameFromFrontier(Collection<Node> nodes){
			Node same = null;
			for(Node node:nodes){
				if(this.intersection.equals(node.intersection)){
					same = node;
				}
			}
			return same;
			
			
		}
		
		public void setChildren(Node child){
			children.add(child);
		}
		
		public void setParent(Node parent){
			this.parent = parent;
		}
		
		public Node compareByG(Node node2){
			if(this.gValue > node2.gValue) {
				return this;
			}
			else if(this.gValue < node2.gValue) {
				return node2;
			}
			else{
				return this;
			}
		}
		
		public Node compareByA(Node node2){
			if(this.gValue + this.sunPathCost > node2.gValue + node2.sunPathCost) {
				return this;
			}
			else if(this.gValue + this.sunPathCost < node2.gValue + node2.sunPathCost){
				return node2;
			}
			else{
				return this; // always put the previous existed node in first when compare
			}				// thus when there is a tie the one already in will remain first
		}
		
			
	}

}
