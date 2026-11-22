
package pe.edu.upeu.LabManager.Fcm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import pe.edu.upeu.LabManager.Entity.Usuario;
import pe.edu.upeu.LabManager.Entity.Enums.Roles;
import pe.edu.upeu.LabManager.Repository.UsuarioRepository;

@Service
public class NotificacionService {

    private static final Logger log =
            LoggerFactory.getLogger(NotificacionService.class);

    private final FcmService fcmService;
    private final UsuarioRepository usuarioRepository;

    public NotificacionService(
            FcmService fcmService,
            UsuarioRepository usuarioRepository
    ) {
        this.fcmService = fcmService;
        this.usuarioRepository = usuarioRepository;
    }

    public void notificarUsuario(
            Usuario usuario,
            String titulo,
            String mensaje
    ) {
        if (usuario == null ||
                usuario.getFcmToken() == null ||
                usuario.getFcmToken().isBlank()) {
            return;
        }

        programarEnvio(
                usuario.getFcmToken(),
                titulo,
                mensaje
        );
    }

    public void notificarEncargados(
            Integer idAmbiente,
            String titulo,
            String mensaje
    ) {
        var encargados =
                usuarioRepository.findByAmbiente_IdAmbienteAndRol_Nombre(
                        idAmbiente,
                        Roles.ENCARGADO
                );

        for (Usuario encargado : encargados) {
            notificarUsuario(encargado, titulo, mensaje);
        }
    }

    private void programarEnvio(
            String token,
            String titulo,
            String mensaje
    ) {
        if (!TransactionSynchronizationManager.isActualTransactionActive()
                || !TransactionSynchronizationManager.isSynchronizationActive()) {
            throw new IllegalStateException("La notificación requiere una transacción activa");
        }

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {

                    @Override
                    public void afterCommit() {
                        try {
                            fcmService.enviarNotificacion(
                                    token,
                                    titulo,
                                    mensaje
                            );
                        } catch (Exception e) {
                            log.warn(
                                    "Error enviando notificación FCM: {}",
                                    e.getMessage()
                            );
                        }
                    }
                }
        );
    }
}
