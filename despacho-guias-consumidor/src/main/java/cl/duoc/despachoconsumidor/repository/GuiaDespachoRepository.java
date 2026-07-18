package cl.duoc.despachoconsumidor.repository;

import cl.duoc.despachoconsumidor.entity.GuiaDespacho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuiaDespachoRepository extends JpaRepository<GuiaDespacho, Long> {
}
