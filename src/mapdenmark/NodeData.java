package mapdenmark;

/**
 * An object storing the raw node data from the krak data file.
 */
public class NodeData {
	final int ARC;
	final int KDV;
	final int KDV_ID;
	final double X_COORD;
	final double Y_COORD;

	/**
	 * Parses node data from line, throws an IOException
	 * if something unexpected is read
	 * @param line The source line from which the NodeData fields are parsed
	 */
	public NodeData(String line) {
		DataLine dl = new DataLine(line);
		ARC = dl.getInt();
		KDV = dl.getInt();
		KDV_ID = dl.getInt();
		X_COORD = dl.getDouble();
		Y_COORD = dl.getDouble();
	}

	/**
	 * Returns a string representing the node data in the same format as used in the
	 * kdv_node_unload.txt file.
	 */
	public String toString() {
		return ARC + "," + KDV + "," + KDV_ID + "," + X_COORD + "," + Y_COORD;
	}
}