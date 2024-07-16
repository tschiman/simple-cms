package com.gettimhired.simplecms.service;

import com.gettimhired.simplecms.model.dto.ContactDTO;
import com.gettimhired.simplecms.model.dto.ContactFormDto;
import com.gettimhired.simplecms.model.mongo.Contact;
import com.gettimhired.simplecms.model.mongo.ContactStatus;
import com.gettimhired.simplecms.repository.ContactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @InjectMocks
    private ContactService contactService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() {
        ContactFormDto contactFormDto = new ContactFormDto("John Doe", "1234567890", "john.doe@example.com", "Hello");

        contactService.save(contactFormDto);

        verify(contactRepository, times(1)).save(any(Contact.class));
    }

    @Test
    public void testFindAllContacts() {
        Contact contact1 = new Contact(UUID.randomUUID().toString(), "John Doe", "1234567890", "john.doe@example.com", "Hello", ContactStatus.NEW);
        Contact contact2 = new Contact(UUID.randomUUID().toString(), "Jane Smith", "0987654321", "jane.smith@example.com", "Hi", ContactStatus.NEW);
        when(contactRepository.findAll()).thenReturn(List.of(contact1, contact2));

        List<ContactDTO> contacts = contactService.findAllContacts();

        assertEquals(2, contacts.size());
        assertEquals(contact1.id(), contacts.get(0).id());
        assertEquals(contact2.id(), contacts.get(1).id());
        verify(contactRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateContactStatus() {
        Contact contact = new Contact(UUID.randomUUID().toString(), "John Doe", "1234567890", "john.doe@example.com", "Hello", ContactStatus.NEW);
        when(contactRepository.findById(contact.id())).thenReturn(Optional.of(contact));

        contactService.updateContactStatus(contact.id(), ContactStatus.READ);

        verify(contactRepository, times(1)).findById(contact.id());
        verify(contactRepository, times(1)).save(any(Contact.class));

        // Verify the saved contact has the updated status
        Contact updatedContact = new Contact(contact.id(), contact.name(), contact.phoneNumber(), contact.email(), contact.message(), ContactStatus.READ);
        verify(contactRepository).save(refEq(updatedContact));
    }

    @Test
    public void testUpdateContactStatus_NotFound() {
        when(contactRepository.findById("1")).thenReturn(Optional.empty());

        contactService.updateContactStatus("1", ContactStatus.READ);

        verify(contactRepository, times(1)).findById("1");
        verify(contactRepository, times(0)).save(any(Contact.class));
    }
}