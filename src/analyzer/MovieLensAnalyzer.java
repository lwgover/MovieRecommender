package analyzer;

import data.Movie;
import data.Reviewer;
import graph.Graph;
import graph.GraphAlgorithms;
import graph.GraphIfc;
import graph.WeightedGraph;
import util.DataLoader;

import java.util.*;

/**
 * MovieLensAnalyser program
 * Extra Credit - Weighted Graph and movie recommender program for the weighted graph.
 *
 * @author Lucas Gover
 * @version 12/10/2021
 *
 */
public class MovieLensAnalyzer {
	
	public static void main(String[] args){
		// Your program should take two command-line arguments: 
		// 1. A ratings file
		// 2. A movies file with information on each movie e.g. the title and genres		
		if(args.length != 2){
			System.err.println("Usage: java MovieLensAnalyzer [ratings_file] [movie_title_file]");
			System.exit(-1);
		}
		DataLoader loader = new DataLoader();
		System.out.println("========== Welcome to MovieLens Analyser ==========");
		System.out.println("The files being analysed are:");
		System.out.println(args[0]);
		System.out.println(args[1]);
		loader.loadData(args[1],args[0]);
		Map<Integer, Movie> movies = loader.getMovies(); // Stores Movies
		Map<Integer, Reviewer> users = loader.getReviewers(); // Stores Users
		Scanner input = new Scanner(System.in);
		GraphIfc<Integer> g = new Graph<Integer>();
		int typeOfGraph = 0;
		boolean numsWrong = true;
		//Print out types of Graphs to Create
		while (numsWrong) {
			System.out.println("\n[Option 1] u and v are adjacent if the same 12 users gave the same rating to both movies");
			System.out.println("[Option 2] u and v are adjacent if the same 12 users watched both movies (regardless of rating)");
			System.out.println("[Option 3] u and v are adjacent they share at least one genre");
			System.out.println("[Option 4] u and v have a weighted edge that is larger the more different the movies are");
			System.out.print("Chose an option to build the Graph(1-4): ");
			typeOfGraph  = input.nextInt();
			if (typeOfGraph > 0 && 4 >= typeOfGraph) {
				numsWrong = false;
			} else {
				System.out.println("\u001B[31m" + "Please enter a number between 1 and 4" + "\u001B[0m");
			}
		}
		//Makes Graphs based on inputs
        switch (typeOfGraph) {
            case 1 ->
                    g = makeUnweightedMovieGraph1(movies, users);
			case 2 ->
					g = makeUnweightedMovieGraph2(movies, users);
			case 3 ->
					g = makeUnweightedMovieGraph3(movies, users);
			case 4 ->
					g = makeWeightedMovieGraph(movies, users);
				//Do Stuff For Option 2
		}
		//There are 3 choices for defining adjacency:
		//[Option 1] u and v are adjacent if the same 12 users gave the same ratings to both movies
		//[Option 2] u and v are adjacent if the same 12 users watched both movies(regardless of rating)
		//[Option 3] u is adjacent to v if at least 33% of the users that rated u gave v the same rating
		//You need to have 2 ways of analysing the data, for how u and v are adjacent
		System.out.println();
		boolean repeat = true;
		while(repeat) {
			System.out.println("\n[Option 1] Print out statistics about the graph");
			System.out.println("[Option 2] Print node information");
			System.out.println("[Option 3] Display the shortest path between nodes");
			System.out.println("[Option 4] Sort Movies by most similar to your movie(Works best with a weighted Graph)");
			System.out.println("[Option 5] Quit");
			System.out.print("Chose an option(1-4): ");
			switch (input.nextInt()) {
				//[Option 1] Print out statistics about the graph
				case 1 -> {
					System.out.println("|V| = " + g.numVertices() + " vertices");
					System.out.println("|E| = " + g.numEdges() + " edges");
					System.out.println("Density = " + (((double)g.numEdges()) / ((double)((g.numVertices()) * ((g.numVertices())-1)))));
					System.out.println("Max. degree = " + GraphAlgorithms.maxDegree(g));
					int[] diameter = GraphAlgorithms.longestShortestPath(g);
					System.out.println("Diameter = " + diameter[0] + " (" + (diameter[1]+1) + " to " + (diameter[2]+1) + ")");
					System.out.println("Avg. path length = " + GraphAlgorithms.averagePathLength(g));
				}
				//[Option 2] Print node information
				case 2 -> {
					numsWrong = true;
					int node =0;
					while (numsWrong) {
						System.out.print("Enter movie id (1-1000):");
						node = input.nextInt();
						if (node > 0 && 1000 >= node) {
							numsWrong = false;
						} else {
							System.out.println("\u001B[31m" + "Please enter a number between 1 and 1000" + "\u001B[0m");
						}
					}
					System.out.println(movies.get(node).toString());
					System.out.println("Neighbors:");
					Collection<Integer> neighbors = g.getNeighbors(node - 1);
					for (int neighbor : neighbors) {
						System.out.println("\t" + movies.get(neighbor + 1).getTitle());
					}
				}
				//[Option 3] Display the shortest path between nodes
				case 3 -> {
					numsWrong = true;
					int startingNode = 0;
					int endingNode = 0;
					while (numsWrong) {
						System.out.print("Enter starting node (1-1000):");
						startingNode = input.nextInt();
						if (startingNode > 0 && 1000 >= startingNode) {
							numsWrong = false;
						} else {
							System.out.println("\u001B[31m" + "Please enter a number between 1 and 1000" + "\u001B[0m");
						}
					}
					numsWrong = true;
					while (numsWrong) {
						System.out.print("Enter ending node (1-1000):");
						endingNode = input.nextInt();
						if (endingNode > 0 && 1000 >= endingNode) {
							numsWrong = false;
						} else {
							System.out.println("\u001B[31m" + "Please enter a number between 1 and 1000" + "\u001B[0m");
						}
					}
					int[] path = GraphAlgorithms.dijkstrasAlgorithm(g, startingNode - 1);
					try {
						path = prevsToPath(path, startingNode - 1, endingNode - 1);
						for (int i = 0; i < path.length - 1; i++) {
							System.out.print("("+(path[i]+1) + ") "+movies.get(path[i] + 1).getTitle() + " ==> ");
						}
						System.out.println("("+(endingNode) + ") "+ movies.get(endingNode).getTitle());
					} catch (InternalError e) {
						System.out.println("Cannot Get from " + movies.get(startingNode).getTitle() + " to " + movies.get(endingNode).getTitle());
					}
				}
				//[Option 4] Sort Movies by most similar to your movie
				case 4 -> {
					System.out.print("Enter movie id (1-1000):");
					int node = input.nextInt();
					System.out.println(movies.get(node).toString());
					int[] mostSimilar = mostSimilar(g,node-1);
					for(int i = 2; i <= (mostSimilar.length); i++){
						System.out.println((i-1) + ": " + movies.get(mostSimilar[i-1]+1).getTitle());
					}
				}
				//[Option 5] Quit
				case 5 -> {
					repeat = false;
					System.out.println("Exiting...bye");
				}
				default -> System.out.println("\u001B[31m" + "Please enter a number between 1 and 4" + "\u001B[0m");
			}
		}

	}
	/**
	 * Constructs a graph in which if 12 users both watched the movie u and the movie v there's an edge between u and v
	 *
	 * @param prevs list of the previous nodes on a path from starting node to ending node
	 * @param startingNode start node of the path
	 * @param endingNode end node of the path
	 * @return returns the list of nodes in order from startingNode to endingNode
	 */
	private static int[] prevsToPath(int[] prevs, int startingNode,int endingNode){
		int curr = endingNode;
		LinkedList<Integer> path = new LinkedList<>();
		while(curr != startingNode){
			if(curr <= -1){
				throw new InternalError();
			}
			path.addFirst(curr);
			curr = prevs[curr];
		}
		path.addFirst(curr);
		int[] temp = new int[path.size()];
		for(int i = 0; i < temp.length; i++){
			temp[i] = path.get(i);
		}
		return temp;
	}
	/**
	 * Constructs a graph in which if 12 users both watched and gave the same rating to the movie u and the movie v there's an edge between u and v
	 *
	 * @param movies List of movies for the nodes in the Graph
	 * @param users List of reviewers to construct the edges in the graph from
	 * @return returns a graph of movies
	 */
	private static Graph<Integer> makeUnweightedMovieGraph1(Map<Integer, Movie> movies, Map<Integer, Reviewer> users){
		Graph<Integer> g = new Graph<Integer>();
		for(int i = 0; i < movies.size(); i++){
			g.addVertex(i);
		}

		for(int i = 0; i < movies.size(); i++){
			for(int j = 0; j < movies.size(); j++){
				if(i != j){
					int counter = 0;
					for(int k = 1; k <= users.size(); k++){
						if(users.get(k).ratedMovie(i+1) && users.get(k).ratedMovie(j+1) && users.get(k).getMovieRating(i+1) == users.get(k).getMovieRating(j+1)){
							counter++;
						}
					}
					if(counter >= 12){
						g.addEdge(i,j);
					}
				}
			}
		}
		return g;
	}
	/**
	 * Constructs a graph in which if 12 users both watched the movie u and the movie v there's an edge between u and v
	 *
	 * @param movies List of movies for the nodes in the Graph
	 * @param users List of reviewers to construct the edges in the graph from
	 * @return returns a graph of movies
	 */
	private static Graph<Integer> makeUnweightedMovieGraph2(Map<Integer, Movie> movies, Map<Integer, Reviewer> users){
		Graph<Integer> g = new Graph<Integer>();
		for(int i = 0; i < movies.size(); i++){
			g.addVertex(i);
		}
		for(int i = 0; i < movies.size(); i++){
			for(int j = 0; j < movies.size(); j++){
				if(i != j){
					int counter = 0;
					for(int k = 1; k <= users.size(); k++){
						//System.out.println("Looking at " + k + " and found " + users.get(k));
						if(users.get(k).ratedMovie(i+1) && users.get(k).ratedMovie(j+1)){
							counter++;
						}
					}
					if(counter >= 12){
						g.addEdge(i,j);
					}
				}
			}
		}
		return g;
	}
	/**
	 * Constructs a graph in which if movies u and v both share at least one genre there's an edge between them
	 *
	 * @param movies List of movies for the nodes in the Graph
	 * @param users List of reviewers to construct the edges in the graph from
	 * @return returns a graph of movies
	 */
	private static GraphIfc<Integer> makeUnweightedMovieGraph3(Map<Integer, Movie> movies, Map<Integer, Reviewer> users){
		Graph<Integer> g = new Graph<Integer>();
		for(int i = 0; i < movies.size(); i++){
			g.addVertex(i);
		}
		for(int i = 0; i < movies.size(); i++){
			for(int j = 0; j < movies.size(); j++){
				if(i != j){
					for(String genre:movies.get(i+1).getGenres()){
						//System.out.println("Looking at " + k + " and found " + users.get(k));
						if(movies.get(j+1).getGenres().contains(genre)){
							g.addEdge(i,j);
							break;
						}
					}
				}
			}
		}
		return g;
	}
	/**
	 * Constructs a weighted connected graph of movies in which the edge weight is given by
	 * ((100 * (genre differences / totalGenres)) + (100 * (ratings differences / totalRatings))^2
	 *
	 * @param movies List of movies for the nodes in the Graph
	 * @param users List of reviewers to construct the edges in the graph from
	 * @return returns a graph of movies
	 */
	private static GraphIfc<Integer> makeWeightedMovieGraph(Map<Integer, Movie> movies, Map<Integer, Reviewer> users){
		WeightedGraph<Integer> g = new WeightedGraph<Integer>();
		for(int i = 0; i < movies.size(); i++){
			g.addVertex(i);
		}
		for(int i = 0; i < movies.size(); i++){
			for(int j = 0; j < movies.size(); j++){
				if(i != j){
					double genreDiffs = 0;
					double totalGenres = 0;
					double ratingsDiffs = 0;
					double totalRatings = 0;
					for(String genre:movies.get(i+1).getGenres()){
						if(!movies.get(j+1).getGenres().contains(genre)){
							genreDiffs++;
						}
						totalGenres++;
					}
					for(int k = 1; k <= users.size(); k++){
						if(users.get(k).ratedMovie(i+1) && users.get(k).ratedMovie(j+1)){
							ratingsDiffs += Math.abs(users.get(k).getMovieRating(i+1) - users.get(k).getMovieRating(j+1)) / 4;
						}
						totalRatings++;
					}
					g.addEdge(i,j,(Math.pow((100 * (genreDiffs / totalGenres)) + (100 * (ratingsDiffs / totalRatings)),2)));
				}
			}
		}
		return g;
	}
	/**
	 * Takes a graph of movies and a movie and ranks how similar all other movies are to your movie
	 *
	 * @param g Graph of Integers
	 * @param movie to find similar movies to
	 * @return returns a ranked list of the most similar movies to yours
	 */
	private static int[] mostSimilar(GraphIfc<Integer> g, int movie){
		int[] movies = GraphAlgorithms.floydWarshall(g)[movie];
		Pair<Integer,Double>[] pairs = new Pair[movies.length];
		for(int i = 0; i < movies.length; i++){
			pairs[i] = new Pair<Integer,Double>(i,movies[i]);
		}
		Arrays.sort(pairs);
		int[] temp = new int[movies.length];
		for(int i = 0; i < temp.length; i++){
			temp[i] = pairs[i].value;
		}
		return temp;
	}
}
