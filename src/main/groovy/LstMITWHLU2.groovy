public class LstMITWHLU2 extends ExtendM3Transaction {
  private final MIAPI mi;
  private final DatabaseAPI database;
  private final ProgramAPI program;

  public LstMITWHLU2(MIAPI mi, DatabaseAPI database, ProgramAPI program) {
    this.mi = mi;
    this.database = database;
    this.program = program;
  }

  public void main() {
    String warehouse = mi.inData.WHLO

    DBAction queryMITWHL = database.table("MITWHL").index("00").selection("MWCONO", "MWWHNM", "MWDIVI", "MWFACI", "MWWHTY").build()
    DBContainer containerMITWHL = queryMITWHL.getContainer()
    containerMITWHL.set("MWCONO", program.LDAZD.CONO)
    containerMITWHL.set("MWWHLO", warehouse)
    queryMITWHL.readAll(containerMITWHL, 1, handler)
  }

  Closure<?> handler = {DBContainer result ->
    if (result.getString("MWWHTY") == "Z4") {
      mi.outData.put("WHLO", result.getString("MWWHLO"))
      mi.outData.put("WHNM", result.getString("MWWHNM"))
      mi.outData.put("DIVI", result.getString("MWDIVI"))
      mi.outData.put("FACI", result.getString("MWFACI"))
      mi.outData.put("WHTY", result.getString("MWWHTY"))
      mi.write()
    }
  }
}
