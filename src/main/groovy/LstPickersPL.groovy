/**
 * EXT420MI.LstPickersPL
 * Extension api for MWS420MI.LstPickersPL
 * Date	    Changed By  Description
 * 20200820	JEACAR01    Add ZCND - Customer name depot
 * 20201102 NJOHNSON    Remove .trim on input fields as this causes errors if fields are null
 */
public class LstPickersPL extends ExtendM3Transaction {
  private final MIAPI mi;
  private final DatabaseAPI database;
  private final MICallerAPI miCaller;
  private final LoggerAPI logger;
  private final ProgramAPI program;

  public LstPickersPL(MIAPI mi, DatabaseAPI database, MICallerAPI miCaller, LoggerAPI logger, ProgramAPI program) {
    this.mi = mi;
    this.database = database;
    this.miCaller = miCaller;
    this.logger = logger;
    this.program = program;
  }

  public void main() {

    def callback = {
      Map <String, String> out ->

        if (out.error != null) {
          mi.error(out.errorMessage);
          return;
        }

        mi.outData.put("CONO", out.get("CONO").trim());
        mi.outData.put("WHLO", out.get("WHLO").trim());
        mi.outData.put("DLIX", out.get("DLIX").trim());
        mi.outData.put("PLSX", out.get("PLSX").trim());
        mi.outData.put("PISS", out.get("PISS").trim());
        mi.outData.put("SLTP", out.get("SLTP").trim());
        mi.outData.put("PISE", out.get("PISE").trim());
        mi.outData.put("PLTM", out.get("PLTM").trim());
        mi.outData.put("SEEQ", out.get("SEEQ").trim());
        mi.outData.put("ESPD", out.get("ESPD").trim());
        mi.outData.put("ESPT", out.get("ESPT").trim());
        mi.outData.put("TEAM", out.get("TEAM").trim());
        mi.outData.put("PICK", out.get("PICK").trim());
        mi.outData.put("PLRI", out.get("PLRI").trim());
        mi.outData.put("WPIC", out.get("WPIC").trim());
        mi.outData.put("ARLD", out.get("ARLD").trim());
        mi.outData.put("ARLT", out.get("ARLT").trim());
        mi.outData.put("ARLE", out.get("ARLE").trim());
        mi.outData.put("ARLF", out.get("ARLF").trim());
        mi.outData.put("NPLL", out.get("NPLL").trim());
        mi.outData.put("NPLR", out.get("NPLR").trim());
        mi.outData.put("NPLP", out.get("NPLP").trim());
        mi.outData.put("CLOD", out.get("CLOD").trim());
        mi.outData.put("CLOT", out.get("CLOT").trim());
        mi.outData.put("ZDEV", out.get("DEVI").trim());
        mi.outData.put("DEVW", out.get("DEVW").trim());
        mi.outData.put("TWLO", out.get("TWLO").trim());
        mi.outData.put("OLIX", out.get("OLIX").trim());
        mi.outData.put("RGDT", out.get("RGDT").trim());
        mi.outData.put("RGTM", out.get("RGTM").trim());
        mi.outData.put("LMDT", out.get("LMDT").trim());
        mi.outData.put("CHNO", out.get("CHNO").trim());
        mi.outData.put("CHID", out.get("CHID").trim());
        mi.outData.put("DSDT", out.get("DSDT").trim());
        mi.outData.put("DSHM", out.get("DSHM").trim());
        mi.outData.put("TRDT", out.get("TRDT").trim());
        mi.outData.put("GRWE", out.get("GRWE").trim());
        mi.outData.put("PGRS", out.get("PGRS").trim());
        mi.outData.put("PIST", out.get("PIST").trim());
        mi.outData.put("CUNO", out.get("CUNO").trim());
        mi.outData.put("CUNM", out.get("CUNM").trim());
        mi.outData.put("RORC", out.get("RORC").trim());
        mi.outData.put("VLME", out.get("VLME").trim());
        mi.outData.put("VOL3", out.get("VOL3").trim());
        mi.outData.put("ROUT", out.get("ROUT").trim());
        mi.outData.put("RODN", out.get("RODN").trim());
        mi.outData.put("ZROD", out.get("RODS").trim());
        mi.outData.put("CONN", out.get("CONN").trim());
        mi.outData.put("LODO", out.get("LODO").trim());
        mi.outData.put("MODL", out.get("MODL").trim());
        mi.outData.put("MODF", out.get("MODF").trim());
        mi.outData.put("ZLPI", out.get("LPIC").trim());
        mi.outData.put("ZWPI", out.get("WTPI").trim());
        mi.outData.put("ZVPI", out.get("VTPI").trim());
        mi.outData.put("ZLPA", out.get("LPAC").trim());
        mi.outData.put("ZWPA", out.get("WTPA").trim());
        mi.outData.put("ZVPA", out.get("VTPA").trim());
        mi.outData.put("PRIO", out.get("PRIO").trim());
        mi.outData.put("RIDN", out.get("RIDN").trim());
        String zrdn = "";
        String zcnd = "";
        if (out.get("RORC").toInteger() == 3) {
          DBAction query = database.table("MHDISH")
            .index("00")
            .selection("OQCONO","OQINOU","OQDLIX","OQRIDN").build();
          DBContainer MHDISH = query.getContainer();
          MHDISH.set("OQCONO", out.get("CONO").trim().toInteger());
          MHDISH.set("OQINOU", 1);
          MHDISH.set("OQDLIX", out.get("DLIX").trim().toInteger());
          if(query.read(MHDISH)){
            zrdn = MHDISH.get("OQRIDN");
            zcnd = getDeliveryAddressName(zrdn, out);
          }
        }
        mi.outData.put("ZRDN", zrdn);
        mi.outData.put("ZCND", zcnd);
        mi.write();
    }

    def params = [ "WHLO": mi.inData.get("WHLO"),
                   "FPIS": mi.inData.get("ZFPI"),
                   "TPIS": mi.inData.get("ZTPI"),
                   "FORC": mi.inData.get("ZFOR"),
                   "TORC": mi.inData.get("ZTOR"),
                   "FSEQ": mi.inData.get("ZFSE"),
                   "TSEQ": mi.inData.get("ZTSE"),
                   "SSEQ": mi.inData.get("ZSSE"),
                   "ZFTP": mi.inData.get("ZFTP"),
                   "ZTTP": mi.inData.get("ZTTP"),
                   "ZLST": mi.inData.get("ZLST"),
                   "TEAM": mi.inData.get("TEAM"),
                   "PICK": mi.inData.get("PICK"),
                   "DLIX": mi.inData.get("DLIX"),
                   "PLSX": mi.inData.get("PLSX"),
                   "PISE": mi.inData.get("PISE"),
                   "ZXPN": mi.inData.get("ZXPN"),
                   "ZXPL": mi.inData.get("ZXPL"),
                   "ROUT": mi.inData.get("ROUT"),
                   "RODN": mi.inData.get("RODN"),
                   "CONN": mi.inData.get("CONN"),
                   "PLRI": mi.inData.get("PLRI"),
                   "FDDT": mi.inData.get("ZFDD"),
                   "FDHM": mi.inData.get("ZFDH"),
                   "TDDT": mi.inData.get("ZTDD"),
                   "TDHM": mi.inData.get("ZTDH"),
                   "CONA": mi.inData.get("CONA"),
                   "FPAS": mi.inData.get("ZFPA"),
                   "TPAS": mi.inData.get("ZTPA"),
                   "ZPIS": mi.inData.get("ZPIS"),
                   "ZORC": mi.inData.get("ZORC"),
                   "ZPAS": mi.inData.get("ZPAS"),
                   "ZISE": mi.inData.get("ZISE"),
                   "CLPI": mi.inData.get("ZCPI"),
                   "CLPA": mi.inData.get("ZCPA"),
                   "CWV1": mi.inData.get("ZCW1"),
                   "CWV2": mi.inData.get("ZCW2"),
                   "XPI0": mi.inData.get("ZPI0"),
                   "XPA0": mi.inData.get("ZPA0"),
                   "FLPI": mi.inData.get("ZFLI"),
                   "TLPI": mi.inData.get("ZTLI"),
                   "FWPI": mi.inData.get("ZFWI"),
                   "TWPI": mi.inData.get("ZTWI"),
                   "FVPI": mi.inData.get("ZFVI"),
                   "TVPI": mi.inData.get("ZTVI"),
                   "FLPA": mi.inData.get("ZFLA"),
                   "TLPA": mi.inData.get("ZTLA"),
                   "FWPA": mi.inData.get("ZFWA"),
                   "TWPA": mi.inData.get("ZTWA"),
                   "FVPA": mi.inData.get("ZFVA"),
                   "TVPA": mi.inData.get("ZTVA"),
                   "MLPI": mi.inData.get("ZMLI"),
                   "MLPA": mi.inData.get("ZMLA"),
                   "RIDN": mi.inData.get("RIDN")
    ];
    //.error("${params}");
    miCaller.call("MWS420MI", "LstPickersPL", params, callback);
  }

