/**
 * EXT422MI.SelPickDetail
 * Extension api for MWS422MI.SelPickDetail
 * Date	    Changed By  Description
 * 20200820	JEACAR01    Add ZMBD - Minimum best before days
 */
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Map
public class SelPickDetail extends ExtendM3Transaction {
  private final MIAPI mi
  private final DatabaseAPI database
  private final MICallerAPI miCaller
  private final LoggerAPI logger
  private final ProgramAPI program

  private final String dfmt = "yyyyMMdd"

  public SelPickDetail(MIAPI mi, DatabaseAPI database, MICallerAPI miCaller, LoggerAPI logger, ProgramAPI program) {
    this.mi = mi
    this.database = database
    this.miCaller = miCaller
    this.logger = logger
    this.program = program
  }

  public void main() {


    def callback = {
      Map <String, String> out ->

        if (out.error != null) {
          mi.error(out.errorMessage)
          return
        }

        int minBestBeforeDate = getMinBestBeforeDate(out)

        mi.outData.put("CONO", out.get("CONO").trim())
        mi.outData.put("WHLO", out.get("WHLO").trim())
        mi.outData.put("ITNO", out.get("ITNO").trim())
        mi.outData.put("WHSL", out.get("WHSL").trim())
        mi.outData.put("BANO", out.get("BANO").trim())
        mi.outData.put("CAMU", out.get("CAMU").trim())
        mi.outData.put("REPN", out.get("REPN").trim())
        mi.outData.put("TTYP", out.get("TTYP").trim())
        mi.outData.put("RIDN", out.get("RIDN").trim())
        mi.outData.put("RIDO", out.get("RIDO").trim())
        mi.outData.put("RIDI", out.get("RIDI").trim())
        mi.outData.put("PLSX", out.get("PLSX").trim())
        mi.outData.put("RIDL", out.get("RIDL").trim())
        mi.outData.put("STAT", out.get("STAT").trim())
        mi.outData.put("SLTP", out.get("SLTP").trim())
        mi.outData.put("PISE", out.get("PISE").trim())
        mi.outData.put("ALQT", out.get("ALQT").trim())
        mi.outData.put("SORT", out.get("SORT").trim())
        mi.outData.put("TRFL", out.get("TRFL").trim())
        mi.outData.put("MAAL", out.get("MAAL").trim())
        mi.outData.put("RFTX", out.get("RFTX").trim())
        mi.outData.put("PGNM", out.get("PGNM").trim())
        mi.outData.put("PLPR", out.get("PLPR").trim())
        mi.outData.put("PAQT", out.get("PAQT").trim())
        mi.outData.put("CAWE", out.get("CAWE").trim())
        mi.outData.put("FLOC", out.get("FLOC").trim())
        mi.outData.put("ZDEV", out.get("DEV").trim())
        mi.outData.put("STCD", out.get("STCD").trim())
        mi.outData.put("SOFT", out.get("SOFT").trim())
        mi.outData.put("TRQT", out.get("TRQT").trim())
        mi.outData.put("TWSL", out.get("TWSL").trim())
        mi.outData.put("ZTWL", out.get("TWS2").trim())
        mi.outData.put("OEND", out.get("OEND").trim())
        mi.outData.put("PLRI", out.get("PLRI").trim())
        mi.outData.put("TRQA", out.get("TRQA").trim())
        mi.outData.put("ALQA", out.get("ALQA").trim())
        mi.outData.put("ALQN", out.get("ALQN").trim())
        mi.outData.put("PAQA", out.get("PAQA").trim())
        mi.outData.put("CNNR", out.get("CNNR").trim())
        mi.outData.put("LSQN", out.get("LSQN").trim())
        mi.outData.put("SSEQ", out.get("SSEQ").trim())
        mi.outData.put("PLRN", out.get("PLRN").trim())
        mi.outData.put("RGDT", out.get("RGDT").trim())
        mi.outData.put("RGTM", out.get("RGTM").trim())
        mi.outData.put("LMDT", out.get("LMDT").trim())
        mi.outData.put("CHNO", out.get("CHNO").trim())
        mi.outData.put("CHID", out.get("CHID").trim())
        mi.outData.put("RIDX", out.get("RIDX").trim())
        mi.outData.put("ITDS", out.get("ITDS").trim())
        mi.outData.put("CONA", out.get("CONA").trim())
        mi.outData.put("UNMS", out.get("UNMS").trim())
        mi.outData.put("DCCD", out.get("DCCD").trim())
        mi.outData.put("DPLO", out.get("DPLO").trim())
        mi.outData.put("DDLO", out.get("DDLO").trim())
        mi.outData.put("CUNO", out.get("CUNO").trim())
        mi.outData.put("CUNM", out.get("CUNM").trim())
        mi.outData.put("BACD", out.get("BACD").trim())
        mi.outData.put("INDI", out.get("INDI").trim())
        mi.outData.put("COMG", out.get("COMG").trim())
        mi.outData.put("LOBL", out.get("LOBL").trim())
        mi.outData.put("LODO", out.get("LODO").trim())
        mi.outData.put("ACTI", out.get("ACTI").trim())
        mi.outData.put("SCRQ", out.get("SCRQ").trim())
        mi.outData.put("GENE", out.get("GENE").trim())
        mi.outData.put("ZLON", out.get("LGON").trim())
        mi.outData.put("AUCZ", out.get("AUCZ").trim())
        mi.outData.put("ORRN", out.get("ORRN").trim())
        mi.outData.put("ZWPI", out.get("WTPI").trim())
        mi.outData.put("ZVPI", out.get("VTPI").trim())
        mi.outData.put("ZWPA", out.get("WTPA").trim())
        mi.outData.put("ZVPA", out.get("VTPA").trim())
        mi.outData.put("PRIO", out.get("PRIO").trim())
        mi.outData.put("CWUN", out.get("CWUN").trim())
        mi.outData.put("PANR", out.get("PANR").trim())
        mi.outData.put("MULS", out.get("MULS").trim())
        mi.outData.put("SULS", out.get("SULS").trim())
        mi.outData.put("SEEQ", out.get("SEEQ").trim())
        mi.outData.put("DSDT", out.get("DSDT").trim())
        mi.outData.put("DSHM", out.get("DSHM").trim())
        mi.outData.put("CONN", out.get("CONN").trim())
        mi.outData.put("ROUT", out.get("ROUT").trim())
        mi.outData.put("RODN", out.get("RODN").trim())
        mi.outData.put("MODL", out.get("MODL").trim())
        mi.outData.put("MODF", out.get("MODF").trim())
        mi.outData.put("RORC", out.get("RORC").trim())
        mi.outData.put("ISEG", out.get("ISEG").trim())
        mi.outData.put("ABFC", out.get("ABFC").trim())
        mi.outData.put("ALUN", out.get("ALUN").trim())
        mi.outData.put("GRWE", out.get("GRWE").trim())
        mi.outData.put("NEWE", out.get("NEWE").trim())
        mi.outData.put("VOL3", out.get("VOL3").trim())
        mi.outData.put("FCU1", out.get("FCU1").trim())
        mi.outData.put("FRAG", out.get("FRAG").trim())
        mi.outData.put("ILEN", out.get("ILEN").trim())
        mi.outData.put("IWID", out.get("IWID").trim())
        mi.outData.put("IHEI", out.get("IHEI").trim())
        mi.outData.put("DIM1", out.get("DIM1").trim())
        mi.outData.put("DIM2", out.get("DIM2").trim())
        mi.outData.put("DIM3", out.get("DIM3").trim())
        mi.outData.put("RSVL", out.get("RSVL").trim())
        mi.outData.put("RSVB", out.get("RSVB").trim())
        mi.outData.put("RSVC", out.get("RSVC").trim())
        if (minBestBeforeDate == 0) {
          mi.outData.put("ZMBD", "")
        } else {
          mi.outData.put("ZMBD", minBestBeforeDate.toString())
        }
        mi.write()
    }


    def params = [ "DLIX": mi.inData.get("DLIX").trim(),
                   "PLSX": mi.inData.get("PLSX").trim(),
                   "PANR": mi.inData.get("PANR").trim(),
                   "SSCC": mi.inData.get("SSCC").trim(),
                   "ITNO": mi.inData.get("ITNO").trim(),
                   "WHSL": mi.inData.get("WHSL").trim(),
                   "BANO": mi.inData.get("BANO").trim(),
                   "CAMU": mi.inData.get("CAMU").trim(),
                   "PLRN": mi.inData.get("PLRN").trim(),
                   "ISTS": mi.inData.get("ZITS").trim(),
                   "ISTH": mi.inData.get("ZISH").trim(),
                   "IRPK": mi.inData.get("ZIRP").trim(),
                   "CWV1": mi.inData.get("ZCW1").trim(),
                   "CWV2": mi.inData.get("ZCW2").trim(),
                   "TL40": mi.inData.get("ZL40").trim(),
                   "TL50": mi.inData.get("ZL50").trim(),
                   "TL60": mi.inData.get("ZL60").trim(),
                   "CONP": mi.inData.get("ZCOP").trim(),
    ]

    miCaller.call("MWS422MI", "SelPickDetail", params, callback)
  }

