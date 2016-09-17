package ch.hftm.wtm.business;

import java.util.Calendar;

import org.apache.log4j.Logger;

import ch.hftm.wtm.model.MasterData;
import ch.hftm.wtm.model.Person;
import ch.hftm.wtm.persistence.MasterDataPersistence;

public class ManageMetaDataBusiness {

    protected Logger logger = Logger.getLogger(getClass());

    /**
     * Submit the Changes made on an MasterData
     *
     * @param md MasterData object to update/create
     * @param isNew Indicates whether the object is new or already existing
     */
    public void submitChanges(MasterData md, boolean isNew) {
        MasterDataPersistence mepp = new MasterDataPersistence();
        if (isNew) {
            mepp.getEntityManager().getTransaction().begin();
            mepp.makePersistent(md);
            mepp.getEntityManager().getTransaction().commit();
        } else {
            System.out.println("Submitchanges ep Id: " + md.getId());
            MasterData dbMasterData = mepp.getMasterData(md);

            if (dbMasterData.getDefaultAnnualVacationDays() != md.getDefaultAnnualVacationDays() ||
                    dbMasterData.getDefaultDailyWorkingHours() != md.getDefaultDailyWorkingHours() ||
                    dbMasterData.getDefaultOvertimeMaximum() != md.getDefaultOvertimeMaximum()) {
                logger.info("MasterData changed, creating new MasterData");
                generateNewMasterData(md, dbMasterData);
                deactivateOldMasterData(dbMasterData);
            } else
                System.out.println("else");
        }
    }

    /**
     * Generates a new EmploymentPeriod based on the old and the new EmploymentPeriods
     *
     * @param newMd an Object with the new Values.
     * @param oldMd an Object with the old Values.
     */
    private void generateNewMasterData(MasterData newMd, MasterData oldMd) {
        MasterData mdToPersist = new MasterData();
        mdToPersist.setActive(true);
        mdToPersist.setDefaultOvertimeMaximum(newMd.getDefaultOvertimeMaximum());
        mdToPersist.setDefaultAnnualVacationDays(newMd.getDefaultAnnualVacationDays());
        mdToPersist.setDefaultDailyWorkingHours(newMd.getDefaultDailyWorkingHours());
        mdToPersist.setBackwardEntryModification(newMd.getBackwardEntryModification());
        mdToPersist.setStartFiscalYear(newMd.getStartFiscalYear());
        mdToPersist.setEndFiscalYear(newMd.getEndFiscalYear());
        mdToPersist.setValidFromDate(Calendar.getInstance().getTime());

        mdToPersist.setClient(oldMd.getClient());
        mdToPersist.setValidToDate(Calendar.getInstance().getTime());

        MasterDataPersistence mdp = new MasterDataPersistence();
        mdp.persistMasterData(mdToPersist);
    }

    /**
     * deactivates an MasterData object
     *
     * @param ep MasterData object to deactivate.
     */
    private void deactivateOldMasterData(MasterData md) {
        MasterDataPersistence mepp = new MasterDataPersistence();
        mepp.deactivateMasterData(md);
    }

    public MasterData getActiveMasterData(Person p) {
        MasterDataPersistence pp = new MasterDataPersistence();

        return pp.findActiveMasterData(p);
    }
}
