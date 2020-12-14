/**
 * PMS060.IssueMethod7Check
 * Extension for PMS060 to validate the location entered matches is the same as the one on the material line
 * if the length and width fields are 7.
 * Date	    Changed By  Description
 */
public class IssueMethod7Check extends ExtendM3Trigger {
  private final ProgramAPI program
  private final InteractiveAPI interactive
  private final LoggerAPI logger

  public IssueMethod7Check(ProgramAPI  program, InteractiveAPI interactive, LoggerAPI logger) {
    this.program = program
    this.interactive = interactive
    this.logger = logger
  }

  public void main() {
    Map<String, Object> material = program.getTableRecord("MWOMAT")

    String WHSL = material.VMWHSL.toString().trim()
    String scrWHSL = interactive.display.fields.get("WMWHSL").toString().trim()
    if (material.VMLGTH == 7 && material.VMWDTH == 7 && !scrWHSL.equals(WHSL)) {
      interactive.showCustomError("WMWHSL", "Location must equal " + WHSL + " for this item")
    }
  }
}
