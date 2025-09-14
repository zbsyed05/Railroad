/* Zainab Syed
 * COP3503 Spring 2025
 * Programming Assignment 5
 */
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

class DisjointSet
{
	int[] rank;
	int[] parent;
	int n;
	
	public DisjointSet(int n)
	{
		rank = new int[n];
		parent = new int[n];
		this.n = n;
		makeSet();
	}
	
	// creates n sets with single item in each
	public void makeSet()
	{
		for (int i = 0; i < n; i++) 
		{
			parent[i] = i;
		}
	}
	
	// path compression
	public int find(int x)
	{
		if (parent[x] != x)
		{
			parent[x] = find(parent[x]);
		}
		return parent[x];
	}
	
	// Union by rank
	public void union(int x, int y)
	{
		int xRoot = find(x), yRoot = find(y);
		
		if(xRoot == yRoot)
			return;
		
		if (rank[xRoot] < rank[yRoot])
			parent[xRoot] = yRoot;
		
		else if (rank[yRoot] < rank[xRoot])
			parent[yRoot] = xRoot;
		
		else
		{
			parent[yRoot] = xRoot;
			rank[xRoot] = rank[xRoot] + 1;
		}
	}
}

class Edge implements Comparable<Edge>
{
	String src;
	String dest;
	int weight;
	
	// initialize edge information
	public Edge(String source, String destination, int weight) 
	{
		this.src = source;
		this.dest = destination;
		this.weight = weight;
	}
	
	// compares the costs
	@Override
	public int compareTo(Edge other) 
	{
		return this.weight - other.weight;
	}
}

public class Railroad 
{	
	private Edge[] tracks;
	
	Railroad (int numTracks, String fileName) 
	{
		
		Edge[] tracks = new Edge[numTracks];		
		File input = new File(fileName);
		
		// extract information from file
		try(Scanner sc = new Scanner(input)) 
		{			
			for (int i = 0; i < numTracks; i++) 
			{				
				String src = sc.next();
				String dest = sc.next();
				int cost = sc.nextInt();
				
				// save edge info
				Edge edge = new Edge(src, dest, cost); 		
				tracks[i] = edge;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// sets up global variable
		this.tracks = tracks;
	}
	
	public String buildRailroad() 
	{
		Arrays.sort(tracks);	
		
		// put cities into hash maps
		HashMap<String, Integer> cityToIdx = new HashMap<>();		
		for(Edge e : tracks) 
		{
			cityToIdx.putIfAbsent(e.src, cityToIdx.size());
			cityToIdx.putIfAbsent(e.dest, cityToIdx.size());			
		}
		
		int numCities = cityToIdx.size();
		DisjointSet ds = new DisjointSet(numCities); // initialize disjoint set
		Edge[] result = new Edge[numCities];
		
		int count = 0;
		for(Edge e : tracks) 
		{
			int srcIdx = cityToIdx.get(e.src);
			int destIdx = cityToIdx.get(e.dest);
			
			// merge sets, create an edge
			if(ds.find(srcIdx) != ds.find(destIdx)) 
			{
				ds.union(srcIdx, destIdx);
				result[count++] = e;
			}

		}	
		
		// print results lexicographically 
		int total = 0;
		for(int i = 0; i < count; i++) 
		{
			Edge e = result[i];
			String city1 = e.src;
			String city2 = e.dest;
			if(city1.compareTo(city2) > 0) 
			{
				String temp = city1;
				city1 = city2;
				city2 = temp;
			}
			System.out.println(city1 + "---" + city2 + "\t$" + e.weight);
			total += e.weight;
		}
		
		return "The cost of the railroad is $" + total + ".";
	}

}