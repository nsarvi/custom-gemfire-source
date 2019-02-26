package io.pivotal.datatx.lucene.model;

import java.io.Serializable;
import java.util.Arrays;

public class Contact  implements Serializable {

    private String name;
    private String[] phoneNumbers;

    public Contact(String name, String[] phoneNumbers) {
        this.name = name;
        this.phoneNumbers = phoneNumbers;
    }

    public String getName() {
        return this.name;
    }

    public String[] getPhones() {
        return this.phoneNumbers;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(String[] phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    @Override
    public String toString() {
        return "(name=" + name + ", phones=" + Arrays.toString(phoneNumbers) + ")";
    }
}
