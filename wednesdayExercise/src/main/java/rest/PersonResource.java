package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.PersonDTO;
import entities.Person;
import exceptions.PersonNotFoundException;
import utils.EMF_Creator;
import facades.PersonFacade;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

//Todo Remove or change relevant parts before ACTUAL use
@Path("person")
public class PersonResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    private static final PersonFacade FACADE = PersonFacade.getFacadeExample(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }

    @Path("/all")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllPersons() throws Throwable {

        List<PersonDTO> personDTO = new ArrayList();
        List<Person> persons = FACADE.getAllPersons();

        for (Person person : persons) {
            personDTO.add(new PersonDTO(person));
        }

        return GSON.toJson(personDTO);

    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public String getPerson(@PathParam("id") long id) throws Throwable {

        Person person = FACADE.getPerson(id);
        PersonDTO personDTO = new PersonDTO(person);
        return GSON.toJson(personDTO);

    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String addPerson(String personAsJSON) throws Throwable {

        Person newPerson = GSON.fromJson(personAsJSON, Person.class);
        Person addPerson = FACADE.addPerson(newPerson.getFirstname(), newPerson.getLastname(), newPerson.getPhone());
        PersonDTO pDTO = new PersonDTO(addPerson);
        return GSON.toJson(pDTO);

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/edit/{id}")
    public String editPerson(String personAsJSON,
            @PathParam("id") long id) throws Throwable {

        try {
            Person oldPerson = FACADE.getPerson(id);
            Person newPerson = GSON.fromJson(personAsJSON, Person.class);

            oldPerson.setFirstname(newPerson.getFirstname());
            oldPerson.setLastname(newPerson.getLastname());
            oldPerson.setPhone(newPerson.getPhone());

            Person person = FACADE.editPerson(oldPerson);
            PersonDTO personDTO = new PersonDTO(person);

            return GSON.toJson(personDTO);
        } catch (PersonNotFoundException ex) {
            throw new PersonNotFoundException("Could not edit person, the provided Id not found");
        }

    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/delete/{id}")
    public String deletePerson(@PathParam("id") long id) throws Throwable {

        try {
            Person person = FACADE.getPerson(id);
            FACADE.deletePerson(id);

            PersonDTO personDTO = new PersonDTO(person);

            return GSON.toJson(personDTO);
        } catch (PersonNotFoundException ex) {
            throw new PersonNotFoundException("Could not delete person, the provided Id not found");
        }
    }
}
