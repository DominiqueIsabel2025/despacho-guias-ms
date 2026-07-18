package cl.duoc.despachoproductor.repository;

import cl.duoc.despachoproductor.entity.GuiaDespacho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GuiaDespachoRepository extends JpaRepository<GuiaDespacho, Long> {

    List<GuiaDespacho> findByTransportistaAndFechaDespacho(String transportista, LocalDate fechaDespacho);

}
