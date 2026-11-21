
package pe.edu.upeu.LabManager.Services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.locks.ReentrantLock;

@Service
public class CorrelativoService {

    @PersistenceContext
    private EntityManager entityManager;

    private final ReentrantLock lock = new ReentrantLock();

    @Transactional(propagation = Propagation.MANDATORY)
    public String generar(String tipo) {

        String tabla = switch (tipo) {
            case "SOL" -> "solicitudes";
            case "PRE" -> "prestamos";
            case "INC" -> "tickets_incidencias";
            default -> throw new IllegalArgumentException("Tipo inválido");
        };

        lock.lock();

        try {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCompletion(int status) {
                            lock.unlock();
                        }
                    }
            );
        } catch (RuntimeException e) {
            lock.unlock();
            throw e;
        }

        Number ultimo = (Number) entityManager.createNativeQuery(
                "SELECT COALESCE(MAX(CAST(" +
                        "SUBSTRING(codigo_correlativo, 5) " +
                        "AS UNSIGNED)), 0) FROM " + tabla
        ).getSingleResult();

        return String.format("%s-%04d", tipo, ultimo.longValue() + 1);
    }
}
