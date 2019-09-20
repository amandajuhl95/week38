package facades;

import dto.PersonDTO;
import utils.EMF_Creator;
import entities.Person;
import exceptions.PersonNotFoundException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Settings;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade facade;
    private Person p1;
    private Person p2;
    private Person p3;
    private Person p4;

    public PersonFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.DROP_AND_CREATE);
        facade = PersonFacade.getFacadeExample(emf);
    }

    @AfterAll
    public static void tearDownClass() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the script below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();

        p1 = new Person("Amanda", "Hansen", "42441486");
        p2 = new Person("Laura", "Saxtrup", "20141614");
        p3 = new Person("Amalie", "Landt", "20856221");
        p4 = new Person("Jonas", "Haslund", "21534532");
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.persist(p4);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testgetPerson() throws PersonNotFoundException {

        Person person = facade.getPerson(p2.getId());
        assertEquals(person.getFirstname(), "Laura");
        assertEquals(person.getLastname(), "Saxtrup");
        assertEquals(person.getPhone(), "20141614");
    }

    @Test
    public void testgetPersonError() throws PersonNotFoundException {
        try {
            facade.getPerson(100);
            fail();
        } catch (PersonNotFoundException ex) {
            assertEquals(ex.getMessage(), "No person with provided id found");
        }
    }

    @Test
    public void testgetAllPerson() {
        List<Person> persons = facade.getAllPersons();
        assertFalse(persons.isEmpty());
        assertEquals(persons.size(), 4);

    }

    @Test
    public void testEditPerson() throws PersonNotFoundException {
        Person person = facade.getPerson(p1.getId());
        person.setFirstname("Hanne");
        facade.editPerson(person);

        Person personE = facade.getPerson(p1.getId());
        assertEquals(personE.getFirstname(), "Hanne");
    }
    
    @Test
    public void testEditPersonError() throws PersonNotFoundException {
        try {
            Person person = null;
            facade.editPerson(person);
            fail();
        } catch (PersonNotFoundException ex) {
            assertEquals(ex.getMessage(), "Person not found");
        }
    }

    @Test
    public void testAddPerson() {
        int personsbefore = facade.getAllPersons().size();
        facade.addPerson("Benjamin", "Kongshaug", "20121314");
        int personsafter = facade.getAllPersons().size();

        assertTrue(personsbefore < personsafter);
    }

    @Test
    public void testDeletePerson() throws PersonNotFoundException {
        facade.deletePerson(p1.getId());

        int personsList = facade.getAllPersons().size();
        assertEquals(personsList, 3);
    }
    
    @Test
    public void testDeletePersonError() throws PersonNotFoundException {
        try {
            facade.deletePerson(100);
            fail();
        } catch (PersonNotFoundException ex) {
            assertEquals(ex.getMessage(), "No person with provided id found");
        }
    }

}