  /**
   * Retrieve minimum best before date
   * @param MWS422MI.SelPickDetail
   * @return date yyyyMMdd
   */
  int getMinBestBeforeDate(Map<String, String> params) {
    int bestDate = 0
    int cono = program.LDAZD.get("CONO")
    String ridn =  params.get("RIDN").trim()
    int ridl = params.get("RIDL").trim().toInteger()
    int ridx = params.get("RIDX").trim().toInteger()
    int ttyp = params.get("TTYP").trim().toInteger()

    logger.debug("XtendM3Debug_SelPickDetail: CONO ${cono} RIDN ${ridn} RIDL ${ridl} RIDX ${ridx}")
    if (ttyp == 31) {
      DBAction query = database.table("OOLINE")
        .index("00")
        .selection("OBCONO","OBORNO","OBPONR","OBPOSX","OBITNO","OBCUNO","OBDWDZ").build()
      DBContainer OOLINE = query.getContainer()
      OOLINE.set("OBCONO", cono)
      OOLINE.set("OBORNO", ridn)
      OOLINE.set("OBPONR", ridl)
      OOLINE.set("OBPOSX", ridx)

      if(query.read(OOLINE)){
        query = database.table("OCUSIT")
          .index("00")
          .selection("ORADCU").build()
        DBContainer OCUSIT = query.getContainer()
        OCUSIT.set("ORCONO", OOLINE.getInt("OBCONO"))
        OCUSIT.set("ORCUNO", OOLINE.getString("OBCUNO"))
        OCUSIT.set("ORITNO", OOLINE.getString("OBITNO"))
        if(query.read(OCUSIT)){
          LocalDate date = LocalDate.parse(OOLINE.getInt("OBDWDZ").toString(), DateTimeFormatter.ofPattern(dfmt))
          date = date.plusDays(OCUSIT.getInt("ORADCU"))
          bestDate = date.format(DateTimeFormatter.ofPattern(dfmt)).toInteger()

          logger.debug("XtendM3Debug_SelPickDetail: OCUSIT ${bestDate}")
        }
      }
    } else if (ttyp == 41 || ttyp == 51 || ttyp == 92) {
      DBAction query = database.table("MGLINE")
        .index("00")
        .selection("MRCONO","MRTRNR","MRPONR","MRPOSX","MRWHLO","MRITNO","MRTRDT").build()
      DBContainer MGLINE = query.getContainer()
      MGLINE.set("MRCONO", cono)
      MGLINE.set("MRTRNR", ridn)
      MGLINE.set("MRPONR", ridl)
      MGLINE.set("MRPOSX", ridx)

      if(query.read(MGLINE)){
        query = database.table("MITBAL")
          .index("00")
          .selection("MBCONO","MBWHLO","MBITNO","MBREOP").build()
        DBContainer MITBAL = query.getContainer()
        MITBAL.set("MBCONO", MGLINE.getInt("MRCONO"))
        MITBAL.set("MBWHLO", MGLINE.getString("MRWHLO"))
        MITBAL.set("MBITNO", MGLINE.getString("MRITNO"))
        if(query.read(MITBAL)){
          LocalDate date = LocalDate.parse(MGLINE.getInt("MRTRDT").toString(), DateTimeFormatter.ofPattern(dfmt))
          date = date.plusDays(MITBAL.getDouble("MBREOP").toInteger())

          bestDate = date.format(DateTimeFormatter.ofPattern(dfmt)).toInteger()

          logger.debug("XtendM3Debug_SelPickDetail: MITBAL ${bestDate}")
        }
      }
    }

    return bestDate
  }
}
