package analyzer;

import data.Movie;
import graph.GraphIfc;

import java.io.File;  // Import the File class
import java.io.FileWriter;
import java.io.IOException;  // Import the IOException class to handle errors
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class MakeJSON {
    public static void graphToJSON(GraphIfc<Integer> g, Map<Integer, Movie> movies) {
        try {
            File json = new File("graph.json");
            json.createNewFile();
            writeFile(json,g,movies);

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public static void writeFile(File json, GraphIfc<Integer> g,Map<Integer, Movie> movies){
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("\t\"nodes\": [\n");
        for(int i =1; i <= g.numVertices(); i++){
            sb.append("\t\t{ \"name\": \"" + movies.get(i).getTitle() + "\", ");
            sb.append("\"genres\": [");
            int j = 0;
            for(String s : movies.get(i).getGenres()){
                sb.append("\"" + s + "\"" + (j >= movies.get(i).getGenres().size()-1 ? "" : ","));
                j++;
            }
            sb.append("]");
            sb.append("},\n");
        }
        sb.append("\t],\n" + "\t\"edges\": [");
        for(int i = 0; i < g.numVertices() ; i++){
            for(int k = i+1; k < g.numVertices(); k++){
                if (g.edgeExists(i, k)) {
                    sb.append("\t\t{ \"source\": " + (i) + ", \"target\": " + (k) + " },\n");
                }
            }
        }
        sb.append("\t]\n" + "}");
        try {
            FileWriter fw = new FileWriter(json, false);
            fw.write(sb.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}