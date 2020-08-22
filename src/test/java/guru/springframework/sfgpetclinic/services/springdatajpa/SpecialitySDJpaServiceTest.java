package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Speciality;
import guru.springframework.sfgpetclinic.repositories.SpecialtyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.linesOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialitySDJpaServiceTest {

    @Mock
    SpecialtyRepository specialtyRepository;

    @InjectMocks
    SpecialitySDJpaService service;

    @Test
    void testDeleteByObject() {

        //given
        Speciality speciality = new Speciality();

        //when
        service.delete(speciality);

        //then
        then(specialtyRepository).should().delete(any(Speciality.class));
//        verify(specialtyRepository).delete(any(Speciality.class));
    }

//    @Test
//    void findByIdTest() {
//        Speciality speciality = new Speciality();
//        when(specialtyRepository.findById(1L)).thenReturn(Optional.of(speciality));
//
//        Speciality foundSpecialty = service.findById(1L);
//
//        assertThat(foundSpecialty).isNotNull();
//
//        verify(specialtyRepository).findById(1L);
//    }

    @Test
    void findByIdTest() {

        //given
        Speciality speciality = new Speciality();
        given(specialtyRepository.findById(1L)).willReturn(Optional.of(speciality));

        //when
        Speciality foundSpecialty = service.findById(1L);

        //then
        assertThat(foundSpecialty).isNotNull();
        then(specialtyRepository).should().findById(anyLong());
        then(speciality).shouldHaveNoMoreInteractions();

    }

    @Test
    void deleteById() {
        //given - none

        //when
        service.deleteById(1l);
        service.deleteById(1l);

        //then
        then(specialtyRepository).should(times(2)).deleteById(1L);
//        verify(specialtyRepository, times(2)).deleteById(1l);
    }

    @Test
    void deleteByIdAtLeast() {
        //given

        //when
        service.deleteById(1l);
        service.deleteById(1l);

        //then
        then(specialtyRepository).should(atLeastOnce()).deleteById(1L);
//        verify(specialtyRepository, atLeastOnce()).deleteById(1l);
    }

    @Test
    void deleteByIdAtMost() {

        //when
        service.deleteById(1l);
        service.deleteById(1l);

        //then
        then(specialtyRepository).should(atMost(5)).deleteById(1L);
//        verify(specialtyRepository, atMost(5)).deleteById(1l);
    }

    @Test
    void deleteByIdNever() {

        //when
        service.deleteById(1l);
        service.deleteById(1l);

        then(specialtyRepository).should(atLeastOnce()).deleteById(1L);

//        verify(specialtyRepository, atLeastOnce()).deleteById(1l);

        then(specialtyRepository).should(never()).deleteById(5L);
//        verify(specialtyRepository, never()).deleteById(5l);
    }

    @Test
    void testDelete() {

        //when
        service.delete(new Speciality());

        //then
        then(specialtyRepository).should().delete(any());
    }

    @Test
    public void testDoThrow() {
        doThrow(new RuntimeException("boom")).when(specialtyRepository).delete(any());

        assertThrows(RuntimeException.class, () -> specialtyRepository.delete(new Speciality()));

        verify(specialtyRepository).delete(any());
    }

    @Test
    void testFindByIDThrows() {
        given(specialtyRepository.findById(1L)).willThrow(new RuntimeException("boom"));

        assertThrows(RuntimeException.class, ()-> service.findById(1L));

        then(specialtyRepository).should().findById(1L);
    }

    @Test
    void testDeleteBDD() {
        willThrow(new RuntimeException("boom")).given(specialtyRepository).delete(any());

        assertThrows(RuntimeException.class, () -> specialtyRepository.delete(new Speciality()));

        then(specialtyRepository).should().delete(any());
    }

    @Test
    void testSaveLambda() {
        //given
        final String MATCH_ME = "MATCH_ME";
        Speciality speciality = new Speciality();
        speciality.setDescription(MATCH_ME);

        Speciality savedSpecialty = new Speciality();
        savedSpecialty.setId(1L);

        //need mock to only return on match MATCH_ME string
        given(specialtyRepository.save(argThat(argument -> argument.getDescription().equals(MATCH_ME)))).willReturn(savedSpecialty);

        //when
        Speciality returnedSpecialty = service.save(speciality);

        //then
        assertThat(returnedSpecialty.getId()).isEqualTo(1L);
    }
}