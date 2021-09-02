package one.digitalinnovation.personapi.service;

import one.digitalinnovation.personapi.dto.response.MessageResponseDTO;
import one.digitalinnovation.personapi.entity.Person;
import one.digitalinnovation.personapi.exception.PersonNotFoundException;
import one.digitalinnovation.personapi.mapper.PersonMapper;
import one.digitalinnovation.personapi.repository.PersonRepository;
import one.digitalinnovation.personapi.request.PersonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private PersonRepository personRepository;
    private final PersonMapper personMapper = PersonMapper.INSTANCE;

    @Autowired
    public PersonService(PersonRepository personRepository){
        this.personRepository = personRepository;
    }

    public MessageResponseDTO createPerson(PersonDTO personDTO) {
        Person personToSave = personMapper.toModel(personDTO);

        Person savedPerson = personRepository.save(personToSave);
        return getMessageResponseDTO(savedPerson.getId(), "Created Person with id");
    }

    public List<PersonDTO> listAll() {
        List<Person> list = personRepository.findAll();

        return list.stream().map(personMapper::toDTO).collect(Collectors.toList());

    }

    public PersonDTO getById(long id) throws PersonNotFoundException{
        Person person = verifyById(id);

        return personMapper.toDTO(person);
    }

    public void deleteById(long id) throws PersonNotFoundException {
        verifyById(id);

        personRepository.deleteById(id);
    }

    public MessageResponseDTO updateById(PersonDTO personDTO, long id) throws PersonNotFoundException {
        verifyById(id);

        Person personToUpdate = personMapper.toModel(personDTO);

        Person updatedPerson = personRepository.save(personToUpdate);
        return getMessageResponseDTO(updatedPerson.getId(), "Updated Person with id");
    }


    private Person verifyById(long id) throws PersonNotFoundException {
        return personRepository.findById(id).
                orElseThrow(() -> new PersonNotFoundException(id));
    }

    private MessageResponseDTO getMessageResponseDTO(long id, String s) {
        return MessageResponseDTO.
                builder().
                message(s + id).
                build();
    }
}
