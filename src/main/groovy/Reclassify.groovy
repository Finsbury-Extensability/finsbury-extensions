/**
 * MMS130.Reclassify
 * Extension for MMS130 to trigger API to reclassify the lot to the correct status as standard M3 always sets it to 
 * status 1 even if status 2 is selected
 *
 * Date	    Changed By  Description
 */
public class Reclassify extends ExtendM3Trigger {
  private final ProgramAPI program
  private final SessionAPI session
  private final InteractiveAPI interactive
  private final MICallerAPI miCaller
  private final LoggerAPI logger

  public Reclassify(ProgramAPI program, SessionAPI session, InteractiveAPI interactive, MICallerAPI miCaller, LoggerAPI logger) {
    this.program = program
    this.session = session
    this.interactive = interactive
    this.miCaller = miCaller
    this.logger = logger
  }

  public void main() {
    String user = program.getUser()

    logtext(user, true)

    // Get required values from screen
    String WHLO = interactive.display.fields.get("WWWHLO")
    String ITNO = interactive.display.fields.get("WWITNO")
    String WHSL = interactive.display.fields.get("WWWHSL")
    String BANO = interactive.display.fields.get("WWNBAN")
    String CAMU = interactive.display.fields.get("WWCAMU")
    String STAS = interactive.display.fields.get("WLSTAS")
    String ALOC = interactive.display.fields.get("WLALOC")
    logtext("BANO=" + interactive.display.fields.get("WWBANO") + " NBAN=" + interactive.display.fields.get("WWNBAN"), true)
    if (BANO.isEmpty()) {
      BANO = interactive.display.fields.get("WWBANO")
    }

    if (!WHSL.isEmpty()) {
      Map<String, String> rcl = RclLot(WHLO, ITNO, WHSL, BANO, CAMU, STAS, ALOC)

      if (!rcl.containsKey('errorMsid')) {
      } else {
        interactive.showCustomError("WWITNO", rcl.errorMessage)
      }
    }
  }


  /**
   * RclLot
   * @param WHLO warehouse
   * @param ITNO item number
   * @param WHSL location
   * @param BANO lot number
   * @param CAMU container
   * @param STAS status
   * @param ALOC allocable
   * @return result result of API call otherwise will contain error
   */
  public Map<String, String> RclLot(String WHLO, String ITNO, String WHSL, String BANO, String CAMU, String STAS, String ALOC) {
    String PRFL = "*EXE"
    String E0PA = "WS"
    String E065 = "WMS"
    def parameters = ["PRFL":PRFL, "E0PA": E0PA, "E065": E065, "WHLO":WHLO, "ITNO":ITNO, "WHSL":WHSL,
                      "BANO":BANO, "CAMU":CAMU, "STAS": STAS, "ALOC": ALOC]
    logtext("RclLot parms ${parameters}", true)
    Map<String, String> result = null
    Closure<?> handler = { Map<String, String> response ->
      logtext("RclLot response ${response}", true)
      result = response
      return result
    }

    miCaller.call("MMS850MI", "AddRclLotSts", parameters, handler)
    return result
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
