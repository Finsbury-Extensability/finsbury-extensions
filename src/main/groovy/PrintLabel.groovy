/**
 * MMS177.Print Label
 * Extension for MMS177 to print a pallet label if the session parameter has been set by MMS177.CreateNewPallet
 * Date	    Changed By  Description
 */
public class PrintLabel extends ExtendM3Trigger {
  private final ProgramAPI program
  private final SessionAPI session
  private final LoggerAPI logger
  private final InteractiveAPI interactive
  private final MICallerAPI miCaller

  public PrintLabel(ProgramAPI program, SessionAPI session, InteractiveAPI interactive, LoggerAPI logger, MICallerAPI miCaller) {
    this.program = program
    this.session = session
    this.interactive = interactive
    this.logger = logger
    this.miCaller = miCaller
  }

  public void main() {

    String newPallet = session.parameters.get("newPallet")

    logtext("newPallet = " + newPallet, true)
    if (newPallet == "true") {
      String WHLO = interactive.display.fields.WWWHLO
      String ITNO = interactive.display.fields.WWITNO
      String WHSL = interactive.display.fields.WWTWSL
      String BANO = interactive.display.fields.WWBANO
      String CAMU = interactive.display.fields.WWTOCA
      String PRT = PrtLabel(WHLO, ITNO, WHSL, BANO, CAMU)
      if (!PRT.isBlank()) {
        logtext("Print not completed - " + PRT, true)
      }
    }
  }

  /**
   * Print pallet label
   * @param WHLO warehouse
   * @param ITNO item number
   * @param WHSL location
   * @param BANO lot number
   * @param CAMU container
   * @return result result of API call otherwise will contain error
   */
  public String PrtLabel(String WHLO, String ITNO, String WHSL, String BANO, String CAMU) {
    def parameters = ["WHLO": WHLO, "ITNO":ITNO, "WHSL": WHSL, "BANO": BANO, "CAMU": CAMU]
    logtext("PrintLabel parms ${parameters}", true)
    String result = ""
    Closure<?> handler = { Map<String, String> response ->
      if (response.containsKey('errorMsid')){
        return result
      }
    }
    miCaller.call("MMS060MI", "PrtPutAwayLbl", parameters, handler)
    logtext("Result of PrtPutAwayLbl: ${result}", true)
    return result
  }

  def logtext(String text, boolean log) {

    if (log == false) {
      return
    }
    logger.debug("Debug $text")

    //interactive.showCustomInfo("Proof $text ran the extension")
  }
}
