package data;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * This class represents a single movie.
 * @author alchambers
 */
public class Movie {
	private int year;
	private int movieId;
	private String title;	
	private Map<Integer, Double> ratings;
	private Set<String> genres;	
	
	/**
	 * Constructs a new movie with the given information
	 * @param theId Movie id
	 * @param theYear Movie year
	 * @param theTitle Movie title
	 */
	public Movie(int theId, int theYear, String theTitle){
		year = theYear;
		movieId = theId;
		title = theTitle;
		ratings = new HashMap<>();
		genres = new HashSet<>();
	}
	
	/**
	 * Records a rating for the movie
	 * @param userId The id of the user rating the movie
	 * @param rating The rating given by the uesr
	 */
	public void addRating(int userId, double rating){
		if(userId < 0 || rating < 0){
			throw new AssertionError("Inputs must be positive.");
		}
		ratings.put(userId, rating);
	}
	
	public void addGenre(String genre) {
		genres.add(genre);
	}
	
	public Set<String> getGenres() {
		return genres;
	}
	
	/**
	 * Checks if the user has rated the movie
	 * @param userId The id of the user
	 * @return true if the user rated the movie, false otherwise
	 */
	public boolean rated(int userId){
		return ratings.containsKey(userId);
	}
		
	/**
	 * Returns a user's rating of the movie  
	 * @param userId The id of the user
	 * @return The user's rating or -1 if the user has not rated the movie
	 */
	public double getRating(int userId){
		Double rating = ratings.get(userId);
		if(rating == null){
			return -1;
		}
		return rating;
	}
	
	/**
	 * Returns the number of ratings for the movie
	 * @return The number of user's who have rated the movie
	 */
	public int numRatings(){
		return ratings.size();
	}
	
	/**
	 * Returns a map view of the ratings
	 * @return A map from the rating to the frequency of that rating 
	 */
	public Map<Integer, Double> getRatings(){
		return ratings;
	}
	
	/**
	 * Set the year of the movie
	 * @param year The year the movie was released
	 */
	public void setYear(int year){
		this.year = year;
	}
	
	/**
	 * Set the title of the movie
	 * @param title The title of the movie
	 */
	public void setTitle(String title){
		this.title = title;
	}
	
	/**
	 * Returns the year of the movie
	 * @return The year the movie was released
	 */
	public int getYear(){
		return year;
	}
	
	/**
	 * Returns the movie id
	 * @return The movie id
	 */
	public int getMovieId(){
		return movieId;
	}
	
	/**
	 * Returns the movie title
	 * @return The movie title
	 */
	public String getTitle(){
		return title;
	}
	
	
	/**
	 * Returns a string representation of the users and ratings for the movie
	 * @return A string representation of the uesrs and ratings for the movie
	 */
	public String toString(){
		String str = "("+ movieId + ") " + title + "\n";
		str += "Num Ratings: " + ratings.size() + "\n";
		str += "Genres: ";
		for(String genre : genres) {
			str += genre + " ";
		}
		str += "\n";
			
//		for( Map.Entry<Integer, Double> entry : ratings.entrySet()){
//			int userId = entry.getKey();
//			double rating = entry.getValue();
//			str += "\tuser=" + userId + " rating=" + rating + "\n";
//		}
		return str;
	}
	
	/**
	 * Determines if two movies are equivalent
	 * @param other An object to compare against
	 * @return true if this movie and the other movie are the same, false otherwise
	 */
	@Override
	public boolean equals(Object other){
		if(other == this){
			return true;
		}		
		if(!(other instanceof Movie)){
			return false;
		}		
		Movie m = (Movie) other;		
		return movieId == m.movieId;
	}
	
	/**
	 * Returns a hashcode for the movie
	 * @return A hashcode for the movie
	 */
	@Override
	public int hashCode(){
		return Objects.hash(movieId);
	}
}
