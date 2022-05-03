package seguridad.controller;

import seguridad.dao.UsuarioDao;
import seguridad.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioControlador {

    @Autowired
    private UsuarioDao usuarioDao;

    //Send the form
    @RequestMapping(value="/registro")
    public String addUsuario(Model model, HttpSession session) {
        model.addAttribute("usuario", new Usuario());
        return "usuario/registro";
    }

    //Collect the form
    @RequestMapping(value="/registro", method= RequestMethod.POST)
    public String processAddSubmit(Model model, @ModelAttribute("usuario") Usuario usuario,
                                   BindingResult bindingResult) {

        usuarioDao.addUsuario(usuario);
        return "redirect:/";
    }

}
