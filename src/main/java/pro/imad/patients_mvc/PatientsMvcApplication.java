package pro.imad.patients_mvc;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pro.imad.patients_mvc.entities.Patient;
import pro.imad.patients_mvc.reposotories.PatientRepository;

import java.util.Date;

@SpringBootApplication
public class PatientsMvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(PatientsMvcApplication.class, args);
	}

	//@Bean
	CommandLineRunner commandLineRunner(PatientRepository patientRepository){
		return args -> {
			patientRepository.save(new Patient(null,"hassan",new Date(),true,122));
			patientRepository.save(new Patient(null,"yassine",new Date(),false,152));
			patientRepository.save(new Patient(null,"hanae",new Date(),true,125));
			patientRepository.findAll().forEach(p->{
				System.out.println(p.getNom());
			});
		};
	}





}
