package i18ntool.consts;

public enum Preservation {
	
	UNKNOWN,
	
	NORMAL,
	
	/**
	 * only save key which value is NOT empty
	 */
	SPECIAL1,
	
	/**
	 * only save key which value is NOT empty but keep the old empty key
	 */
	SPECIAL2;
	
	
	public static Preservation parsePreservation(String arg) {
		if (arg.equals("NORMAL")) {
			return NORMAL;
		} else if (arg.equals("SPECIAL1")) {
			return SPECIAL1;
		} else if (arg.equals("SPECIAL2")) {
			return SPECIAL2;
		}
		return UNKNOWN;
	}
}
