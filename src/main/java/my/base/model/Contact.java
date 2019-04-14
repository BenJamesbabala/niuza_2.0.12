package my.base.model;

import java.util.LinkedList;
import java.util.List;
import my.base.parser.JSONParsable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Contact implements JSONParsable {
    private String homeAddress;
    private String homeEmail;
    private String homeFax;
    private String homePhone;
    private String jsonString = "";
    private String mobilePhone;
    private String name;
    private String root = "";
    private String workAddress;
    private String workEmail;
    private String workFax;
    private String workOrganization;
    private String workPhone;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHomePhone() {
        return this.homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getWorkPhone() {
        return this.workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public String getMobilePhone() {
        return this.mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getWorkFax() {
        return this.workFax;
    }

    public void setWorkFax(String workFax) {
        this.workFax = workFax;
    }

    public String getHomeFax() {
        return this.homeFax;
    }

    public void setHomeFax(String homeFax) {
        this.homeFax = homeFax;
    }

    public String getHomeEmail() {
        return this.homeEmail;
    }

    public void setHomeEmail(String homeEmail) {
        this.homeEmail = homeEmail;
    }

    public String getWorkEmail() {
        return this.workEmail;
    }

    public void setWorkEmail(String workEmail) {
        this.workEmail = workEmail;
    }

    public String getHomeAddress() {
        return this.homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getWorkAddress() {
        return this.workAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    public String getWorkOrganization() {
        return this.workOrganization;
    }

    public void setWorkOrganization(String workOrganization) {
        this.workOrganization = workOrganization;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public void setJSONString(String jsonString) {
        this.jsonString = jsonString;
    }

    public String getJSONString() {
        return this.jsonString;
    }

    public void decodeFromJSON(JSONObject jsonObject) throws JSONException {
        setHomeAddress(jsonObject.getString(this.root + "homeaddress"));
        setHomeEmail(jsonObject.getString(this.root + "homeemail"));
        setHomeFax(jsonObject.getString(this.root + "homefax"));
        setHomePhone(jsonObject.getString(this.root + "homephone"));
        setMobilePhone(jsonObject.getString(this.root + "mobilephone"));
        setName(jsonObject.getString(this.root + "name"));
        setWorkAddress(jsonObject.getString(this.root + "workaddress"));
        setWorkEmail(jsonObject.getString(this.root + "workemail"));
        setWorkFax(jsonObject.getString(this.root + "workfax"));
        setWorkOrganization(jsonObject.getString(this.root + "workorganization"));
        setWorkPhone(jsonObject.getString(this.root + "workphone"));
    }

    public List<JSONParsable> decodeFromJSON(JSONArray jsonArray) {
        List<JSONParsable> contactBookList = new LinkedList();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                Contact contact = new Contact();
                contact.setRoot(this.root);
                contact.decodeFromJSON(jsonArray.getJSONObject(i));
                contactBookList.add(contact);
            } catch (JSONException e) {
            }
        }
        return contactBookList;
    }
}
