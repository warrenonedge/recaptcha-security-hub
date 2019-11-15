package no.mad.recpatcha.recaptchasecurityhub.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import no.mad.recpatcha.recaptchasecurityhub.dao.RecaptchaRepository;
import no.mad.recpatcha.recaptchasecurityhub.model.Recaptcha;

@Controller
public class RecaptchaUIController {
    
    @Autowired
    RecaptchaRepository recaptchaRepository;

    @GetMapping({"/","/search"})
    public String searchRecaptcha(@RequestParam Optional<Recaptcha> recaptcha, @RequestParam(required = false, defaultValue = "false") Boolean isSearched, Model model) {
        if (recaptcha.isPresent()) {
            model.addAttribute("recaptcha", recaptcha.get());
        } 
        model.addAttribute("isSearched", isSearched);

        return "search";
    }

    @PostMapping({"/","/search"})
    public String searchRecaptcha(@RequestParam Optional<Integer> id, @RequestParam String application, Model model) {
        Optional<Recaptcha> recaptcha = (id.isPresent()) ? recaptchaRepository.findById(id.get()) : Optional.ofNullable(recaptchaRepository.findByApplication(application));
        
        return searchRecaptcha(recaptcha, true, model);
    }

    @GetMapping("/create")
    public String createRecaptcha(Model model) {
        Recaptcha recaptcha = new Recaptcha();
        model.addAttribute("recaptcha", recaptcha);
        return "create";
    }

    @PostMapping({"/create", "/modify"})
    public String createRecaptcha(@ModelAttribute @Valid Recaptcha recaptcha, Model model) {
        recaptcha = recaptchaRepository.save(recaptcha);

        return searchRecaptcha(Optional.of(recaptcha), false, model);
    }

    @GetMapping("/all")
    public String getAllRecaptcha(Model model) {
        Iterable<Recaptcha> recaptchaList = recaptchaRepository.findAll();

        model.addAttribute("recaptchaList", recaptchaList);
        return "allRecaptcha";
    }
}