package ch.hftm.wtm.presentation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import ch.hftm.wtm.business.PublicHolidaysBusiness;
import ch.hftm.wtm.model.Client;
import ch.hftm.wtm.model.PublicHoliday;
import ch.hftm.wtm.model.Region;

@SessionScoped
@ManagedBean(name = "publicHolidaysBean")
public class PublicHolidaysBean {
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

    PublicHolidaysBusiness phb = new PublicHolidaysBusiness();

    private List<Region> allRegions;
    private Set<Region> selectedRegions;
    private Region singleRegion;

    private Region newRegion;
    private PublicHoliday newPublicHoliday;
    private List<PublicHoliday> allPublicHolidays;

    // indicated wheter or not the currently selected PublicHoliday object is new or already existing.
    private boolean submittedPublicHolidayIsNew = true;

    public List<PublicHoliday> getAllPublicHolidays() {
        return allPublicHolidays;
    }

    public void setAllPublicHolidays(List<PublicHoliday> allPublicHolidays) {
        this.allPublicHolidays = allPublicHolidays;
    }

    public Region getSingleRegion() {
        return singleRegion;
    }

    public void setSingleRegion(Region singleRegion) {
        this.singleRegion = singleRegion;
    }

    public void setAllRegions(List<Region> allRegions) {
        this.allRegions = allRegions;
    }

    public List<Region> getAllRegions() {
        return allRegions;
    }

    public void setNewPublicHoliday(PublicHoliday publicHoliday) {
        this.newPublicHoliday = publicHoliday;
    }

    public PublicHoliday getNewPublicHoliday() {
        return newPublicHoliday;
    }

    public void setLoginBean(LoginBean lb) {
        this.loginBean = lb;
    }

    public Region getNewRegion() {
        return newRegion;
    }

    public void setNewRegion(Region newRegion) {
        this.newRegion = newRegion;
    }

    public Set<Region> getSelectedRegions() {
        return selectedRegions;
    }

    public void setSelectedRegions(Set<Region> selectedRegions) {
        this.selectedRegions = selectedRegions;
    }

    /**
     * called on load, doing some initial work.
     * 
     * @return returns null
     */
    public String onLoad() {
        System.out.println("PublicHolidaysBean onLoad");
        submittedPublicHolidayIsNew = true;

        phb.loadPersonalData(loginBean.getLoggedInPerson());

        resetNewPublicHoliday();
        loadPublicHolidays();
        loadRegions();

        return null;
    }

    /**
     * resets the currently set publicHoliday object
     */
    private void resetNewPublicHoliday() {
        newPublicHoliday = new PublicHoliday();
        newPublicHoliday.setAppliedRegions(new HashSet<Region>());
    }

    /**
     * button-click event. Sets the selected PublicHoliday object
     * 
     * @param ph selected PublicHoliday object.
     */
    public void updatePublicHolidayBtn(PublicHoliday ph) {
        submittedPublicHolidayIsNew = false;
        newPublicHoliday = ph;

        if (selectedRegions == null)
            selectedRegions = new HashSet<Region>();
        else
            selectedRegions.clear();

        selectedRegions.addAll(ph.getAppliedRegions());
    }

    /**
     * loads all PublicHoliday Objects from the currently logged user's client.
     */
    private void loadPublicHolidays() {
        if (allPublicHolidays == null)
            allPublicHolidays = new ArrayList<PublicHoliday>();

        PublicHolidaysBusiness phb = new PublicHolidaysBusiness();

        Client c = loginBean.getLoggedInPerson().getClient();
        allPublicHolidays = phb.getAllPublicHolidays(c);

        return;
    }

    /**
     * load all regions from the currently logged in user's client.
     */
    private void loadRegions() {
        newRegion = new Region();

        if (allRegions == null)
            allRegions = new ArrayList<>();
        else
            allRegions.clear();

        List<Region> regions = phb.getAllRegions(loginBean.getLoggedInPerson().getClient());
        allRegions.addAll(regions);
    }

    /**
     * Submits the current PublicHoliday object. Depending on the status, it's a new object or an existing one.
     */
    public void submit() {
        if (submittedPublicHolidayIsNew)
            submitNewPublicHoliday();
        else
            submitPublicHolidayChanges();

        resetView();
    }

    /**
     * Submits the changes made to the currently selected PublicHoliday object.
     */
    private void submitPublicHolidayChanges() {
        System.out.println("Submit changes.");

        if (newPublicHoliday.getClient() == null) {
            Client c = loginBean.getLoggedInPerson().getClient();
            newPublicHoliday.setClient(c);
        }

        if (selectedRegions.size() == 0) {
            System.out.println("Keine Regionen zugewiesen, es wurden keine Änderungen vorgenommen");
            return;
        }

        newPublicHoliday.setAppliedRegions(selectedRegions);
        System.out.println("Selected Regions Size: " + newPublicHoliday.getAppliedRegions().size());
        phb.updatePublicHoliday(newPublicHoliday);
    }

    /**
     * Submits a new PublicHoliday object to the database.
     */
    private void submitNewPublicHoliday() {
        System.out.println("Submit new PublicHoliday");

        Client c = loginBean.getLoggedInPerson().getClient();
        newPublicHoliday.setClient(c);
        newPublicHoliday.setAppliedRegions(selectedRegions);

        phb.persistPublicHoliday(newPublicHoliday);
    }

    /**
     * resets all the objects on the view.
     */
    private void resetView() {
        loadPublicHolidays();
        loadRegions();

        newPublicHoliday = phb.getPublicHoliday(newPublicHoliday.getId());

        submittedPublicHolidayIsNew = true;
    }

    /**
     * Delets an existing Region item from the database.
     * 
     * @param item The item do delete.
     */
    public void delete(Region item) {
        phb.deleteRegion(item);
        allRegions.remove(item);
    }

    /**
     * Submits a new Region object to the database.
     */
    public void submitNewRegion() {
        phb.persistRegion(newRegion);

        loadRegions();
    }
}
