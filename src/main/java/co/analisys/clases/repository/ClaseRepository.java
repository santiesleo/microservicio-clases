package co.analisys.clases.repository;

import co.analisys.clases.model.Clase;
import co.analisys.clases.model.ClaseId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaseRepository extends JpaRepository<Clase, ClaseId> {
}
