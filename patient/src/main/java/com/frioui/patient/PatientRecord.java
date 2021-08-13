package com.frioui.patient;



import javax.persistence.*;


import lombok.*;

@Entity
@Table(name = "patient_record")
@Data   //@Data = @Getter + @Setter + @ToString + @EqualsAndHashCode + @RequiredArgsConstructor
@NoArgsConstructor   //génère le constructeur sans argument et public
@AllArgsConstructor  //génère le constructeur avec tous les arguments et public
@Builder //génère une classe interne de type « Builder » capable de construire au moyen de « method chaining » une instance de la classe. L'opération terminale sera build() ;
public class PatientRecord{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long patientId;
    
    @NonNull //indique au Builder tous les champs obligatoires 
    private String name;
	
    @NonNull
    private Integer age;
    
    @NonNull 
    private String address;
}
