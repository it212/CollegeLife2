package com.it212.collegelife.domain;

/**
 * Created by imac on 2017/4/5.
 */

public class GroupInfo {
    private String GroupName;
    private String GroupId;
    private String invatePerson;

    public String getGroupname() {
        return GroupName;
    }

    public void setGroupname(String groupname) {
        GroupName = groupname;
    }

    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String groupId) {
        GroupId = groupId;
    }

    public String getInvatePerson() {
        return invatePerson;
    }

    public void setInvatePerson(String invatePerson) {
        this.invatePerson = invatePerson;
    }
}
