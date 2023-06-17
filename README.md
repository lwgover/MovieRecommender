# MovieRecommender
<img width="1415" alt="MoviesGraph" src="https://github.com/lwgover/MovieRecommender/assets/73131292/618abf72-8dec-4b7f-aca1-68f625b002d1">

## Description
This project was designed to generate a graph of movies from a movie review database using custom-coded Graph, WeightedGraph, and PriorityQueue classes. I implemented Dijkstra's algorithm and the Floyd-Warshall algorithm to generate movie recommendations based on similarity scores. I created an efficient interface that allowed users to gain insights into the graph, navigating the similarities between movies. As part of this project, I wrote a visual representation of the graph with D3.js, which was designed to give users a clearer understanding of the movie relationships. 

## How to use

The interface is contained within the `src/Analyzer/MovieLensAnalyzer.java` class. In the terminal, run `javac src/Analyzer/MovieLensAnalyzer.java` then `java src/Analyzer/MovieLensAnalyzer.java` to get movie recommendations!

## Visualization Demo

To see the live demo, go to [lucasgover.com/MovieRecommender](https://www.lucasgover.com/MovieRecommender/).

## Sample Terminal Interaction

```
========== Welcome to MovieLens Analyser ==========
The files being analysed are:
src/ml-latest-small/ratings.csv
src/ml-latest-small/movies.csv

[Option 1] u and v are adjacent if the same 12 users gave the same rating to both movies
[Option 2] u and v are adjacent if the same 12 users watched both movies (regardless of rating)
[Option 3] u and v are adjacent they share at least one genre
[Option 4] u and v have a weighted edge that is larger the more different the movies are
[Option 5] Small graph in which each node is connected to the two most similar movies
Chose an option to build the Graph(1-5): 3


[Option 1] Print out statistics about the graph
[Option 2] Print node information
[Option 3] Display the shortest path between nodes
[Option 4] Sort Movies by most similar to your movie(Works best with a weighted Graph)
[Option 5] Quit
[Option 6] Write graph as json file
Chose an option(1-6): 1
|V| = 1000 vertices
|E| = 544922 edges
Density = 0.5454674674674674
Max. degree = 948
Diameter = 3 (2 to 52)
Avg. path length = 1.455604

[Option 1] Print out statistics about the graph
[Option 2] Print node information
[Option 3] Display the shortest path between nodes
[Option 4] Sort Movies by most similar to your movie(Works best with a weighted Graph)
[Option 5] Quit
[Option 6] Write graph as json file
Chose an option(1-6): 5
Exiting...bye

Process finished with exit code 0
```