  /**
   * Retrieve delivery address name
   * @param
   * @return deliveryName
   */
  String getDeliveryAddressName(String orderNo, Map<String, String> params) {
    String deliveryAddress = "";
    DBAction query = database.table("OOHEAD")
      .index("00")
      .selection("OAORNO","OACUNO","OAADID").build();
    DBContainer OOHEAD = query.getContainer();
    OOHEAD.set("OACONO", program.LDAZD.get("CONO"));
    OOHEAD.set("OAORNO", orderNo);
    if(query.read(OOHEAD)){
      int iter = 0;
      Closure<?> OOADRECallback = {
        DBContainer data->
          if (iter > 1) {
            return;
          }
          iter = iter + 1;
          deliveryAddress = data.get("ODCUNM");
          logger.debug("XtendM3Debug_LstPickersPL: ${orderNo} OOADRE ${deliveryAddress}");
      }

      query = database.table("OOADRE")
        .index("00")
        .selection("ODCUNM").build();
      DBContainer OOADRE = query.getContainer();
      OOADRE.set("ODCONO", OOHEAD.get("OACONO"));
      OOADRE.set("ODORNO", OOHEAD.get("OAORNO"));
      OOADRE.set("ODADRT", 1);

      if(query.readAll(OOADRE, 3, OOADRECallback)){
        return deliveryAddress;
      }

      query = database.table("OCUSAD")
        .index("00")
        .selection("OPCUNM").build();
      DBContainer OCUSAD = query.getContainer();
      OCUSAD.set("OPCONO", OOHEAD.get("OACONO"));
      OCUSAD.set("OPCUNO", OOHEAD.get("OACUNO"));
      OCUSAD.set("OPADRT", 1);
      OCUSAD.set("OPADID", OOHEAD.get("OAADID"));
      if(query.read(OCUSAD)){
        deliveryAddress = OCUSAD.get("OPCUNM");
        logger.debug("XtendM3Debug_LstPickersPL: ${orderNo} OCUSAD ${deliveryAddress}");
        return deliveryAddress;
      }

      logger.debug("XtendM3Debug_LstPickersPL: ${orderNo} CONO=${OOHEAD.get('OACONO')} LDAZD=${params['CONO'].toInteger()} CUNO=${OOHEAD.get('OACUNO')} ${params['CUNO']}");
      if (OOHEAD.get("OACONO") != params["CONO"].toInteger() || OOHEAD.get("OACUNO") != params["CUNO"]) {
        query = database.table("OCUSMA")
          .index("00")
          .selection("OKCUNM").build();
        DBContainer OCUSMA = query.getContainer();
        OCUSMA.set("OKCONO", OOHEAD.get("OACONO"));
        OCUSMA.set("OKCUNO", OOHEAD.get("OKCUNO"));
        if(query.read(OCUSMA)){
          deliveryAddress = OCUSMA.get("OKCUNM");
          return deliveryAddress;
        }
      }
    }
    return deliveryAddress;
  }
}
