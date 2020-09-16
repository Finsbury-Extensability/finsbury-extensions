/**
 * MMS177.CreateNewPallet
 * Extension for MMS177 to trigger API's to perform the stock movements as certain scenarios don't work in MMS177.
 * Also will populate the to container number if the container management type is 7 and the to location is container managed.
 * If a new pallet is created then a session parm will be set to trigger a label to be printed in MMS177.PrintLabel
 *
 * Scenario 1 - From and to locations container managed and no trans qty entered then use MMS850MI/AddPackMove
 * Scenario 2 = From location container managed but to location istn't use MMS850MI/AddMove
 *
 * Date	    Changed By  Description
 */
public class CreateNewPallet extends ExtendM3Trigger {
  private final ProgramAPI program
  private final SessionAPI session
  private final InteractiveAPI interactive
  private final MICallerAPI miCaller
  private final LoggerAPI logger
  public CreateNewPallet(ProgramAPI program, SessionAPI session, InteractiveAPI interactive, MICallerAPI miCaller, LoggerAPI logger) {
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
    String BANO = interactive.display.fields.get("WWBANO")
    String CAMU = interactive.display.fields.get("WWCAMU")
    String TWSL = interactive.display.fields.get("WWTWSL")
    String TOCA = interactive.display.fields.get("WWTOCA")
    String TRQT = interactive.display.fields.get("WWTRQT")

    // Define variables
    String COMG = ""
    String PACT = ""
    Map<String, String> Itm = null
    Map<String, String> ItmWhs = null
    Map<String, String> FromLocation = null
    Map<String, String> ToLocation = null
    Map<String, String> BalID = null
    Map<String, String> MoveAPI = null

    logtext(TOCA, true)

    Itm = GetItem(ITNO)
    ItmWhs = GetItmWhs(WHLO, ITNO)
    FromLocation = GetLocation(WHLO, WHSL)
    ToLocation = GetLocation(WHLO, TWSL)
    BalID = GetBalanceID(WHLO, ITNO, WHSL, BANO, CAMU)

    String newPallet = "false"

    if (ItmWhs.COMG == "7") {
      if (ToLocation.CMNG == "1") {
        if (TOCA.isBlank()) {
          PACT = GetPackaging(ITNO)
          logtext("PACT= ${PACT}", true)
          if (PACT.isEmpty()) {
            interactive.showCustomError("WWITNO", "No packaging record found for item container cannot be auto created")
            return;
          }
          TOCA = GetContainer(PACT)
          logtext("TOCA= ${TOCA}", true)
          if (!TOCA.isBlank()) {
            interactive.display.fields.put("WWTOCA", TOCA)
          }
        }
        newPallet = "true"
      }

      // If both from and to locations are container managed then execute MMS850MI/AddPackMove
      if (FromLocation.CMNG == "1" && ToLocation.CMNG == "1" && TRQT.isBlank()) {
        MoveAPI = AddPackMove(WHLO, CAMU, TWSL)
        if (!MoveAPI.containsKey('errorMsid')) {
          SetLatestMoved(WHLO, ItmWhs.WHNM, ITNO, Itm.ITDS, WHSL, FromLocation.SLDS,
            BANO, CAMU, BalID.STQT, ItmWhs.MNUN)
          interactive.showCustomError("WWITNO", "")
        } else {
          interactive.showCustomError("WWITNO", MoveAPI.errorMessage)
        }
      }

      // If from location is container managed but to location isn't then execute MMS850MI/AddMove
      if (FromLocation.CMNG == "1" && ToLocation.CMNG == "0") {
        String LatestMovedQTY = TRQT
        MoveAPI = AddMove(WHLO, WHSL, ITNO, BANO, CAMU, TRQT, TWSL, TOCA)
        if (!MoveAPI.containsKey('errorMsid')) {
          if (LatestMovedQTY.isBlank()) {
            LatestMovedQTY = BalID.STQT
          }
        } else {
          interactive.showCustomError("WWITNO", MoveAPI.errorMessage)
        }

        SetLatestMoved(WHLO, ItmWhs.WHNM, ITNO, Itm.ITDS, WHSL, FromLocation.SLDS,
          BANO, CAMU, LatestMovedQTY, ItmWhs.MNUN)
        interactive.showCustomError("WWITNO", "")
      }

    }
    session.parameters.put("newPallet", newPallet)
  }



  /**
   * Set latest moved fields on display
   * @param
   * @return
   */
  public void SetLatestMoved (String WHLO, String WHNM, String ITNO, String ITDS, String WHSL, String SLDS,
                              String BANO, String CAMU, String TRQT, String MNUN) {
    interactive.display.fields.put("WLWHLO", WHLO)
    interactive.display.fields.put("WLWHNM", WHNM)
    interactive.display.fields.put("WLITNO", ITNO)
    interactive.display.fields.put("WLITDS", ITDS)
    interactive.display.fields.put("WLWHSL", WHSL)
    interactive.display.fields.put("WLSLDS", SLDS)
    interactive.display.fields.put("WLBANO", BANO)
    interactive.display.fields.put("WLCAMU", CAMU)
    interactive.display.fields.put("WLTRQT", TRQT)
    interactive.display.fields.put("WLMNUN", MNUN)

    PrtLabel(WHLO, ITNO, WHSL, BANO, CAMU)
  }


  /**
   * Get item
   * @param ITNO item number
   * @return result result of API call otherwise will contain error
   */
  public Map<String, String> GetItem(String ITNO) {
    def parameters = ["ITNO":ITNO]
    logtext("GetItem parms ${parameters}", true)
    Map<String, String> result = null
    Closure<?> handler = { Map<String, String> response ->
      logtext("GetItem response ${response}", true)
      result = response
      return result
    }

    miCaller.call("MMS200MI", "GetItmBasic", parameters, handler)
    return result
  }


