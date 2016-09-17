package ch.hftm.wtm.presentation;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author HOEFI
 * @since 06.06.2016
 * @version 1.0
 */
@ManagedBean
@SessionScoped
public class IndexBean {

    @ManagedProperty(value = "#{loginBean}")
    LoginBean loginBean;
}
