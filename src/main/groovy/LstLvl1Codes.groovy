/**
 * EXT001MI.LstLvl1Codes
 * Extension API to list the level 1 reason codes these are stored in either CSYTAB or MWDSTC dependent on the program
 * they are required for.
 * Date	    Changed By  Description
 */
public class LstLvl1Codes extends ExtendM3Transaction {
  private final MIAPI mi;
  private final DatabaseAPI database;
  private final ProgramAPI program;

  public LstLvl1Codes(MIAPI mi, DatabaseAPI database, ProgramAPI program) {
    this.mi = mi;
    this.database = database;
    this.program = program;
  }

  public void main() {
    String programName = mi.inData.ZPGM.toString().trim()
    String level1 = mi.inData.ZLV1.toString().trim()

    if (programName == "CRS090" || programName.isBlank()) {
      DBAction queryCSYTAB = database.table("CSYTAB").index("20").selection("CTCONO", "CTDIVI", "CTSTCO", "CTSTKY", "CTTX40", "CTTX15").build()
      DBContainer containerCSYTAB = queryCSYTAB.getContainer()
      containerCSYTAB.set("CTCONO", program.LDAZD.CONO)
      containerCSYTAB.set("CTSTCO", "SCRE")
      queryCSYTAB.readAll(containerCSYTAB, 2, handlerCSYTAB)
    }
    if (programName == "PDS044" || programName.isBlank()) {
      DBAction queryMWDSTC = database.table("MWDSTC").index("00").selection("M2CONO", "M2DICE", "M2TX30", "M2TX15").build()
      DBContainer containerMWDSTC = queryMWDSTC.getContainer()
      containerMWDSTC.set("M2CONO", program.LDAZD.CONO)
      queryMWDSTC.readAll(containerMWDSTC, 1, handlerMWDSTC)
    }
  }

  Closure<?> handlerCSYTAB = {DBContainer result ->
    String level1 = mi.inData.ZLV1.toString().trim()
    if (level1.isBlank() || result.getString("CTSTKY") == level1) {
      mi.outData.put("PGNM", "CRS090")
      mi.outData.put("ZLV1", result.getString("CTSTKY"))
      mi.outData.put("TX40", result.getString("CTTX40"))
      mi.outData.put("TX15", result.getString("CTTX15"))
      mi.write()
    }
  }

  Closure<?> handlerMWDSTC = {DBContainer result ->
    String level1 = mi.inData.ZLV1.toString().trim()
    if (level1.isBlank() || result.getString("M2DICE") == level1) {
      mi.outData.put("PGNM", "PDS044")
      mi.outData.put("ZLV1", result.getString("M2DICE"))
      mi.outData.put("TX40", result.getString("M2TX30"))
      mi.outData.put("TX15", result.getString("M2TX15"))
      mi.write()
    }
  }

}
