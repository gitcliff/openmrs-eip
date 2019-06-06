package org.openmrs.sync.core.service.facade;

import org.openmrs.sync.core.model.BaseModel;
import org.openmrs.sync.core.service.EntityNameEnum;
import org.openmrs.sync.core.entity.Patient;
import org.openmrs.sync.core.entity.Person;
import org.openmrs.sync.core.model.PatientModel;
import org.openmrs.sync.core.model.PersonModel;
import org.openmrs.sync.core.service.AbstractEntityService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EntityServiceFacadeTest {

    @Mock
    private AbstractEntityService<Person, PersonModel> personService;

    @Mock
    private AbstractEntityService<Patient, PatientModel> patientService;

    private EntityServiceFacade facade;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        facade = new EntityServiceFacade(Arrays.asList(personService, patientService));
    }

    @Test
    public void getModels() {
        // Given
        PersonModel personModel1 = new PersonModel();
        PersonModel personModel2 = new PersonModel();
        LocalDateTime lastSyncDate = LocalDateTime.now();
        when(personService.getEntityName()).thenReturn(EntityNameEnum.PERSON);
        when(patientService.getEntityName()).thenReturn(EntityNameEnum.VISIT);
        when(personService.getModels(lastSyncDate)).thenReturn(Arrays.asList(personModel1, personModel2));

        // When
        List<? extends BaseModel> result = facade.getModels(EntityNameEnum.PERSON, lastSyncDate);

        // Then
        assertEquals(2, result.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getModels_unknown_service() {
        // Given
        LocalDateTime lastSyncDate = LocalDateTime.now();
        facade = new EntityServiceFacade(Collections.singletonList(personService));
        when(personService.getEntityName()).thenReturn(EntityNameEnum.PERSON);
        when(patientService.getEntityName()).thenReturn(EntityNameEnum.VISIT);

        // When
        facade.getModels(EntityNameEnum.VISIT, lastSyncDate);

        // Then
        // BOOM
    }

    @Test
    public void saveModel() {
        // Given
        PersonModel personModel = new PersonModel();
        when(personService.getEntityName()).thenReturn(EntityNameEnum.PERSON);

        // When
        facade.saveModel(EntityNameEnum.PERSON, personModel);

        // Then
        verify(personService).save(personModel);
    }
}