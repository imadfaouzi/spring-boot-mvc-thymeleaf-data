package pro.imad.patients_mvc.web;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pro.imad.patients_mvc.entities.Patient;
import pro.imad.patients_mvc.reposotories.PatientRepository;

import java.util.Optional;

@Controller
@AllArgsConstructor
public class PatientController {
   private PatientRepository patientRepository;

   @GetMapping("/index")
   private String patients(Model model,
                           @RequestParam (name = "page", defaultValue = "0") int page,
                           @RequestParam (name = "size", defaultValue = "5") int size,
                           @RequestParam (name = "keyword", defaultValue = "") String keyword){

      Page<Patient> pagepatients = patientRepository.findByNomContains(keyword, PageRequest.of(page, size));
      model.addAttribute("patients", pagepatients.getContent());
      model.addAttribute("pages" , new int[pagepatients.getTotalPages()]);
      model.addAttribute("keyword", keyword);
      model.addAttribute("currentPage", page);
      model.addAttribute("size", size);
      model.addAttribute("numberOfpatients", pagepatients.getContent().size());

      Long numberOfpatientsTotal;
      if(keyword.isEmpty()){
          numberOfpatientsTotal = patientRepository.count();
      }else{
          numberOfpatientsTotal = patientRepository.countByNomContains(keyword);
      }
      model.addAttribute("numberOfpatientsTotal", numberOfpatientsTotal);

      return "patients";
   }

   @GetMapping("/delete")
   public String delete(Long id,@RequestParam (name = "keyword", defaultValue = "") String keyword ,
                        @RequestParam (name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "5") int size){
      patientRepository.deleteById(id);
      return "redirect:/index?page="+page+"&keyword="+keyword+"&size="+size;
   }

   @GetMapping("/formPatient")
   public String formPatient(Model model){
      model.addAttribute("patient", new Patient());
      return "formPatient";
   }


   @PostMapping("/save")
   private String save(Model model, @Valid Patient patient, BindingResult bindingResult,
                       @RequestParam(name = "keyword", defaultValue = "") String keyword ,
                       @RequestParam(name = "page", defaultValue = "0")int page,
                       @RequestParam(name = "size", defaultValue = "5")int size){
      if(bindingResult.hasErrors()) return  "formPatient";
      patientRepository.save(patient);
      System.out.println(patient);
      return "redirect:/index?page="+page+"&keyword="+keyword+"&size="+size;
   }

   @GetMapping("/editPatient")
   public String editPatient(Model model, Long id, @RequestParam (name = "keyword", defaultValue = "") String keyword ,
                             @RequestParam (name = "page", defaultValue = "0") int page,
                             @RequestParam(name = "size", defaultValue = "5") int size){
      Patient patient = patientRepository.findById(id).orElse(null);
       if(patient == null){
            throw  new RuntimeException("patient in trouvable");
       }
       model.addAttribute("patient", patient);
       model.addAttribute("page", page);
       model.addAttribute( "keyword", keyword);
       model.addAttribute( "size", size);
       return "editPatient";
   }
}
