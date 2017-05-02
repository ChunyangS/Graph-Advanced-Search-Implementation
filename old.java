/*private static void searchWithBfs(Graph graph) throws FileNotFoundException{
		
		Queue<Node> frontier = new LinkedList<Node>();
		Queue<Node> explored = new LinkedList<Node>();
		Stack<Node> path = new Stack<Node>();
		int cost = 0;
		File outFile = new File("output.txt");
		PrintWriter outPut = new PrintWriter(outFile);
		
		if(graph.edges.size() < 1) return;
		Node root = new Node(null,graph.origin,0);
		//check if initial node is des or not
		if(root.intersection.equals(graph.destination)){
			
			path.add(root);
			while(root.parent != null){
				root = root.parent;
				path.add(root);
			}
			for(Node traverse : path){
				System.out.println(traverse.intersection + " ");
				outPut.println(traverse.intersection + " " );
			}

			outPut.close();
			return;
		}
		
		
		int edgePointer = 0;
		frontier.add(root);
		while(edgePointer < graph.edges.size()-1){
			
			root = frontier.poll();
			explored.add(root);
			//System.out.println(root.intersection);
			
			//System.out.println(root.intersection + " " + cost);
			//outPut.println(root.intersection + " " + cost);
			
			
			
				while(graph.edges.get(edgePointer).start.equals(root.intersection)){
					Node child = new Node(root,graph.edges.get(edgePointer).end,graph.edges.get(edgePointer).pathCost);
					if(child.intersection.equals(graph.destination)){
						path.add(child);
						explored.add(child);
						
						while(child.parent != null){
							child = child.parent;
							path.add(child);
						}
					
						while(path.size() != 0){
							Node trace = path.pop();
							cost += trace.pathCost;
							System.out.println(trace.intersection + " " + cost);
							outPut.println(trace.intersection + " " + cost);
						}

						outPut.close();
						return;
					}
					else{
						root.setChildren(child);
						frontier.add(child);
						edgePointer++;
					}
				}
				
			
		}
		//File outFile = new File("output.txt");
		//PrintWriter outPut = new PrintWriter(outFile);
		
		
		
		
		
	}
	
	public static void searchWithDfs(Graph graph) throws FileNotFoundException{
		Stack<Node> frontier = new Stack<Node>();
		Queue<Node> explored = new LinkedList<Node>();
		Stack<Node> path = new Stack<Node>();
		
		File outFile = new File("ouput.txt");
		PrintWriter outPut = new PrintWriter(outFile);
		int cost = 0;
		
		Node root = new Node(null,graph.origin,0);
		if(graph.origin.equals(graph.destination)){
			outPut.print(graph.origin+" "+0);
			return;
		}
		frontier.add(root);
		int edgePointer = 0;
		while(edgePointer < graph.edges.size()-1){
			root = frontier.pop();
			explored.add(root);
			while(graph.edges.get(edgePointer).start == root.intersection){
				Node child = new Node(root,graph.edges.get(edgePointer).end,graph.edges.get(edgePointer).pathCost);
				frontier.add(child);
				edgePointer++;
			}
		}
		
		
	}*/
