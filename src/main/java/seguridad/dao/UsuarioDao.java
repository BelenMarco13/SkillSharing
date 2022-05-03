package seguridad.dao;

import org.springframework.stereotype.Repository;
import seguridad.model.Usuario;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UsuarioDao {
    private final Map<String, Usuario> listaUsuarios = new HashMap<String, Usuario>();

    public void addUsuario(Usuario usuario){
        listaUsuarios.put(usuario.getCorreo(), usuario);
    }

    public Usuario getUsuario(String correo){
        return listaUsuarios.get(correo);
    }
}
