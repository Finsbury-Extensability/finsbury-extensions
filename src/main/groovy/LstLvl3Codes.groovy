/**
 * EXT001MI.LstLvl3Codes
 * Extension API to list the level 3 reason codes stored in CUGEX1
 * Date	    Changed By  Description
 */
public class LstLvl3Codes extends ExtendM3Transaction {
  private final MIAPI mi;
  private final DatabaseAPI database;
  private final ProgramAPI program

  public LstLvl3Codes(MIAPI mi, DatabaseAPI database, ProgramAPI program) {
    this.mi = mi;
    this.database = database;
    this.program = program;
  }

  public void main() {
    String programName = mi.inData.ZPGM
    String level1Code = mi.inData.ZLV1
    String level2Code = mi.inData.ZLV2

    DBAction queryCUGEX1 = database.table("CUGEX1").index("00").selection("F1CONO", "F1FILE", "F1PK01", "F1PK02", "F1PK03", "F1PK04", "F1PK05", "F1A030", "F1A121").build()
    DBContainer containerCUGEX1 = queryCUGEX1.getContainer()
    containerCUGEX1.set("F1CONO", program.LDAZD.CONO)
    containerCUGEX1.set("F1FILE", "ZRCLVL")
    containerCUGEX1.set("F1PK01", programName);
    containerCUGEX1.set("F1PK02", level1Code)
    containerCUGEX1.set("F1PK03", level2Code)
    queryCUGEX1.readAll(containerCUGEX1, 5, handler)
  }

  Closure<?> handler = {DBContainer result ->
    if (!result.getString("F1PK04").isBlank() && result.getString("F1PK05").isBlank()) {
      mi.outData.put("PGNM", result.getString("F1PK01"))
      mi.outData.put("ZLV1", result.getString("F1PK02"))
      mi.outData.put("ZLV2", result.getString("F1PK03"))
      mi.outData.put("ZLV3", result.getString("F1PK04"))
      mi.outData.put("TX40", result.getString("F1A121"))
      mi.outData.put("TX15", result.getString("F1A030"))
      mi.write()
    }
  }
}
