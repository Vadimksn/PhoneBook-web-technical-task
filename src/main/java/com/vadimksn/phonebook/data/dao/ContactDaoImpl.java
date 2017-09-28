package com.vadimksn.phonebook.data.dao;

import com.vadimksn.phonebook.data.entity.Contact;
import com.vadimksn.phonebook.data.entity.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Qualifier("contactDao")
public class ContactDaoImpl implements ContactDao<Contact> {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Contact addContact(Contact contact) {
        jdbcTemplate.update("BEGIN;" +
                        "INSERT INTO phone_book.contacts (name, address) VALUES (?,?);" +
                        "INSERT INTO phone_book.phones (contact_id, phone_number) VALUES (LAST_INSERT_ID(), ?);" +
                        "COMMIT;",
                contact.getName(), contact.getAddress(), contact.getNewPhoneNumber());
        return contact;
    }

    @Override
    public Contact updateAddressAddPhone(Contact contact) {
        jdbcTemplate.update("BEGIN;" +
                        "UPDATE phone_book.contacts SET address=? WHERE name=?;" +
                        "SET @contactId = (Select id FROM phone_book.contacts WHERE name=?);" +
                        "INSERT INTO phone_book.phones (contact_id, phone_number) VALUES (@contactId, ?);" +
                        "COMMIT;",
                contact.getAddress(), contact.getName(), contact.getName(), contact.getNewPhoneNumber());
        return contact;
    }

    @Override
    public void editContact(Contact contact, int contactId) {

    }

    @Override
    public void deleteContact(Contact contact) {

    }

    @Override
    public Contact findContactById(int contactId) {
        return null;
    }

    @Override
    public boolean isExistContact(Contact contact) {
        boolean exist = false;
        int count = jdbcTemplate.queryForObject("select count(*) from phone_book.contacts where name=?",
                new Object[]{contact.getName()}, Integer.class);
        if (count == 1) exist = true;
        return exist;
    }

    @Override
    public List<Contact> getListAllContacts() {
        List<Contact> contactList = jdbcTemplate.query("SELECT * FROM phone_book.contacts",
                new BeanPropertyRowMapper(Contact.class));
        List<PhoneNumber> phoneNumbers = jdbcTemplate.query("SELECT * FROM phone_book.phones",
                new BeanPropertyRowMapper(PhoneNumber.class));
        for (Contact contact : contactList) {
            if (contact.getPhoneNumbers() == null) contact.setPhoneNumbers(new ArrayList<>());
            for (PhoneNumber phoneNumber : phoneNumbers) {
                if (phoneNumber.getContactId() == contact.getId()) {
                    contact.getPhoneNumbers().add(phoneNumber);
                }
            }

        }
        return contactList;
    }
}
