package com.frioui.patient;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import javassist.NotFoundException;

import org.springframework.http.HttpStatus;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/patient")
public class PatientRecordController {
    @Autowired PatientRecordRepository patientRecordRepository;
    // CRUD methods to be added
    
    @GetMapping
    public List<PatientRecord> getAllRecords() {
        return patientRecordRepository.findAll();
    }

    @GetMapping(value = "{patientId}")
    public PatientRecord getPatientById(@PathVariable(value="patientId") Long patientId) {
        return patientRecordRepository.findById(patientId).get();
    }
    
    @PostMapping
    public PatientRecord createRecord(@RequestBody @Valid PatientRecord patientRecord) { 
    	/*L'annotation @Valid garantit que toutes les contraintes dans la base de données et dans la classe d'entité sont vérifiées avant que
    	 *  les données ne soient manipulées.*/
        return patientRecordRepository.save(patientRecord);
    }
    /* Avant de passer aux autres méthodes de requête, créons une seule exception générale pour toutes les exceptions rencontrées dans la base de code
     *  et appelons-la InvalidRequestException. Pour le code d'état, utilisons le code d'état BAD_REQUEST 400.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    class InvalidRequestException extends RuntimeException {
        public InvalidRequestException(String s) {
            super(s);
        }
    }
    
    
    @PutMapping
    public PatientRecord updatePatientRecord(@RequestBody PatientRecord patientRecord) throws NotFoundException {
        if (patientRecord == null || patientRecord.getPatientId() == null) {
            throw new InvalidRequestException("PatientRecord or ID must not be null!");
        }
        Optional<PatientRecord> optionalRecord = patientRecordRepository.findById(patientRecord.getPatientId());
        if (optionalRecord.isEmpty()) {
            throw new NotFoundException("Patient with ID " + patientRecord.getPatientId() + " does not exist.");
        }
        PatientRecord existingPatientRecord = optionalRecord.get();

        existingPatientRecord.setName(patientRecord.getName());
        existingPatientRecord.setAge(patientRecord.getAge());
        existingPatientRecord.setAddress(patientRecord.getAddress());
    	
        return patientRecordRepository.save(existingPatientRecord);
    }
    /* Pour gérer les mises à jour - pour la méthode PUT, annotons-la avec un @PutMapping et exigeons un paramètre annoté par @RequestBody qui contient 
     * le PatientRecord mis à jour, similaire au mappage POST. Nous voulons nous assurer que l'enregistrement existe à des fins de validation
     *  en utilisant le patientId. Puisqu'il s'agit d'une demande PUT, l'enregistrement à mettre à jour doit exister dans la base de données, 
     *  sinon il s'agit d'une demande invalide. En outre, lancez une InvalidRequestException si le corps de la requête ou le champ patientId est nul*/
    
    @DeleteMapping(value = "{patientId}")
    public void deletePatientById(@PathVariable(value = "patientId") Long patientId) throws NotFoundException {
        if (patientRecordRepository.findById(patientId).isEmpty()) {
            throw new NotFoundException("Patient with ID " + patientId + " does not exist.");
        }
        patientRecordRepository.deleteById(patientId);
    }
    /*La méthode renverra une exception et un code d'état 400 si le patient n'existe pas. Comme la méthode GET qui récupère un patient par ID,
     *  ajoutez une propriété value à l'annotation @DeleteMapping, ainsi que le @PathVariable */
    
    
}
