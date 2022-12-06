package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import data.Movie;
import data.Reviewer;


/**
 * This class contains methods for reading and parsing the MovieLens files. You can alter
 * the number of movies in movies.csv to change the size of the data set. Only the
 * ratings for movies in movies.csv are retained. 
 * 
 * All movie ids are renumbered to be sequential from 1...M whereas user ids are not renumbered
 * 
 * @author alchambers
 * @version 2018
 *
 */
public class DataLoader {
	private Map<Integer, Movie> movies;
	private Map<Integer, Reviewer> reviewers;	
	private Map<Integer, Integer> renumber;

	/**
	 * Constructor 
	 */
	public DataLoader(){
		reviewers = new HashMap<>();
		movies = new HashMap<>();
		renumber = new HashMap<>();
	}

	/**
	 * Loads the movie title and ratings data
	 * @param movieFilename The filename for the movie titles file
	 * @param reviewFilename The filename for the ratings file
	 */
	public void loadData(String movieFilename, String reviewFilename){
		// These files *must* be read in this order
		readMovieTitlesFile(movieFilename);		
		readRatings(reviewFilename);
	}


	/**
	 * Prints the list of reviewers 
	 */
	public void printReviewerList(){
		System.out.println("Total size: " + reviewers.size());		
		for( Map.Entry<Integer, Reviewer> entry : reviewers.entrySet()){
			System.out.println(entry.getValue());
		}
	}

	/**
	 * Prints the list of movies 
	 */	
	public void printMovieList(){
		System.out.println("Total size: " + movies.size());		
		for( Map.Entry<Integer, Movie> entry : movies.entrySet()){
			System.out.println(entry.getValue());
		}
	}

	/**
	 * Returns the list of reviewers
	 */
	public Map<Integer, Reviewer> getReviewers(){
		return reviewers;
	}

	/**
	 * Returns the list of movies
	 */
	public Map<Integer, Movie> getMovies(){				
		return movies;
	}



	/**************************************************************
	 * 				Private Helper Methods
	 **************************************************************/


	/**
	 * Reads and parses the ratings data 
	 */
	private void readRatings(String filename){
		try{
			BufferedReader input = new BufferedReader(new FileReader(filename));
			input.readLine(); //read the header line
			String line = input.readLine();						
			while(line != null) {
				String[] fields = line.split(",");
				assert(fields.length == 4);

				int userId = -1, movieId = -1;
				double rating = -1;
				try {
					userId = Integer.parseInt(fields[0]);
					movieId = Integer.parseInt(fields[1]);					
					rating = Double.parseDouble(fields[2]);					
				}
				catch(NumberFormatException e){
					System.out.println(line);
					System.out.println(e);
				}

				// Only care about the movies that were in our movie title file
				if(renumber.containsKey(movieId)) {
					movieId = renumber.get(movieId);
					// A new reviewer
					if(!reviewers.containsKey(userId)) {
						Reviewer r = new Reviewer(userId);
						r.addMovie(movieId, rating);
						reviewers.put(userId, r);					
					}				
					// An existing reviewer
					else {
						reviewers.get(userId).addMovie(movieId, rating);
					}
					movies.get(movieId).addRating(userId, rating);
				}
				line = input.readLine();
			}
			input.close();
		}
		catch(IOException e){
			System.out.println(System.getProperty("user.dir"));
			System.out.println(e);
		}
	}

	/**
	 * Reads and parses the data from movie_titles.txt
	 */
	private void readMovieTitlesFile(String filename){
		try{
			int id = 1;
			BufferedReader input = new BufferedReader(new FileReader(filename));
			input.readLine(); // read the header row

			String line = input.readLine();
			while(line != null){
				String title = "", genres = "";
				int year = -1, movieId = -1;

				int firstComma = line.indexOf(",");
				int lastComma = line.lastIndexOf(",");	

				try {
					movieId = Integer.parseInt(line.substring(0, firstComma));
					genres = line.substring(lastComma+1);				
					title = line.substring(firstComma+1, lastComma);
					year = Integer.parseInt(getYear(title));
					title = title.replaceAll("\"", "");
					title = title.replaceAll("\\(.+?\\)",	"");
					title = title.trim();
				}
				catch(NumberFormatException e){
					System.out.println(line);
					System.out.println(e);
				}

				renumber.put(movieId, id); // maps from external to internal id
				Movie m = new Movie(id, year, title);				
				String[] genre_list = genres.split("\\|");
				for(String genre : genre_list) {
					m.addGenre(genre);
				}
				
				movies.put(id, m);
				id++;
				line = input.readLine();
			}
			input.close();
		}
		catch(IOException e){
			System.out.println(e);
		}
	}

	private String getYear(String input) {
		String yearRegEx = "(\\d\\d\\d\\d)";
		Pattern p = Pattern.compile(yearRegEx);
		Matcher matcher = p.matcher(input);
		String year = "-1";
		if(matcher.find()) {
			year = matcher.group(0);
		}
		return year;
	}



	public static void main(String[] args){
		DataLoader p = new DataLoader();
		p.loadData("./src/ml-latest-small/movies_top_1000.csv", "./src/ml-latest-small/ratings.csv");
		System.out.println("Number of movies: " + p.getMovies().size());
		System.out.println("Number of reviewers: " + p.getReviewers().size());
		p.printMovieList();
		p.printReviewerList();
	}

}
