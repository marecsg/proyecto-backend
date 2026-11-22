package pe.edu.upeu.LabManager.Fcm;

import com.google.api.core.ApiFuture;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FcmService {

    @Autowired
    private FirebaseApp firebaseApp;

    public String enviarNotificacion(String token, String titulo, String mensaje)
            throws FirebaseMessagingException {Message notificacion = Message.builder()
                .setToken(token)
                .setNotification(
                        Notification.builder()
                                .setTitle(titulo)
                                .setBody(mensaje)
                                .build()
                ).build();

        return FirebaseMessaging.getInstance(firebaseApp)
                .send(notificacion);
    }

    public ApiFuture<String> enviarNotificacionAsync(
            String token,
            String titulo,
            String mensaje
    ) {
        Message notificacion = Message.builder()
                .setToken(token)
                .setNotification(
                        Notification.builder()
                                .setTitle(titulo)
                                .setBody(mensaje)
                                .build()
                )
                .build();

        return FirebaseMessaging.getInstance(firebaseApp)
                .sendAsync(notificacion);
    }

}
