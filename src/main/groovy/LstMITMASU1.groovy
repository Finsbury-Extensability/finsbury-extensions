/**
 * EXT001MI.LstMITMASU1
 * Extension API to list items at status 20
 * Date	    Changed By  Description
 */
public class LstMITMASU1 extends ExtendM3Transaction {
  private final MIAPI mi;
  private final DatabaseAPI database;
  private final ProgramAPI program;

  public LstMITMASU1(MIAPI mi, DatabaseAPI database, ProgramAPI program) {
    this.mi = mi;
    this.database = database;
    this.program = program;
  }

  public void main() {
    String item = mi.inData.ITNO
    String status = "20"

    DBAction queryMITMAS = database.table("MITMAS").index("20").selection("MMCONO", "MMSTAT", "MMITDS").build()
    DBContainer containerMITMAS = queryMITMAS.getContainer()
    containerMITMAS.set("MMCONO", program.LDAZD.CONO)
    containerMITMAS.set("MMSTAT", status)
    containerMITMAS.set("MMITNO", item)
    queryMITMAS.readAll(containerMITMAS, 2, handler)
  }

  Closure<?> handler = {DBContainer result ->
      mi.outData.put("ITNO", result.getString("MMITNO"))
      mi.outData.put("ITDS", result.getString("MMITDS"))
      mi.write()
  }
}
