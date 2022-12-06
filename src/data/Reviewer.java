package data;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * This class contains information for a single Netflix movie reviewer
 * including the reviewer id and a list of the movies (and ratings)
 * reviewed
 * 
 * @author alchambers
 *
 */
public class Reviewer {
	private int reviewerId;

	// The key is the movie id and the value is the rating
	// given by the user
	private Map<Integer, Double> listByMovieId;


	/**
	 * Create a new reviewer with given id 
	 */
	public Reviewer(int id){
		reviewerId = id;
		listByMovieId = new HashMap<>();		
	}	

	/**
	 * Add a movie to the reviewer's list of rated movies
	 */
	public void addMovie(int movieId, double rating){
		listByMovieId.put(movieId, rating);
	}


	/**
	 * Get a list of all movies the reviewer rated, regardless
	 * of the rating
	 */
	public Map<Integer, Double> getRatings(){
		return  listByMovieId;
	}

	/**
	 * Returns the rating for a movie or -1
	 * if the reviewer never rated the movie
	 */
	public double getMovieRating(int movieId){
		if(!listByMovieId.containsKey(movieId)){
			return -1;
		}
		return listByMovieId.get(movieId);
	}

	/**
	 * Returns the reviewer's unique id
	 */
	public int getReviewerId(){
		return reviewerId;
	}

	/**
	 * Returns true if reviewer rated the movie and false
	 * otherwise
	 */
	public boolean ratedMovie(int movieId){		
		return listByMovieId.containsKey(movieId);
	}

	/**
	 * Returns the number of movies rated by the reviewer
	 */
	public int numRated(){
		return listByMovieId.size();
	}
	
	/**
	 * Returns a hash value for the reviewer	
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + reviewerId;
		return result;
	}

	/**
	 * Returns true if the object is equal to the reviewer, false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reviewer other = (Reviewer) obj;
		if (reviewerId != other.reviewerId)
			return false;
		return true;
	}

	/**
	 * Returns a string representation of all movies rated
	 * by the reviewer
	 */
	public String toString(){
		String str = "User: " + reviewerId + "\n";					
		for(Map.Entry<Integer, Double> entry : listByMovieId.entrySet()){
			int movieId = entry.getKey();
			double rating = entry.getValue();
			str += "\tmovie=" + movieId + " rating=" + rating + "\n";
		}		
		return str;
	}
}
