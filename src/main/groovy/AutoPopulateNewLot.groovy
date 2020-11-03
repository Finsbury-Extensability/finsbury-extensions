/**
 * MMS130.AutoPopulateNewLot
 * Extension for MMS130 to autopopulate the new lot number so that the Reclassify extension knows the new lot number 
 * so it can be reclassified to the correct status
 *
 * Date	    Changed By  Description
 */
public class AutoPopulateNewLot extends ExtendM3Trigger {
  private final ProgramAPI program
  private final SessionAPI session
  private final InteractiveAPI interactive
  private final LoggerAPI logger

  public AutoPopulateNewLot(ProgramAPI program, SessionAPI session, InteractiveAPI interactive, LoggerAPI logger) {
    this.program = program
    this.session = session
    this.interactive = interactive
    this.logger = logger
  }

  public void main() {
    String user = program.getUser()

    logtext(user, true)

    // Get required values from screen
    String NBAN = interactive.display.fields.get("WWNBAN")
    String TRDT = interactive.display.fields.get("WWTRDT")
    String TRTM = interactive.display.fields.get("WWTRTM")
    String WHSL = interactive.display.fields.get("WWWHSL")
    logtext("NBAN before=" + NBAN, true)
    boolean IN72 = program.indicator.get(72)
    boolean IN75 = program.indicator.get(75)
    boolean IN55 = program.indicator.get(55)

    if (!IN72 && IN75 && IN55) {
    } else {
      if (!WHSL.isEmpty()) {
        if (NBAN.isEmpty()) {
          interactive.display.fields.put("WWNBAN", TRDT + TRTM)
        }
      }
    }
    logtext("NBAN after=" + NBAN, true)
  }

  /**
   * logtext
   * @param text text
   * @param log log
   */
  def logtext(String text, boolean log) {

    if (log == false) {
      return
    }
    logger.info("Debug $text")
  }

}
