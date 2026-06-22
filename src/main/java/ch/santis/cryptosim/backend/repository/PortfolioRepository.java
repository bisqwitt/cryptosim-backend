package ch.santis.cryptosim.backend.repository;

import ch.santis.cryptosim.backend.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
}
