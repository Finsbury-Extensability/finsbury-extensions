/**
 * PMS060MI.IssueMethod7CheckForAPI
 * Extension for PMS060MI to validate the location entered matches is the same as the one on the material line
 * if the length and width fields are 7.
 * Date	    Changed By  Description
 * 20201025 NJOHNSON    Error using MTNO instead of MSEQ to set key values.
 */
public class IssueMethod7CheckForAPI extends ExtendM3Trigger {
  private final TransactionAPI transaction;
  private final DatabaseAPI database;
  private final ProgramAPI program;

  public IssueMethod7CheckForAPI(TransactionAPI transaction,  DatabaseAPI database, ProgramAPI program) {
    this.transaction = transaction
    this.database = database;
    this.program = program
  }

  public void main() {
    // Get required input fields
    String inWOSQ = transaction.parameters.get("WOSQ")
    String inFACI = transaction.parameters.get("FACI")
    String inPRNO = transaction.parameters.get("PRNO")
    String inMFNO = transaction.parameters.get("MFNO")
    String inMSEQ = transaction.parameters.get("MSEQ")
    String inMTNO = transaction.parameters.get("MTNO")

    if (inWOSQ == null) {
      inWOSQ = ""
    }
    if (inFACI == null) {
      inFACI = ""
    }
    if (inPRNO == null) {
      inPRNO = ""
    }
    if (inMFNO == null) {
      inMFNO = ""
    }
    if (inMSEQ == null) {
      inMSEQ = ""
    }
    if (inMTNO == null) {
      inMTNO = ""
    }

    DBAction HED55 = database.table("MWOHED").index("55").selection("VHCONO", "VHFACI", "VHPRNO", "VHMFNO").build()
    DBContainer HEDcntr = HED55.getContainer()
    DBAction MAT50 = database.table("MWOMAT").index("50").selection("VMCONO", "VMFACI", "VMPRNO", "VMMFNO", "VMWOSQ",
      "VMMTNO", "VMWHSL", "VMLGTH", "VMWDTH", "VMSPMT", "VMPYPR").build()
    DBAction MAT30 = database.table("MWOMAT").index("30").selectAllFields().build()
    DBAction MAT00 = database.table("MWOMAT").index("00").selectAllFields().build()
    DBContainer MATcntr = MAT50.getContainer()

    // WOSQ passed so go straight to MWOMAT50
    if (!inWOSQ.isEmpty() && !inWOSQ.isBlank()) {
      MATcntr.set("VMCONO", program.LDAZD.CONO)
      MATcntr.set("VMWOSQ", inWOSQ.toLong())
      MAT50.readAll(MATcntr, 2, handler)
    } else {
      if (inPRNO.isEmpty() || inPRNO.isBlank()) {
        HEDcntr.set("VHCONO", program.LDAZD.CONO)
        HEDcntr.set("VHFACI", inFACI)
        HEDcntr.set("VHMFNO", inMFNO)
        if (HED55.read(HEDcntr)) {
          inPRNO = HEDcntr.getString("VHPRNO")
        }
      }

      if ((inMSEQ.isEmpty() || inMSEQ.isBlank()) && (!inMTNO.isEmpty() && !inMTNO.isBlank())) {
        MATcntr.set("VMCONO", program.LDAZD.CONO)
        MATcntr.set("VMFACI", inFACI)
        MATcntr.set("VMPRNO", inPRNO)
        MATcntr.set("VMMFNO", inMFNO)
        MATcntr.set("VMMTNO", inMTNO)
        MAT30.readAll(MATcntr, 5, handler)
      } else {
        if (!inMSEQ.isEmpty() && !inMSEQ.isBlank()) {
          MATcntr.set("VMCONO", program.LDAZD.CONO)
          MATcntr.set("VMFACI", inFACI)
          MATcntr.set("VMPRNO", inPRNO)
          MATcntr.set("VMMFNO", inMFNO)
          //MATcntr.set("VMMSEQ", inMTNO.toInteger()) //D 20201025
          MATcntr.set("VMMSEQ", inMSEQ.toInteger()) //A 20201025
          MAT00.readAll(MATcntr, 5, handler)
        }
      }
    }
  }

  Closure<?> handler = {DBContainer result ->
    String inWHSL = transaction.parameters.get("WHSL")
    String WHSL = result.getString("VMWHSL")
    int LGTH = result.getInt("VMLGTH")
    int WDTH = result.getInt("VMWDTH")
    int SPMT = result.getInt("VMSPMT")
    int BYPR = result.getInt("VMBYPR")
    if ((SPMT != 1 && SPMT < 2 ) && BYPR != 1) {
      if ((LGTH == 7 && WDTH == 7) && inWHSL != WHSL) {
        String message = "Location must equal " + WHSL + " for this item"
        transaction.abortTransaction("WHSL", "CPF9898", message)
      }
    }
  }
}
