package mapdenmark;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Parse Krak data files (kdv_node_unload.txt, kdv_unload.txt).
 *
 * Customize to your needs by overriding processNode and processEdge. 
 * See example in main. 
 *
 * Original author Peter Tiedemann petert@itu.dk; 
 * updates (2014) by Søren Debois, debois@itu.dk
 */
public abstract class KrakLoader {

	public abstract void processNode(NodeData nd);
	public abstract void processEdge(EdgeData ed);

	/**
	 * Load krak-data from given files, invoking 
	 * processNode and processEdge once for each node- and edge-
	 * specification in the input file, respectively. 
	 *
	 * @param nodeFile
	 * @param edgeFile
	 * @return
	 * @throws IOException if there is a problem reading data or the files don't exist
	 */
	public void load(String nodeFile, String edgeFile) throws IOException {
		/* Nodes. */
		BufferedReader br;
		br = new BufferedReader(new FileReader(nodeFile));
		br.readLine(); // First line is column names, not data.

		String line;
		while( (line = br.readLine()) != null ) {
			processNode(new NodeData(line)); 
		}
		br.close();

		/* Edges. */

		br = new BufferedReader(new FileReader(edgeFile));
		br.readLine(); // Again, first line is column names, not data.

		while( (line = br.readLine()) != null ) {
			processEdge(new EdgeData(line)); 
		}
		br.close();

		DataLine.resetInterner();
		System.gc();
	}
}