  /**
   * Get item warehouse
   * @param WHLO warehouse
   * @param ITNO item number
   * @return result result of API call otherwise will contain error
   */
  public  Map<String, String> GetItmWhs(String WHLO, String ITNO) {
    def parameters = ["WHLO":WHLO, "ITNO":ITNO]
    logtext("GetItmWhs parms ${parameters}", true)
    Map<String, String> result = null
    Closure<?> handler = { Map<String, String> response ->
      logtext("GetItmWhs response ${response}", true)
      result = response
      return result
    }

    miCaller.call("MMS200MI", "GetItmWhsBasic", parameters, handler)
    return result
  }

  /**
   * Get location
   * @param WHLO warehouse
   * @param WHSL location
   * @return result result of API call otherwise will contain error
   */
  public Map<String, String> GetLocation(String WHLO, String WHSL) {
    def parameters = ["WHLO":WHLO, "WHSL":WHSL]
    logtext("GetLocation parms ${parameters}", true)
    Map<String, String> result = null
    Closure<?> handler = { Map<String, String> response ->
      logtext("GetLocation response ${response}", true)
      result = response
      return result
    }

    miCaller.call("MMS010MI", "GetLocation", parameters, handler)
    return result
  }

/**
 * Get balance id
 * @param WHLO warehouse
 * @param ITNO item number
 * @param WHSL location
 * @param BANO lot number
 * @param CAMU container
 * @return result result of API call otherwise will contain error
 */
  public Map<String, String> GetBalanceID(String WHLO, String ITNO, String WHSL, String BANO, String CAMU) {
    def parameters = ["WHLO":WHLO, "ITNO":ITNO, "WHSL":WHSL, "BANO":BANO, "CAMU":CAMU]
    logtext("GetBalanceID parms ${parameters}", true)
    Map<String, String> result = null
    Closure<?> handler = { Map<String, String> response ->
      logtext("GetBalanceID response ${response}", true)
      result = response
      return result
    }

    miCaller.call("MMS060MI", "Get", parameters, handler)
    return result
  }

  /**
   * Get packaging
   * @param ITNO item number
   * @return result result of API call otherwise will contain error
   */
  public String GetPackaging(String ITNO) {
    def parameters = ["ITNO":ITNO, "TRQT":"99999"]
    logtext("GetPackaging parms ${parameters}", true)
    String result = ""
    Closure<?> handler = { Map<String, String> response ->
      logtext("GetPackaging response ${response}", true)
      if (response.containsKey('errorMsid')) {
        return result
      }
      result = response.PACT
    }

    miCaller.call("MMS053MI", "GetItemConnPkg", parameters, handler)
    return result
  }

  /**
   * Get get container
   * @param PACT pacakaging
   * @return result result of API call otherwise will contain error
   */
  public String GetContainer(String PACT) {
    def parameters = ["PACT":PACT]
    logtext("GetContainer parms ${parameters}", true)
    String result = ""
    Closure<?> handler = { Map<String, String> response ->
      logtext("GetContainer response ${response}", true)
      if (response.containsKey('errorMsid')) {
        return result
      }
      result = response.PANR
    }

    miCaller.call("MMS470MI", "AddPackStk", parameters, handler)
    return result
  }

  /**
   * Print pallet label
   * @param WHLO warehouse
   * @param ITNO item number
   * @param WHSL location
   * @param BANO lot number
   * @param CAMO container
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

  /**
   * Add pack move
   * @param WHLO warehouse
   * @param PANR package number
   * @param TWSL to location
   * @return result result of API call otherwise will contain error
   */
  public Map<String, String> AddPackMove(String WHLO, String PANR, String TWSL) {
    String PRFL = "*EXE"
    String E0PA = "WS"
    String E065 = "WMS"
    def parameters = ["PRFL": PRFL, "E0PA":E0PA, "E065": E065, "WHLO": WHLO, "PANR": PANR, "TWSL":TWSL]
    logtext("AddPackMove parms ${parameters}", true)
    Map<String, String> result = null
    Closure<?> handler = { Map<String, String> response ->
      result = response
      return result
    }
    miCaller.call("MMS850MI", "AddPackMove", parameters, handler)
    logtext("Result of AddPackMove: ${result}", true)
    return result
  }

/**
 * Add move
 * @param WHLO warehouse
 * @param WHSL location
 * @param ITNO item number
 * @param BANO lot number
 * @param CAMU container
 * @param QLQT quantity
 * @param TWSL to location
 * @prarm TOCA to container
 * @return result result of API call otherwise will contain error
 */
  public Map<String, String> AddMove(String WHLO, String WHSL, String ITNO, String BANO,
                                     String CAMU, String QLQT, String TWSL, String TOCA) {
    String PRFL = "*EXE"
    String E0PA = "WS"
    String E065 = "WMS"
    def parameters = ["PRFL": PRFL, "E0PA":E0PA, "E065":E065, "WHLO":WHLO, "WHSL":WHSL, "ITNO":ITNO, "BANO":BANO,
                      "CAMU":CAMU, "QLQT":QLQT, "TWSL":TWSL, "TOCA":TOCA]
    logtext("AddMove parms ${parameters}", true)
    Map<String, String> result = null
    Closure<?> handler = { Map<String, String> response ->
      result = response
      return result
    }
    miCaller.call("MMS850MI", "AddMove", parameters, handler)
    logtext("Result of AddMove: ${result}", true)
    return result
  }

  def logtext(String text, boolean log) {

    if (log == false) {
      return
    }
    //logger.warning("Warning $text")
    logger.debug("Debug $text")
    //logger.error("Error $text")
    logger.trace("Trace $text")
    //logger.info("Info $text")
  }
}
