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
    //String user = program.getUser()
    //logger.debug("User = " + user)
    Map<String, String> material = program.getTable("MWOMAT")

    String WHSL = material.VMWHSL
    String LGTH = material.VMLGTH
    String WDTH = material.VMWDTH
    String scrWHSL = interactive.display.fields.get("WMWHSL")

    if (LGTH == "0000007" && WDTH == "0000007" && scrWHSL != WHSL) {
      interactive.showCustomError("WMWHSL", "Location must equal " + WHSL + " for this item")
    }
  }
}