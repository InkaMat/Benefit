package pl.lodz.p.it.spjava.benefit.web.utils;

import java.io.Serializable;
import java.util.Locale;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class UtilsLanguageBean implements Serializable {

    private Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

    private String language = locale.toString();

    public Locale getLocale() {
        return locale;
    }

    public String getLanguage() {
        return locale.getLanguage();
    }

    public void changeLanguage(String language) {
        locale = new Locale(language);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale(language));
    }
}
