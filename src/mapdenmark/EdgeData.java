package mapdenmark;

/**
 * Represents the raw data from a line in kdv_unload.txt.
 */
public class EdgeData {
	public final int FNODE; 
	public final int TNODE; 
	public final double LENGTH;
	public final int DAV_DK;
	public final int DAV_DK_ID;
	public final int TYP;
	public final String VEJNAVN;
	public final int FROMLEFT;
	public final int TOLEFT;
	public final int FROMRIGHT;
	public final int TORIGHT;
	public final String FROMLEFT_BOGSTAV;
	public final String TOLEFT_BOGSTAV;
	public final String FROMRIGHT_BOGSTAV;
	public final String TORIGHT_BOGSTAV;
	public final int V_SOGNENR;
	public final int H_SOGNENR;
	public final int V_POSTNR;
	public final int H_POSTNR;
	public final int KOMMUNENR;
	public final int VEJKODE;
	public final int SUBNET;
	public final String RUTENR;
	public final int FRAKOERSEL;
	public final int ZONE;
	public final int SPEED;
	public final double DRIVETIME;
	public final String ONE_WAY;
	public final String F_TURN;
	public final String T_TURN;
	public final int VEJNR;
	public final String AENDR_DATO;
	public final int TJEK_ID;

	public String toString() {
		return 
			FNODE + "," +
			TNODE + "," +
			String.format("%.5f,", LENGTH) +
			DAV_DK + "," +
			DAV_DK_ID + "," +
			TYP + "," +
			"'" + VEJNAVN +"'," +
			FROMLEFT + "," +
			TOLEFT + "," +
			FROMRIGHT + "," +
			TORIGHT + "," +
			"'" + FROMLEFT_BOGSTAV + "'," +
			"'" + TOLEFT_BOGSTAV + "'," +
			"'" + FROMRIGHT_BOGSTAV + "'," +
			"'" + TORIGHT_BOGSTAV + "'," +
			V_SOGNENR + "," +
			H_SOGNENR + "," +
			V_POSTNR + "," +
			H_POSTNR + "," +
			KOMMUNENR + "," +
			VEJKODE + "," +
			SUBNET + "," +
			"'" + RUTENR + "'," +
			FRAKOERSEL + "," +
			ZONE + "," +
			SPEED + "," +
			String.format("%.3f,", DRIVETIME) +
			"'" + ONE_WAY + "'," +
			"'" + F_TURN + "'," +
			"'" + T_TURN + "'," +
			(VEJNR == -1 ? "**********," : VEJNR + ",") +
			"'" + AENDR_DATO + "'," +
			TJEK_ID;
	}

	public EdgeData(String line) {
		DataLine dl = new DataLine(line);
		FNODE = dl.getInt();
		TNODE = dl.getInt();
		LENGTH = dl.getDouble();
		DAV_DK = dl.getInt();
		DAV_DK_ID = dl.getInt();
		TYP = dl.getInt();
		VEJNAVN = dl.getString();
		FROMLEFT = dl.getInt();
		TOLEFT = dl.getInt();
		FROMRIGHT = dl.getInt();
		TORIGHT = dl.getInt();
		FROMLEFT_BOGSTAV = dl.getString();
		TOLEFT_BOGSTAV = dl.getString();
		FROMRIGHT_BOGSTAV = dl.getString();
		TORIGHT_BOGSTAV = dl.getString();
		V_SOGNENR = dl.getInt();
		H_SOGNENR = dl.getInt();
		V_POSTNR = dl.getInt();
		H_POSTNR = dl.getInt();
		KOMMUNENR = dl.getInt();
		VEJKODE = dl.getInt();
		SUBNET = dl.getInt();
		RUTENR = dl.getString();
		FRAKOERSEL = dl.getInt();
		ZONE = dl.getInt();
		SPEED = dl.getInt();
		DRIVETIME = dl.getDouble();
		ONE_WAY = dl.getString();
		F_TURN = dl.getString();
		T_TURN = dl.getString();
		VEJNR = dl.getInt();
		AENDR_DATO = dl.getString();
		TJEK_ID = dl.getInt();
	}
}